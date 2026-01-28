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
package com.springsource.greenhouse.signup;

import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;

import com.springsource.greenhouse.account.Account;

/**
 * 新しいアカウントをウェルカムメールメッセージに変換します。
 * integration-signup.xml パイプラインの一部として使用され、新しいメンバーにウェルカムメールを送信します。
 * 
 * @author Keith Donald
 */
public class WelcomeMailTransformer {

	private final StringTemplateFactory welcomeTemplateFactory = new ResourceStringTemplateFactory(
			new ClassPathResource("welcome.st", getClass()));

	/**
	 * アカウントをメールメッセージに変換します。
	 */
	@Transformer
	public MailMessage welcomeMail(Account account) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("Greenhouse <noreply@springsource.com>");
		mailMessage.setTo(account.getEmail());
		mailMessage.setSubject("Welcome to the Greenhouse!");
		// テキストテンプレートを取得し、アカウント情報を設定します。
		StringTemplate textTemplate;
		textTemplate = welcomeTemplateFactory.getStringTemplate();
		textTemplate.put("firstName", account.getFirstName());
		textTemplate.put("profileUrl", account.getProfileUrl());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}