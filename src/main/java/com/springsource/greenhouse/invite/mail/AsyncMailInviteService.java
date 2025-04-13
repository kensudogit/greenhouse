/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.invite.mail;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.templating.LocalStringTemplate;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.invite.InviteRepository;
import com.springsource.greenhouse.invite.Invitee;

/**
 * メール招待サービスの実装で、別スレッドで非同期にメール招待を送信します。
 * Springの@Asyncサポートを利用して非同期動作を実現しています。
 * StringTemplateを使用してテンプレートから招待メールのテキストを生成します。
 * テキストには招待受諾ページへのリンクが含まれ、招待者にそのリンクを有効化するよう指示します。
 * 
 * @author Keith Donald
 */
@Service
public class AsyncMailInviteService implements MailInviteService {

	private final MailSender mailSender;

	private final InviteRepository inviteRepository;

	private final UriTemplate acceptUriTemplate;

	private final MailInviteService mailInviteService;

	/**
	 * AsyncMailInviteServiceを作成します。
	 * 
	 * @param mailSender        JavaMail APIを使用してメールを実際に配信するオブジェクト。
	 * @param inviteRepository  招待を保存して後で取得するためのインターフェース。
	 * @param acceptUriTemplate 招待を受け入れるために訪問すべきURLを生成するためのテンプレート。
	 * @param mailInviteService 注入されたインターフェース。
	 */
	@Inject
	public AsyncMailInviteService(MailSender mailSender, InviteRepository inviteRepository,
			@Value("#{environment['application.secureUrl']}/invite/accept?token={token}") String acceptUriTemplate,
			MailInviteService mailInviteService) {
		this.mailSender = mailSender;
		this.inviteRepository = inviteRepository;
		this.acceptUriTemplate = new UriTemplate(acceptUriTemplate);
		this.mailInviteService = mailInviteService;
	}

	/**
	 * 招待を送信します。
	 * 
	 * @param from           招待を送信するアカウント。
	 * @param to             招待を受け取る招待者のリスト。
	 * @param invitationBody 招待メッセージの本文。
	 */
	public void sendInvite(Account from, List<Invitee> to, String invitationBody) {
		StringTemplate textTemplate = textTemplateFactory.getStringTemplate();
		textTemplate.put("account", from);
		StringTemplate bodyTemplate = new LocalStringTemplate(invitationBody);
		for (Invitee invitee : to) {
			bodyTemplate.put("invitee", invitee);
			textTemplate.put("body", bodyTemplate.render());
			String token = tokenGenerator.generateKey();
			textTemplate.put("acceptUrl", acceptUriTemplate.expand(token));
			mailInviteService.send(from, invitee, textTemplate.render(), token);
		}
	}

	// internal helpers

	/**
	 * 非同期で招待メールを送信します。
	 * 
	 * @param from  招待を送信するアカウント。
	 * @param to    招待を受け取る招待者。
	 * @param text  招待メールのテキスト。
	 * @param token 招待トークン。
	 */
	@Async
	@Transactional
	public void send(Account from, Invitee to, String text, String token) {
		SimpleMailMessage mailMessage = createInviteMailMessage(to, text);
		if (!inviteRepository.alreadyInvited(to.getEmail())) {
			inviteRepository.saveInvite(token, to, text, from.getId());
			mailSender.send(mailMessage);
		}
	}

	/**
	 * 招待メールメッセージを作成します。
	 * 
	 * @param to   招待を受け取る招待者。
	 * @param text 招待メールのテキスト。
	 * @return 作成されたSimpleMailMessageオブジェクト。
	 */
	private SimpleMailMessage createInviteMailMessage(Invitee to, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("Greenhouse <noreply@springsource.com>");
		mailMessage.setTo(to.getEmail());
		mailMessage.setSubject("Your Greenhouse Invitation");
		mailMessage.setText(text);
		return mailMessage;
	}

	private final StringTemplateFactory textTemplateFactory = new ResourceStringTemplateFactory(
			new ClassPathResource("invite-text.st", getClass()));

	private final StringKeyGenerator tokenGenerator = KeyGenerators.string();

}

public interface MailInviteService {
	void sendInvite(Account from, List<Invitee> to, String invitationBody);

	void send(Account from, Invitee to, String text, String token);
}