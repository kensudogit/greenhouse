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
	 * Writes a list of events to the response body in JSON format.
	 * Matches only 'GET /events' requests and returns JSON content.
	 * Otherwise sends 404.
	 * TODO: Send 406 if unsupported representation (e.g., XML) is requested.
	 * See SPR-7353.
	 */
	@GetMapping(value = "/events", produces = "application/json")
	public List<Event> upcomingEvents(
			@RequestHeader("Accept") String acceptHeader,
			@RequestParam(value = "after", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Long afterMillis) {
		checkMediaType(acceptHeader);
		return eventRepository.findUpcomingEvents(afterMillis);
	}

	/**
	 * Writes the event's favorite list to the response body.
	 */
	@GetMapping(value = "/events/{eventId}/favorites", produces = "application/json")
	public List<EventSession> favorites(@PathVariable Long eventId, Account account) {
		return eventRepository.findEventFavorites(eventId, account.getId());
	}

	/**
	 * Writes a page of tweet search results for an event to the response body.
	 * Page number and size can be specified by the client. If not specified, returns the first page with 10 results by default.
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
	 * Posts a tweet about an event to Twitter.
	 * Returns OK status if successful.
	 */
	@PostMapping(value = "/events/{eventId}/tweets")
	public ResponseEntity<String> postTweet(@PathVariable Long eventId, @RequestParam String status,
			Location currentLocation) {
		twitter.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Retweets another event tweet.
	 */
	@PostMapping(value = "/events/{eventId}/retweet")
	public ResponseEntity<String> postRetweet(@PathVariable Long eventId, @RequestParam Long tweetId) {
		twitter.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Writes the list of attendee's favorite sessions to the response body.
	 */
	@GetMapping(value = "/events/{eventId}/sessions/favorites", produces = "application/json")
	public List<EventSession> favoriteSessions(@PathVariable Long eventId, Account account) {
		return eventRepository.findAttendeeFavorites(eventId, account.getId());
	}

	/**
	 * Writes sessions for the specified day to the response body.
	 */
	@GetMapping(value = "/events/{eventId}/sessions/{day}", produces = "application/json")
	public List<EventSession> sessionsOnDay(@PathVariable Long eventId,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate day, Account account) {
		return eventRepository.findSessionsOnDay(eventId, day, account.getId());
	}

	/**
	 * Toggles a session as an attendee's favorite.
	 * Writes the new favorite status to the response body.
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
	 * Writes a page of tweet search results for a session to the response body.
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
	 * Posts a tweet about a session.
	 */
	@PostMapping(value = "/events/{eventId}/sessions/{sessionId}/tweets")
	public ResponseEntity<String> postSessionTweet(@PathVariable Long eventId, @PathVariable Integer sessionId,
			@RequestParam String status, Location currentLocation) {
		twitter.timelineOperations().updateStatus(status);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Retweets a session tweet.
	 */
	@PostMapping(value = "/events/{eventId}/sessions/{sessionId}/retweet")
	public ResponseEntity<String> postSessionRetweet(@PathVariable Long eventId, @PathVariable Integer sessionId,
			@RequestParam Long tweetId) {
		twitter.timelineOperations().retweet(tweetId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// for web browser (HTML) clients

	/**
	 * Renders a list of upcoming events as HTML in the client's web browser.
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