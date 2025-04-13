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

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Model backing the "Register New App" and "Update Existing App" forms.
 * 
 * @author Keith Donald
 */
public class AppForm {

	@NotEmpty
	private String name;

	@NotEmpty
	private String description;

	private String organization;

	private String website;

	private String callbackUrl;

	/**
	 * アプリの名前を取得します。
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * アプリの名前を設定します。
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * アプリの短い説明を取得します。
	 */
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * アプリを公開する組織を取得します。
	 */
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * アプリに関する情報を得るためのウェブサイトを取得します。
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	/**
	 * メンバーがアプリに接続した後にリダイレクトされるURLを取得します。
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}
