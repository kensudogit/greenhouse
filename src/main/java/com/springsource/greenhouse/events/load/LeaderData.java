/*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.events.load;

/*
 * LeaderData クラスは、リーダーの情報を保持するためのデータクラスです。
 * 各リーダーの名前、経歴、個人URL、Twitter ID、情報のソース、およびソースIDを格納します。
 */
public class LeaderData {

	private final String name;
	private final String bio;
	private final String personalUrl;
	private final String twitterId;
	private final String source;
	private final long sourceId;

	public LeaderData(String name, String bio, String personalUrl, String twitterId, String source, long sourceId) {
		this.name = name;
		this.bio = bio;
		this.personalUrl = personalUrl;
		this.twitterId = twitterId;
		this.source = source;
		this.sourceId = sourceId;
	}

	// 名前を取得します。
	public String getName() {
		return name;
	}

	// 経歴を取得します。
	public String getBio() {
		return bio;
	}

	// 個人URLを取得します。
	public String getPersonalUrl() {
		return personalUrl;
	}

	// Twitter IDを取得します。
	public String getTwitterId() {
		return twitterId;
	}

	// 情報のソースを取得します。
	public String getSource() {
		return source;
	}

	// ソースIDを取得します。
	public long getSourceId() {
		return sourceId;
	}

}
