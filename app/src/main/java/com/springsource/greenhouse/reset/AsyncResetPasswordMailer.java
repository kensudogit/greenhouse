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

import org.springframework.core.convert.converter.Converter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 非同期でパスワードリセットメールを送信する実装クラス。
 * RequestPasswordRequestをメール送信可能なSimpleMailMessageに変換するためにConverterを使用します。
 * 
 * @author Keith Donald
 */
@Component
public final class AsyncResetPasswordMailer implements ResetPasswordMailer {

	private final Converter<ResetPasswordRequest, SimpleMailMessage> converter;

	private final MailSender mailSender;

	@Inject
	public AsyncResetPasswordMailer(Converter<ResetPasswordRequest, SimpleMailMessage> converter,
			MailSender mailSender) {
		this.converter = converter;
		this.mailSender = mailSender;
	}

	/**
	 * 非同期でパスワードリセットリクエストをメールとして送信します。
	 * 
	 * @param request パスワードリセットリクエスト
	 */
	@Async
	public void send(final ResetPasswordRequest request) {
		// リクエストをSimpleMailMessageに変換
		SimpleMailMessage mail = converter.convert(request);
		// メールを送信
		mailSender.send(mail);
	}

}