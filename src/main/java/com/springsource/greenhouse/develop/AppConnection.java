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
package com.springsource.greenhouse.develop;

/**
 * クライアントアプリケーションとメンバーアカウントの間の接続。
 * 接続が確立されている間、クライアントアプリケーションはメンバーに代わってデータを読み取り、更新する権限を持ちます。
 * これは、クライアントアプリケーションが保護されたリソースにアクセスするためのリクエストでアクセストークンを提示することによって達成されます。
 * 
 * @author Keith Donald
 */
public final class AppConnection {

	private final Long accountId;

	private final String apiKey;

	private final String accessToken;

	private final String secret;

	public AppConnection(Long accountId, String apiKey, String accessToken, String secret) {
		this.accountId = accountId;
		this.apiKey = apiKey;
		this.accessToken = accessToken;
		this.secret = secret;
	}

	/**
	 * メンバーアカウントID。
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * クライアントアプリケーションを識別するAPIキー。
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * 接続を識別するアクセストークン。
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * OAuth 1.0ベースのクライアントの署名検証に使用されるアクセストークンシークレット。
	 */
	public String getSecret() {
		return secret;
	}

}
