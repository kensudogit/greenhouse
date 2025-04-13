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
package com.springsource.greenhouse.develop.oauth;

/**
 * クライアントによって送信されたリクエストトークンが無効な場合にスローされます。
 * これは、OAuthセッションが完了または期限切れになった場合に発生する可能性があります。
 * 
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public class InvalidRequestTokenException extends Exception {

	private final String requestToken;

	public InvalidRequestTokenException(String requestToken) {
		super("invalid request token"); // 無効なリクエストトークン
		this.requestToken = requestToken;
	}

	public String getRequestToken() {
		return requestToken; // リクエストトークンを返します
	}

}