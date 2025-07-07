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

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.joda.time.tz.CachedDateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.events.Event;
import com.springsource.greenhouse.events.EventRepository;
import com.springsource.greenhouse.events.JdbcEventRepository;
import com.springsource.greenhouse.events.Venue;

public class JdbcEventLoaderRepositoryTest {

	private EmbeddedDatabase db;
	private JdbcTemplate jdbcTemplate;
	private EventRepository eventRepository;
	private EventLoaderRepository eventLoaderRepository;

	// Test data constants
	private static final EventData TEST_EVENT_DATA = new EventData(1, "Test Event", "Test Event Description", "test",
			"2012-10-15T00:00:00", "2012-10-18T23:59:59", "America/New_York", "NFJS", 297);
	private static final VenueData TEST_VENUE_DATA = new VenueData("Some Fancy Hotel",
			"1234 North Street, Chicago, IL 60605", 41.89001, -87.677765, "It's in Illinois");
	private static final LeaderData TEST_LEADER_DATA = new LeaderData("Craig Walls",
			"Craig is the Spring Social project lead", "http://www.habuma.com", "habuma", "NFJS", 1234);
	private static final TimeSlotData TEST_TIME_SLOT_DATA = new TimeSlotData(1L, "Time Slot 1", "2012-10-15T00:00:00",
			"2012-10-15T01:30:00", "NFJS", 6296);
	private static final EventSessionData TEST_EVENT_SESSION_DATA = new EventSessionData(1L, 1, "What's new in Spring",
			"Come find out what's new in Spring", "#newspring", 1L, 1L, "NFJS", 24409L, Collections.emptyList());

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().group().activity().invite().venue().event()
				.testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		eventLoaderRepository = new JdbcEventLoaderRepository(jdbcTemplate);
		eventRepository = new JdbcEventRepository(jdbcTemplate);
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	// ==================== Event Loading Tests ====================

	@Test
	public void testLoadEvent_ShouldCreateNewEvent() {
		// When
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);

		// Then
		assertEquals(1L, eventId);
		Event event = eventRepository.findEventBySlug("s2gx", 2012, 10, "test");
		assertEventData(event, TEST_EVENT_DATA);
		assertVenueData(event, TEST_VENUE_DATA);
	}

	@Test
	public void testLoadEvent_ShouldUpdateExistingEvent() {
		// Given - Create initial event
		long initialEventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);
		assertEquals(1L, initialEventId);

		// When - Update with new data
		EventData updatedEventData = new EventData(1, "Updated Event", "Updated Description", "test",
				"2012-10-15T00:00:00", "2012-10-18T23:59:59", "America/New_York", "NFJS", 297);
		VenueData updatedVenueData = new VenueData("Some Conference Hall", "1234 North Street, Chicago, IL 60605",
				41.89001, -87.677765, "It's in Illinois");
		long updatedEventId = eventLoaderRepository.loadEvent(updatedEventData, updatedVenueData);

		// Then
		assertEquals(1L, updatedEventId);
		Event updatedEvent = eventRepository.findEventBySlug("s2gx", 2012, 10, "test");
		assertEventData(updatedEvent, updatedEventData);
		assertVenueData(updatedEvent, updatedVenueData);
	}

	@Test
	public void testLoadEvent_ShouldHandleDifferentEventIds() {
		// When - Load events with different IDs
		EventData eventData1 = new EventData(1, "Event 1", "Description 1", "event1", "2012-10-15T00:00:00",
				"2012-10-18T23:59:59", "America/New_York", "NFJS", 297);
		EventData eventData2 = new EventData(2, "Event 2", "Description 2", "event2", "2012-10-20T00:00:00",
				"2012-10-23T23:59:59", "America/Chicago", "NFJS", 298);

		long eventId1 = eventLoaderRepository.loadEvent(eventData1, TEST_VENUE_DATA);
		long eventId2 = eventLoaderRepository.loadEvent(eventData2, TEST_VENUE_DATA);

		// Then
		assertEquals(1L, eventId1);
		assertEquals(2L, eventId2);
	}

	// ==================== Leader Loading Tests ====================

	@Test
	public void testLoadLeader_ShouldCreateNewLeader() throws SQLException {
		// When
		long leaderId = eventLoaderRepository.loadLeader(TEST_LEADER_DATA);

		// Then
		assertEquals(1L, leaderId);
		assertLeaderDataInDatabase(leaderId, TEST_LEADER_DATA);
		assertExternalLeaderDataInDatabase(leaderId, TEST_LEADER_DATA);
	}

	@Test
	public void testLoadLeader_ShouldUpdateExistingLeader() throws SQLException {
		// Given - Create initial leader
		long initialLeaderId = eventLoaderRepository.loadLeader(TEST_LEADER_DATA);
		assertEquals(1L, initialLeaderId);

		// When - Update with new data
		LeaderData updatedLeaderData = new LeaderData("Craig Walls", "Updated bio", "http://www.updated.com",
				"updated_twitter", "NFJS", 1234);
		long updatedLeaderId = eventLoaderRepository.loadLeader(updatedLeaderData);

		// Then
		assertEquals(1L, updatedLeaderId);
		assertLeaderDataInDatabase(updatedLeaderId, updatedLeaderData);
		assertExternalLeaderDataInDatabase(updatedLeaderId, updatedLeaderData);
	}

	@Test
	public void testLoadLeader_ShouldHandleMultipleLeaders() throws SQLException {
		// When - Load multiple leaders
		LeaderData leader1 = new LeaderData("Craig Walls", "Bio 1", "http://www.habuma.com", "habuma", "NFJS", 1234);
		LeaderData leader2 = new LeaderData("John Doe", "Bio 2", "http://www.johndoe.com", "johndoe", "NFJS", 5678);

		long leaderId1 = eventLoaderRepository.loadLeader(leader1);
		long leaderId2 = eventLoaderRepository.loadLeader(leader2);

		// Then
		assertEquals(1L, leaderId1);
		assertEquals(2L, leaderId2);
		assertLeaderDataInDatabase(leaderId1, leader1);
		assertLeaderDataInDatabase(leaderId2, leader2);
	}

	// ==================== TimeSlot Loading Tests ====================

	@Test
	public void testLoadTimeSlot_ShouldCreateNewTimeSlot() throws SQLException {
		// Given - Create event first
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);

		// When
		long timeSlotId = eventLoaderRepository.loadTimeSlot(TEST_TIME_SLOT_DATA);

		// Then
		assertEquals(1L, timeSlotId);
		assertTimeSlotDataInDatabase(timeSlotId, TEST_TIME_SLOT_DATA);
	}

	@Test
	public void testLoadTimeSlot_ShouldUpdateExistingTimeSlot() throws SQLException {
		// Given - Create event and initial time slot
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);
		long initialTimeSlotId = eventLoaderRepository.loadTimeSlot(TEST_TIME_SLOT_DATA);
		assertEquals(1L, initialTimeSlotId);

		// When - Update with new data
		TimeSlotData updatedTimeSlotData = new TimeSlotData(eventId, "Time Slot One", "2012-10-15T01:00:00",
				"2012-10-15T02:30:00", "NFJS", 6296);
		long updatedTimeSlotId = eventLoaderRepository.loadTimeSlot(updatedTimeSlotData);

		// Then
		assertEquals(1L, updatedTimeSlotId);
		assertTimeSlotDataInDatabase(updatedTimeSlotId, updatedTimeSlotData);
	}

	@Test
	public void testLoadTimeSlot_ShouldHandleMultipleTimeSlots() throws SQLException {
		// Given - Create event
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);

		// When - Load multiple time slots
		TimeSlotData timeSlot1 = new TimeSlotData(eventId, "Morning Session", "2012-10-15T09:00:00",
				"2012-10-15T10:30:00", "NFJS", 6296);
		TimeSlotData timeSlot2 = new TimeSlotData(eventId, "Afternoon Session", "2012-10-15T14:00:00",
				"2012-10-15T15:30:00", "NFJS", 6297);

		long timeSlotId1 = eventLoaderRepository.loadTimeSlot(timeSlot1);
		long timeSlotId2 = eventLoaderRepository.loadTimeSlot(timeSlot2);

		// Then
		assertEquals(1L, timeSlotId1);
		assertEquals(2L, timeSlotId2);
		assertTimeSlotDataInDatabase(timeSlotId1, timeSlot1);
		assertTimeSlotDataInDatabase(timeSlotId2, timeSlot2);
	}

	// ==================== EventSession Loading Tests ====================

	@Test
	public void testLoadEventSession_ShouldCreateNewEventSession() throws SQLException {
		// Given - Create event and time slot
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);
		long timeSlotId = eventLoaderRepository.loadTimeSlot(TEST_TIME_SLOT_DATA);

		// When
		eventLoaderRepository.loadEventSession(TEST_EVENT_SESSION_DATA);

		// Then
		assertEventSessionDataInDatabase(eventId, TEST_EVENT_SESSION_DATA);
	}

	@Test
	public void testLoadEventSession_ShouldUpdateExistingEventSession() throws SQLException {
		// Given - Create event, time slot, and initial session
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);
		long timeSlotId = eventLoaderRepository.loadTimeSlot(TEST_TIME_SLOT_DATA);
		eventLoaderRepository.loadEventSession(TEST_EVENT_SESSION_DATA);

		// When - Update with new data
		EventSessionData updatedSessionData = new EventSessionData(eventId, 1, "What's new in Spring?",
				"Juergen gives the dish on the latest in Spring", "#spring3", 1L, timeSlotId, "NFJS", 24409L,
				Collections.emptyList());
		eventLoaderRepository.loadEventSession(updatedSessionData);

		// Then
		assertEventSessionDataInDatabase(eventId, updatedSessionData);
	}

	@Test
	public void testLoadEventSession_ShouldHandleMultipleSessions() throws SQLException {
		// Given - Create event and time slot
		long eventId = eventLoaderRepository.loadEvent(TEST_EVENT_DATA, TEST_VENUE_DATA);
		long timeSlotId = eventLoaderRepository.loadTimeSlot(TEST_TIME_SLOT_DATA);

		// When - Load multiple sessions
		EventSessionData session1 = new EventSessionData(eventId, 1, "Session 1", "Description 1", "#session1", 1L,
				timeSlotId, "NFJS", 24409L, Collections.emptyList());
		EventSessionData session2 = new EventSessionData(eventId, 2, "Session 2", "Description 2", "#session2", 1L,
				timeSlotId, "NFJS", 24410L, Collections.emptyList());

		eventLoaderRepository.loadEventSession(session1);
		eventLoaderRepository.loadEventSession(session2);

		// Then
		assertEventSessionDataInDatabase(eventId, session1);
		assertEventSessionDataInDatabase(eventId, session2);
	}

	// ==================== Helper Methods ====================

	private void assertEventData(Event event, EventData expectedData) {
		assertEquals(expectedData.getId().longValue(), event.getId().longValue());
		assertEquals(expectedData.getTitle(), event.getTitle());
		assertEquals(expectedData.getSlug(), event.getSlug());
		assertEquals(expectedData.getDescription(), event.getDescription());
		assertEquals(CachedDateTimeZone.forID(expectedData.getTimeZone()), event.getTimeZone());
	}

	private void assertVenueData(Event event, VenueData expectedData) {
		Set<Venue> venues = event.getVenues();
		assertEquals(1, venues.size());
		Venue venue = new ArrayList<Venue>(venues).get(0);
		assertEquals(expectedData.getName(), venue.getName());
		assertEquals(expectedData.getPostalAddress(), venue.getPostalAddress());
		assertEquals(expectedData.getLatitude(), venue.getLocation().getLatitude().doubleValue(), .000000000001);
		assertEquals(expectedData.getLongitude(), venue.getLocation().getLongitude().doubleValue(), .000000000001);
		assertEquals(expectedData.getLocationHint(), venue.getLocationHint());
	}

	private void assertLeaderDataInDatabase(long leaderId, LeaderData expectedData) throws SQLException {
		jdbcTemplate.queryForObject("select id, name, bio, personalUrl, twitterUsername from Leader where id=?",
				new RowMapper<ResultSet>() {
					public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
						assertEquals(leaderId, rs.getLong("id"));
						assertEquals(expectedData.getName(), rs.getString("name"));
						assertEquals(expectedData.getBio(), rs.getString("bio"));
						assertEquals(expectedData.getPersonalUrl(), rs.getString("personalUrl"));
						assertEquals(expectedData.getTwitterUsername(), rs.getString("twitterUsername"));
						return null;
					}
				}, leaderId);
	}

	private void assertExternalLeaderDataInDatabase(long leaderId, LeaderData expectedData) throws SQLException {
		jdbcTemplate.queryForObject("select leader, sourceId, source from ExternalLeader where leader=?",
				new RowMapper<ResultSet>() {
					public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
						assertEquals(leaderId, rs.getLong("leader"));
						assertEquals(expectedData.getSourceId().longValue(), rs.getLong("sourceId"));
						assertEquals(expectedData.getSource(), rs.getString("source"));
						return null;
					}
				}, leaderId);
	}

	private void assertTimeSlotDataInDatabase(long timeSlotId, TimeSlotData expectedData) throws SQLException {
		jdbcTemplate.queryForObject("select id, event, label, startTime, endTime from EventTimeSlot where id=?",
				new RowMapper<ResultSet>() {
					public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
						assertEquals(timeSlotId, rs.getLong("id"));
						assertEquals(expectedData.getEventId(), rs.getLong("event"));
						assertEquals(expectedData.getLabel(), rs.getString("label"));
						// Note: Timestamp assertions are commented out in original code
						return null;
					}
				}, timeSlotId);
	}

	private void assertEventSessionDataInDatabase(long eventId, EventSessionData expectedData) throws SQLException {
		jdbcTemplate.queryForObject(
				"select event, id, title, description, hashtag, venue, timeslot from EventSession where event=? and id=?",
				new RowMapper<ResultSet>() {
					public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
						assertEquals(eventId, rs.getLong("event"));
						assertEquals(expectedData.getSessionId(), rs.getLong("id"));
						assertEquals(expectedData.getTitle(), rs.getString("title"));
						assertEquals(expectedData.getDescription(), rs.getString("description"));
						assertEquals(expectedData.getHashtag(), rs.getString("hashtag"));
						assertEquals(expectedData.getVenueId(), rs.getLong("venue"));
						assertEquals(expectedData.getTimeSlotId(), rs.getLong("timeslot"));
						return null;
					}
				}, eventId, expectedData.getSessionId());
	}
}
