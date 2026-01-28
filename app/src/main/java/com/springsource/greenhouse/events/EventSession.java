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
package com.springsource.greenhouse.events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.springsource.greenhouse.utils.SubResourceReference;

/**
 * A meeting of interest scheduled during an event.
 * A session has an start-time and an end-time that occurs within the range of
 * the Event.
 * A session is typically assigned a Room in a Venue where the Event is held.
 * A session has one or more Leaders that lead attendees in the discussion of
 * the topic of interest.
 * Once a session is delivered, its value maybe rated by each attendee.
 * A session may also be favorited to indicate it is an highlight of the Event.
 * 
 * @author Keith Donald
 */
public class EventSession {

	private final Integer id;

	private final String title;

	private final DateTime startTime;

	private final DateTime endTime;

	private final String description;

	private final String hashtag;

	private final Float rating;

	private final SubResourceReference<Long, Integer> room;

	private final Boolean favorite;

	private List<EventSessionLeader> leaders;

	/**
	 * Constructs an EventSession with all required fields.
	 */
	public EventSession(Integer id, String title, DateTime startTime, DateTime endTime,
			String description, String hashtag, Float rating,
			SubResourceReference<Long, Integer> room, Boolean favorite) {
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.hashtag = hashtag;
		this.rating = rating;
		this.room = room;
		this.favorite = favorite;
	}

	private EventSession(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.startTime = builder.startTime;
		this.endTime = builder.endTime;
		this.description = builder.description;
		this.hashtag = builder.hashtag;
		this.rating = builder.rating;
		this.room = builder.room;
		this.favorite = builder.favorite;
	}

	/**
	 * The internal id of the session.
	 * This id is unique relative to the Event where the session is delivered.
	 * セッションの内部ID。
	 * このIDは、セッションが提供されるイベントに対して一意です。
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * The title of the session; a clear, concise summary of the topic.
	 * セッションのタイトル。トピックの明確で簡潔な要約。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The time the session starts.
	 * セッションが開始する時間。
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * The time the session ends.
	 * セッションが終了する時間。
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * A paragraph description of the session that describes the scope of what's
	 * covered and how attendees will benefit.
	 * セッションの説明文。カバーされる範囲と参加者がどのように利益を得るかを説明します。
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The hashtag used to identify the Twitter conversation covering this session.
	 * このセッションをカバーするTwitterの会話を識別するために使用されるハッシュタグ。
	 */
	public String getHashtag() {
		return hashtag;
	}

	/**
	 * The session's average rating.
	 * May be null if there have been no ratings.
	 * セッションの平均評価。
	 * 評価がない場合はnullになることがあります。
	 */
	public Float getRating() {
		return rating;
	}

	/**
	 * A reference to the room where the session is held.
	 * セッションが開催される部屋への参照。
	 */
	public SubResourceReference<Long, Integer> getRoom() {
		return room;
	}

	/**
	 * If this session is a favorite.
	 * This could be attendee-relative or event-wide-relative depending upon the use
	 * case.
	 * このセッションがお気に入りかどうか。
	 * 使用ケースに応じて、参加者相対またはイベント全体相対である可能性があります。
	 */
	public boolean isFavorite() {
		return favorite;
	}

	/**
	 * The people who will be leading, or delivering, this session.
	 * このセッションをリードまたは提供する人々。
	 */
	public List<EventSessionLeader> getLeaders() {
		return Collections.unmodifiableList(leaders);
	}

	/**
	 * Add a session leader.
	 * This is called when building the object and should not be called after that.
	 */
	public void addLeader(EventSessionLeader leader) {
		if (leaders == null) {
			leaders = new LinkedList<EventSessionLeader>();
		}
		leaders.add(leader);
	}

	public static class Builder {
		private Integer id;
		private String title;
		private DateTime startTime;
		private DateTime endTime;
		private String description;
		private String hashtag;
		private Float rating;
		private SubResourceReference<Long, Integer> room;
		private Boolean favorite;

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder startTime(DateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder endTime(DateTime endTime) {
			this.endTime = endTime;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder hashtag(String hashtag) {
			this.hashtag = hashtag;
			return this;
		}

		public Builder rating(Float rating) {
			this.rating = rating;
			return this;
		}

		public Builder room(SubResourceReference<Long, Integer> room) {
			this.room = room;
			return this;
		}

		public Builder favorite(Boolean favorite) {
			this.favorite = favorite;
			return this;
		}

		public EventSession build() {
			return new EventSession(this);
		}
	}
}