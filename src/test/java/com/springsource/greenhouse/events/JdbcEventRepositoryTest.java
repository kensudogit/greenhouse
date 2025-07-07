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
package com.springsource.greenhouse.events;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.utils.Location;

public class JdbcEventRepositoryTest {

	private EmbeddedDatabase db;
	private JdbcTemplate jdbcTemplate;
	private EventRepository eventRepository;

	// Test data constants
	private static final String TEST_GROUP_ID = "s2gx";
	private static final int TEST_YEAR = 2010;
	private static final int TEST_MONTH = 10;
	private static final String TEST_SLUG = "chicago";
	private static final long TEST_EVENT_ID = 1L;
	private static final long TEST_VENUE_ID = 1L;
	private static final long TEST_MEMBER_ID = 1L;
	private static final long TEST_ATTENDEE_ID = 2L;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().group().activity().invite().venue().event()
				.testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		eventRepository = new JdbcEventRepository(jdbcTemplate);
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	// ==================== Event Finding Tests ====================

	@Test
	public void testFindEventBySlug_ShouldReturnEvent_WhenValidSlugProvided() {
		// When
		Event event = eventRepository.findEventBySlug(TEST_GROUP_ID, TEST_YEAR, TEST_MONTH, TEST_SLUG);

		// Then
		assertNotNull("Event should not be null", event);
		assertEquals("Event title should match", "SpringOne2gx", event.getTitle());
		assertEquals("Event description should match",
				"SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.",
				event.getDescription());
		assertEventGroup(event);
		assertEventVenue(event);
	}

	@Test
	public void testFindEventSearchString_ShouldReturnHashtag_WhenEventExists() {
		// When
		String searchString = eventRepository.findEventSearchString(TEST_EVENT_ID);

		// Then
		assertEquals("Search string should be hashtag format", "#s2gx", searchString);
	}

	@Test
	public void testFindEventSessionSearchString_ShouldReturnCombinedHashtags_WhenSessionExists() {
		// When
		String searchString = eventRepository.findSessionSearchString(TEST_EVENT_ID, 1);

		// Then
		assertEquals("Session search string should combine event and session hashtags", "#s2gx #mvc", searchString);
	}

	// ==================== Session Finding Tests ====================

	@Test
	public void testFindSessionsOnDay_ShouldReturnMultipleSessions_WhenSessionsExist() {
		// Given
		LocalDate sessionDate = new LocalDate(2010, 10, 21);

		// When
		List<EventSession> sessions = eventRepository.findSessionsOnDay(TEST_EVENT_ID, sessionDate, TEST_VENUE_ID);

		// Then
		assertEquals("Should return 2 sessions", 2, sessions.size());
		assertSocialSession(sessions.get(0), true);
		assertMobileSession(sessions.get(1), true);
	}

	@Test
	public void testFindSessionsOnDay_ShouldReturnSingleSession_WhenOnlyOneSessionExists() {
		// Given
		LocalDate sessionDate = new LocalDate(2010, 10, 19);

		// When
		List<EventSession> sessions = eventRepository.findSessionsOnDay(TEST_EVENT_ID, sessionDate, TEST_VENUE_ID);

		// Then
		assertEquals("Should return 1 session", 1, sessions.size());
	}

	// ==================== Favorite Tests ====================

	@Test
	public void testFindEventFavorites_ShouldReturnFavorites_WhenFavoritesExist() {
		// When
		List<EventSession> favorites = eventRepository.findEventFavorites(TEST_EVENT_ID, TEST_ATTENDEE_ID);

		// Then
		assertEquals("Should return 2 favorites", 2, favorites.size());
		assertSocialSession(favorites.get(0), false);
		assertMobileSession(favorites.get(1), false);
	}

	@Test
	public void testFindAttendeeFavorites_ShouldReturnFavorites_WhenAttendeeHasFavorites() {
		// When
		List<EventSession> favorites = eventRepository.findAttendeeFavorites(TEST_EVENT_ID, TEST_MEMBER_ID);

		// Then
		assertEquals("Should return 2 favorites", 2, favorites.size());
		assertSocialSession(favorites.get(0), true);
		assertMobileSession(favorites.get(1), true);
	}

	@Test
	public void testToggleFavorite_ShouldToggleFavoriteStatus() {
		// Given
		long eventId = 1L;
		int sessionId = 3;
		long memberId = 1L;

		// When - First toggle (should add favorite)
		boolean firstResult = eventRepository.toggleFavorite(eventId, sessionId, memberId);

		// Then
		assertFalse("First toggle should return false (favorite added)", firstResult);

		// When - Second toggle (should remove favorite)
		boolean secondResult = eventRepository.toggleFavorite(eventId, sessionId, memberId);

		// Then
		assertTrue("Second toggle should return true (favorite removed)", secondResult);
	}

	// ==================== Rating Tests ====================

	@Test
	public void testRate_ShouldCalculateAverageRating_WhenMultipleRatingsProvided() throws RatingPeriodClosedException {
		// Given
		long eventId = 2L;
		int sessionId = 6;
		Rating rating1 = new Rating((short) 5, "Rocked");
		Rating rating2 = new Rating((short) 4, "Rocked");
		Rating rating3 = new Rating((short) 2, "Rocked");

		// When
		eventRepository.rate(eventId, sessionId, 1L, rating1);
		eventRepository.rate(eventId, sessionId, 2L, rating2);
		Float averageRating = eventRepository.rate(eventId, sessionId, 3L, rating3);

		// Then
		assertEquals("Average rating should be calculated correctly", new Float(3.5), averageRating);
	}

	// ==================== Helper Methods ====================

	private void assertEventGroup(Event event) {
		assertEquals("Group ID should match", TEST_GROUP_ID, event.getGroup().getId());
		assertEquals("Group label should match", "SpringOne2gx", event.getGroup().getLabel());
	}

	private void assertEventVenue(Event event) {
		assertEquals("Venue name should match", "Westin Lombard Yorktown Center",
				event.getVenues().iterator().next().getName());
		assertEquals("Venue address should match", "70 Yorktown Center Lombard, IL 60148",
				event.getVenues().iterator().next().getPostalAddress());
		assertEquals("Venue location should match", new Location(41.8751108905486, -88.0184300761646),
				event.getVenues().iterator().next().getLocation());
		assertEquals("Venue location hint should match", "adjacent to Shopping Center",
				event.getVenues().iterator().next().getLocationHint());
	}

	private void assertMobileSession(EventSession session, boolean favorite) {
		assertEquals("Mobile session title should match", "Choices in Mobile Application Development",
				session.getTitle());
		assertEquals("Mobile session should have 2 leaders", 2, session.getLeaders().size());
		assertEquals("First leader should be Roy Clarkson", "Roy Clarkson", session.getLeaders().get(0).getName());
		assertEquals("Second leader should be Keith Donald", "Keith Donald", session.getLeaders().get(1).getName());
		assertEquals("Mobile session rating should be 0", new Float(0), session.getRating());
		assertEquals("Mobile session favorite status should match", favorite, session.isFavorite());
		assertEquals("Mobile session room should be Junior Ballroom B", "Junior Ballroom B",
				session.getRoom().getLabel());
	}

	private void assertSocialSession(EventSession session, boolean favorite) {
		assertEquals("Social session title should match", "Developing Social-Ready Web Applications",
				session.getTitle());
		assertEquals("Social session should have 1 leader", 1, session.getLeaders().size());
		assertEquals("Social session leader should be Craig Walls", "Craig Walls",
				session.getLeaders().get(0).getName());
		assertEquals("Social session favorite status should match", favorite, session.isFavorite());
		assertEquals("Social session rating should be 0", new Float(0), session.getRating());
		assertEquals("Social session room should be Junior Ballroom B", "Junior Ballroom B",
				session.getRoom().getLabel());
	}
}