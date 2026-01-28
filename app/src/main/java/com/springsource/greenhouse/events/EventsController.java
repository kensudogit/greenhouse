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

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * UI Controller for Event actions.
 *
 * @author Keith Donald
 */
@RestController
public class EventsController {

	private final EventRepository eventRepository;

	private final Twitter twitter;

	@Inject
	public EventsController(EventRepository eventRepository, Twitter twitter) {
		this.eventRepository = eventRepository;
		this.twitter = twitter;
	}

	// for web service (JSON) clients

	/**
	 * イベントのリストをJSON形式でレスポンスのボディに書き込みます。
	 * 'GET /events' リクエストのみをマッチし、JSONコンテンツを返します。
	 * それ以外の場合は404を送信します。
	 * TODO: サポートされていない表現（XMLなど）が要求された場合、406を送信します。
	 * SPR-7353を参照してください。
	 */
	@GetMapping(value = "/events", produces = "application/json")
	public List<Event> upcomingEvents(
			@RequestHeader("Accept") String acceptHeader,
			@RequestParam(value = "after", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Long afterMillis) {
		checkMediaType(acceptHeader);
		return eventRepository.findUpcomingEvents(afterMillis);
	}

	/**
	 * イベントのお気に入りリストをレスポンスのボディに書き込みます。
	 */
	@GetMapping(value = "/events/{eventId}/favorites", produces = "application/json")
	public List<EventSession> favorites(@PathVariable Long eventId, Account account) {
		return eventRepository.findEventFavorites(eventId, account.getId());
	}

	/**
	 * イベントのツイート検索結果のページをレスポンスのボディに書き込みます。
	 * ページ番号とサイズはクライアントが指定できます。指定されていない場合、デフォルトで10件の結果の最初のページを返します。
	 */
	@GetMapping(value = "/events/{eventId}/tweets", produces = "application/json")
	public SearchResults tweets(@PathVariable Long eventId, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		String searchString = eventRepository.findEventSearchString(eventId);
		if (searchString != null && !searchString.isEmpty()) {
			// Spring Social 1.1.2 doesn't support page number directly, use pageSize only
			return twitter.searchOperations().search(searchString, pageSize);
		}
		return null;
	}

	/**
	 * イベントについてのツイートをTwitterに投稿します。
	 * 成功した場合、OKステータスを返します。
	 */
	@PostMapping(value = "/events/{eventId}/tweets")
	public ResponseEntity<String> postTweet(@PathVariable Long eventId, @RequestParam String status,
			Location currentLocation) {
		twitter.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * 他のイベントツイートをリツイートします。
	 */
	@PostMapping(value = "/events/{eventId}/retweet")
	public ResponseEntity<String> postRetweet(@PathVariable Long eventId, @RequestParam Long tweetId) {
		twitter.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * 出席者のお気に入りセッションのリストをレスポンスのボディに書き込みます。
	 */
	@GetMapping(value = "/events/{eventId}/sessions/favorites", produces = "application/json")
	public List<EventSession> favoriteSessions(@PathVariable Long eventId, Account account) {
		return eventRepository.findAttendeeFavorites(eventId, account.getId());
	}

	/**
	 * 指定された日のセッションをレスポンスのボディに書き込みます。
	 */
	@GetMapping(value = "/events/{eventId}/sessions/{day}", produces = "application/json")
	public List<EventSession> sessionsOnDay(@PathVariable Long eventId,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate day, Account account) {
		return eventRepository.findSessionsOnDay(eventId, day, account.getId());
	}

	/**
	 * セッションを出席者のお気に入りとしてトグルします。
	 * 新しいお気に入りステータスをレスポンスのボディに書き込みます。
	 */
	@PutMapping(value = "/events/{eventId}/sessions/{sessionId}/favorite")
	public Boolean toggleFavorite(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account) {
		return eventRepository.toggleFavorite(eventId, sessionId, account.getId());
	}

	/**
	 * 出席者がセッションに与えた評価を追加または更新します。
	 * セッションの新しい平均評価をレスポンスのボディに書き込みます。
	 */
	@PostMapping(value = "/events/{eventId}/sessions/{sessionId}/rating")
	public Float updateRating(@PathVariable Long eventId, @PathVariable Integer sessionId, Account account,
			@RequestParam Short value, @RequestParam String comment) throws RatingPeriodClosedException {
		return eventRepository.rate(eventId, sessionId, account.getId(), new Rating(value, comment));
	}

	/**
	 * セッションのツイート検索結果のページをレスポンスのボディに書き込みます。
	 */
	@GetMapping(value = "/events/{eventId}/sessions/{sessionId}/tweets", produces = "application/json")
	public SearchResults sessionTweets(@PathVariable Long eventId, @PathVariable Integer sessionId,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
		String searchString = eventRepository.findSessionSearchString(eventId, sessionId);
		if (searchString != null && !searchString.isEmpty()) {
			// Spring Social 1.1.2 doesn't support page number directly, use pageSize only
			return twitter.searchOperations().search(searchString, pageSize);
		}
		return null;
	}

	/**
	 * セッションについてのツイートを投稿します。
	 */
	@PostMapping(value = "/events/{eventId}/sessions/{sessionId}/tweets")
	public ResponseEntity<String> postSessionTweet(@PathVariable Long eventId, @PathVariable Integer sessionId,
			@RequestParam String status, Location currentLocation) {
		twitter.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * セッションツイートをリツイートします。
	 */
	@PostMapping(value = "/events/{eventId}/sessions/{sessionId}/retweet")
	public ResponseEntity<String> postSessionRetweet(@PathVariable Long eventId, @PathVariable Integer sessionId,
			@RequestParam Long tweetId) {
		twitter.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// for web browser (HTML) clients

	/**
	 * クライアントのWebブラウザでHTMLとして今後のイベントのリストをレンダリングします。
	 */
	@GetMapping(value = "/events", produces = "text/html")
	public String upcomingEventsView(Model model, DateTimeZone timeZone) {
		model.addAttribute(eventRepository.findUpcomingEvents(new DateTime(timeZone).getMillis()));
		return "events/list";
	}

	private void checkMediaType(@RequestHeader("Accept") String acceptHeader) {
		if (!acceptHeader.contains("application/json")) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Unsupported media type");
		}
	}

}