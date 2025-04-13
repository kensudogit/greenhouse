/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.config;

import java.util.Properties;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * メール送信のための設定クラス。
 * JavaMail APIとSpringのJavaMailサポートに依存しています。
 * Spring Integrationの「サインアップパイプライン」と「招待」モジュールで使用されます。
 * 
 * @author Keith Donald
 */
@Configuration
public class MailConfig {

	private final Environment environment;

	@Inject
	public MailConfig(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Java Mail送信者。
	 * メール送信が埋め込みモードで動作することは一般的に期待されていません。
	 * このメール送信者は常に非同期で呼び出されるため、開発者に問題を引き起こすことはありません。
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setHost(environment.getProperty("mail.host"));
		mailSender.setPort(environment.getProperty("mail.port", Integer.class, 25));
		mailSender.setUsername(environment.getProperty("mail.username"));
		mailSender.setPassword(environment.getProperty("mail.password"));
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth", Boolean.class, false));
		properties.put("mail.smtp.starttls.enable",
				environment.getProperty("mail.smtp.starttls.enable", Boolean.class, false));
		mailSender.setJavaMailProperties(properties);
		return mailSender;
	}

}