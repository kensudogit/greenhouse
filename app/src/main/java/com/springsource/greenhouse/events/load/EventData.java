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

// イベントデータを表すクラス
class EventData {

	private final long memberGroupId;
	private final String name;
	private final String description;
	private final String abbreviation;
	private final String firstDay;
	private final String lastDay;
	private final String timeZone;
	private final String source;
	private final long sourceId;

	// パラメータをカプセル化するクラス
	static class EventDataParameters {
		final long memberGroupId;
		final String name;
		final String description;
		final String abbreviation;
		final String firstDay;
		final String lastDay;
		final String timeZone;
		final String source;
		final long sourceId;

		private EventDataParameters(Builder builder) {
			this.memberGroupId = builder.memberGroupId;
			this.name = builder.name;
			this.description = builder.description;
			this.abbreviation = builder.abbreviation;
			this.firstDay = builder.firstDay;
			this.lastDay = builder.lastDay;
			this.timeZone = builder.timeZone;
			this.source = builder.source;
			this.sourceId = builder.sourceId;
		}

		// Builderクラス
		static class Builder {
			private long memberGroupId;
			private String name;
			private String description;
			private String abbreviation;
			private String firstDay;
			private String lastDay;
			private String timeZone;
			private String source;
			private long sourceId;

			Builder memberGroupId(long memberGroupId) {
				this.memberGroupId = memberGroupId;
				return this;
			}

			Builder name(String name) {
				this.name = name;
				return this;
			}

			Builder description(String description) {
				this.description = description;
				return this;
			}

			Builder abbreviation(String abbreviation) {
				this.abbreviation = abbreviation;
				return this;
			}

			Builder firstDay(String firstDay) {
				this.firstDay = firstDay;
				return this;
			}

			Builder lastDay(String lastDay) {
				this.lastDay = lastDay;
				return this;
			}

			Builder timeZone(String timeZone) {
				this.timeZone = timeZone;
				return this;
			}

			Builder source(String source) {
				this.source = source;
				return this;
			}

			Builder sourceId(long sourceId) {
				this.sourceId = sourceId;
				return this;
			}

			EventDataParameters build() {
				return new EventDataParameters(this);
			}
		}
	}

	/**
	 * Constructs EventData with all required fields.
	 */
	public EventData(long memberGroupId, String name, String description, String abbreviation,
			String firstDay, String lastDay, String timeZone, String source, long sourceId) {
		this.memberGroupId = memberGroupId;
		this.name = name;
		this.description = description;
		this.abbreviation = abbreviation;
		this.firstDay = firstDay;
		this.lastDay = lastDay;
		this.timeZone = timeZone;
		this.source = source;
		this.sourceId = sourceId;
	}

	/**
	 * Constructs EventData using EventDataParameters object.
	 */
	public EventData(EventDataParameters params) {
		this.memberGroupId = params.memberGroupId;
		this.name = params.name;
		this.description = params.description;
		this.abbreviation = params.abbreviation;
		this.firstDay = params.firstDay;
		this.lastDay = params.lastDay;
		this.timeZone = params.timeZone;
		this.source = params.source;
		this.sourceId = params.sourceId;
	}

	// メンバーグループIDを取得します
	public long getMemberGroupId() {
		return memberGroupId;
	}

	// 名前を取得します
	public String getName() {
		return name;
	}

	// 説明を取得します
	public String getDescription() {
		return description;
	}

	// 略称を取得します
	public String getAbbreviation() {
		return abbreviation;
	}

	// 開始日を取得します
	public String getFirstDay() {
		return firstDay;
	}

	// 終了日を取得します
	public String getLastDay() {
		return lastDay;
	}

	// タイムゾーンを取得します
	public String getTimeZone() {
		return timeZone;
	}

	// ソースを取得します
	public String getSource() {
		return source;
	}

	// ソースIDを取得します
	public long getSourceId() {
		return sourceId;
	}

}
