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
package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.web.util.UriTemplate;

/**
 * ResetPasswordRequest を SimpleMailMessage に変換し、メール送信のために使用します。
 * リセットパスワードのメールテキストは、文字列テンプレートを使用して生成されます。
 * 
 * @author Keith Donald
 */
@Component
public final class ResetPasswordMailMessageConverter implements Converter<ResetPasswordRequest, SimpleMailMessage> {

	private final StringTemplateFactory resetTemplateFactory = new ResourceStringTemplateFactory(
			new ClassPathResource("reset-password.st", getClass()));

	private final UriTemplate resetUriTemplate;

	/**
	 * コンストラクタ。リセットパスワードの URI テンプレートを初期化します。
	 * 
	 * @param resetUriTemplate リセットパスワードの URI テンプレート
	 */
	@Inject
	public ResetPasswordMailMessageConverter(
			@Value("#{environment['application.secureUrl']}/reset?token={token}") String resetUriTemplate) {
		this.resetUriTemplate = new UriTemplate(resetUriTemplate);
	}

	/**
	 * ResetPasswordRequest を SimpleMailMessage に変換します。
	 * 
	 * @param request リセットパスワードリクエスト
	 * @return SimpleMailMessage オブジェクト
	 */
	public SimpleMailMessage convert(ResetPasswordRequest request) {
		// メールメッセージオブジェクトを作成
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// 送信者を設定
		mailMessage.setFrom("Greenhouse <noreply@springsource.com>");
		// 受信者を設定
		mailMessage.setTo(request.getAccount().getEmail());
		// 件名を設定
		mailMessage.setSubject("Reset your Greenhouse password");
		// テキストテンプレートを取得
		StringTemplate textTemplate = resetTemplateFactory.getStringTemplate();
		// テンプレートに変数を設定
		textTemplate.put("firstName", request.getAccount().getFirstName());
		textTemplate.put("resetUrl", resetUriTemplate.expand(request.getToken()));
		// テンプレートをレンダリングしてメール本文を設定
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}