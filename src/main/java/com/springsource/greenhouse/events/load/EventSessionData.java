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

import java.util.List;

/*
 * EventSessionData クラスは、イベントセッションに関連するデータを保持します。
 * 各フィールドは、イベントの詳細を表します。
 */
class EventSessionData {

	private final long event;
	private final int id;
	private final String title;
	private final String description;
	private final String hashtag;
	private final Long venue;
	private final Long timeslot;
	private final String source;
	private final long sourceId;
	private final List<Long> leaderIds;

	class EventDetails {
		private final String title;
		private final String description;
		private final String hashtag;
		private final Long venue;
		private final Long timeslot;

		public EventDetails(String title, String description, String hashtag, Long venue, Long timeslot) {
			this.title = title;
			this.description = description;
			this.hashtag = hashtag;
			this.venue = venue;
			this.timeslot = timeslot;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public String getHashtag() {
			return hashtag;
		}

		public Long getVenue() {
			return venue;
		}

		public Long getTimeslot() {
			return timeslot;
		}
	}

	public EventSessionData(long event, int id, EventDetails eventDetails, String source, long sourceId,
			List<Long> leaderIds) {
		// EventSessionData コンストラクタは、すべてのフィールドを初期化します。
		this.event = event;
		this.id = id;
		this.title = eventDetails.getTitle();
		this.description = eventDetails.getDescription();
		this.hashtag = eventDetails.getHashtag();
		this.venue = eventDetails.getVenue();
		this.timeslot = eventDetails.getTimeslot();
		this.source = source;
		this.sourceId = sourceId;
		this.leaderIds = leaderIds;
	}

	public long getEvent() {
		return event;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getHashtag() {
		return hashtag;
	}

	public Long getVenue() {
		return venue;
	}

	public Long getTimeslot() {
		return timeslot;
	}

	public String getSource() {
		return source;
	}

	public long getSourceId() {
		return sourceId;
	}

	public List<Long> getLeaderIds() {
		return leaderIds;
	}

}
