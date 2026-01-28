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
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.client.RequestMatchers.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreators;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class NFJSLoaderTest {

	// Test data constants
	private static final int SHOW_ID = 271;
	private static final String EXPECTED_EVENT_TITLE = "SpringOne 2GX";
	private static final String EXPECTED_TIME_ZONE = "America/Chicago";
	private static final String EXPECTED_START_TIME = "2011-10-25 00:00:00.0";
	private static final String EXPECTED_END_TIME = "2011-10-28 23:59:59.0";
	private static final String EXPECTED_SLUG = "S2GX";
	private static final String EXPECTED_VENUE_NAME = "Chicago Marriott Downtown Magnificent Mile";
	private static final String EXPECTED_VENUE_ADDRESS = "540 North Michigan Avenue null Chicago, IL 60611";
	private static final double EXPECTED_LATITUDE = 41.8920052;
	private static final double EXPECTED_LONGITUDE = -87.6247001;
	private static final String EXPECTED_LOCATION_HINT = "Chicago, IL";
	private static final String EXPECTED_LEADER_NAME = "Craig Walls";
	private static final String EXPECTED_LEADER_BIO = "Craig Walls is the Spring Social Project Lead.";
	private static final String EXPECTED_LEADER_URL = "http://blog.springsource.com/author/cwalls/";
	private static final String EXPECTED_TWITTER_USERNAME = "habuma";
	private static final String EXPECTED_TIME_SLOT_LABEL = "DINNER";
	private static final String EXPECTED_TIME_SLOT_START = "2011-10-26 18:30:00.0";
	private static final String EXPECTED_TIME_SLOT_END = "2011-10-26 19:30:00.0";

	private EmbeddedDatabase db;
	private JdbcTemplate jdbcTemplate;
	private EventLoaderRepository eventLoaderRepository;
	private NFJSLoader loader;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder()
				.member()
				.group()
				.activity()
				.invite()
				.venue()
				.event()
				.testData("com/springsource/greenhouse/events/load/JdbcEventLoaderRepositoryTest.sql").getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		eventLoaderRepository = new JdbcEventLoaderRepository(jdbcTemplate);
		loader = new NFJSLoader(eventLoaderRepository);
	}

	@After
	public void tearDown() {
		jdbcTemplate.update("drop all objects");
	}

	// ==================== Initial Load Tests ====================

	@Test
	public void testLoadEventData_ShouldLoadAllData_WhenFirstTimeLoad() {
		// Given
		setupMockRestServiceServer(loader, 1);

		// When
		loader.loadEventData(SHOW_ID);

		// Then
		assertRowCounts(1, 1, 85, 36, 112);
		assertEventData(EXPECTED_EVENT_TITLE, EXPECTED_TIME_ZONE, EXPECTED_START_TIME, EXPECTED_END_TIME,
				EXPECTED_SLUG);
		assertVenueData(EXPECTED_VENUE_NAME, EXPECTED_VENUE_ADDRESS, EXPECTED_LATITUDE, EXPECTED_LONGITUDE,
				EXPECTED_LOCATION_HINT, 1L);
		assertLeaderData(EXPECTED_LEADER_NAME, EXPECTED_LEADER_BIO, EXPECTED_LEADER_URL, EXPECTED_TWITTER_USERNAME);
		assertEventTimeSlotData(16L, 1L, EXPECTED_TIME_SLOT_LABEL, EXPECTED_TIME_SLOT_START, EXPECTED_TIME_SLOT_END);
	}

	// ==================== Update Tests ====================

	@Test
	public void testLoadEventData_ShouldNotDuplicateData_WhenSameDataLoadedTwice() {
		// Given
		setupMockRestServiceServer(loader, 3);

		// When - First load
		loader.loadEventData(SHOW_ID);

		// Then - First load results
		assertRowCounts(1, 1, 85, 36, 112);

		// When - Second load with same data
		loader.loadEventData(SHOW_ID);

		// Then - Should not duplicate data
		assertRowCounts(1, 1, 85, 36, 112);
		assertEventData(EXPECTED_EVENT_TITLE, EXPECTED_TIME_ZONE, EXPECTED_START_TIME, EXPECTED_END_TIME,
				EXPECTED_SLUG);
		assertVenueData(EXPECTED_VENUE_NAME, EXPECTED_VENUE_ADDRESS, EXPECTED_LATITUDE, EXPECTED_LONGITUDE,
				EXPECTED_LOCATION_HINT, 1L);
		assertLeaderData(EXPECTED_LEADER_NAME, EXPECTED_LEADER_BIO, EXPECTED_LEADER_URL, EXPECTED_TWITTER_USERNAME);
		assertEventTimeSlotData(16L, 1L, EXPECTED_TIME_SLOT_LABEL, EXPECTED_TIME_SLOT_START, EXPECTED_TIME_SLOT_END);
	}

	@Test
	public void testLoadEventData_ShouldUpdateData_WhenNewDataProvided() {
		// Given
		setupMockRestServiceServerWithUpdates(loader);

		// When - First load
		loader.loadEventData(SHOW_ID);

		// Then - First load results
		assertRowCounts(1, 1, 85, 36, 112);
		assertEventData(EXPECTED_EVENT_TITLE, EXPECTED_TIME_ZONE, EXPECTED_START_TIME, EXPECTED_END_TIME,
				EXPECTED_SLUG);
		assertVenueData(EXPECTED_VENUE_NAME, EXPECTED_VENUE_ADDRESS, EXPECTED_LATITUDE, EXPECTED_LONGITUDE,
				EXPECTED_LOCATION_HINT, 1L);
		assertLeaderData(EXPECTED_LEADER_NAME, EXPECTED_LEADER_BIO, EXPECTED_LEADER_URL, EXPECTED_TWITTER_USERNAME);
		assertEventTimeSlotData(16L, 1L, EXPECTED_TIME_SLOT_LABEL, EXPECTED_TIME_SLOT_START, EXPECTED_TIME_SLOT_END);

		// When - Second load with updated data
		loader.loadEventData(SHOW_ID);

		// Then - Should update with new data
		assertRowCounts(1, 1, 86, 37, 113);
		assertEventData("SpringOne/2GX", "America/Boise", "2012-06-09 00:00:00.0", "2012-06-12 23:59:59.0", "SGX");
		assertVenueData("Pocatello Convention Center", "1234 South Arizona Drive null Pocatello, ID 83201", 41.8920052,
				-87.6247001, "Pocatello, ID", 1L);
		assertLeaderData("Mr. Craig Walls",
				"Craig Walls is the Spring Social Project Lead and an avid collector of American Way magazines.",
				"http://blog.springsource.com/author/craigwalls/", "habumadude");
		assertEventTimeSlotData(16L, 1L, "SUPPER", "2012-06-10 18:30:00.0", "2012-06-10 19:30:00.0");
	}

	// ==================== Helper Methods ====================

	private void assertRowCounts(int eventRows, int venueRows, int leaderRows, int timeSlotRows, int sessionRows) {
		assertRowCount("Event", eventRows);
		assertRowCount("ExternalEvent", eventRows);
		assertRowCount("Venue", venueRows);
		assertRowCount("Leader", leaderRows);
		assertRowCount("ExternalLeader", leaderRows);
		assertRowCount("EventTimeSlot", timeSlotRows);
		assertRowCount("ExternalEventTimeSlot", timeSlotRows);
		assertRowCount("EventSession", sessionRows);
		assertRowCount("ExternalEventSession", sessionRows);
	}

	private void assertRowCount(String tableName, int rowCount) {
		assertEquals("Row count for " + tableName + " should match", rowCount,
				jdbcTemplate.queryForInt("select count(*) from " + tableName));
	}

	private void assertEventData(String title, String timeZone, String startTime, String endTime, String slug) {
		Map<String, Object> externalEventData = jdbcTemplate.queryForObject(
				"select event, sourceId, source from ExternalEvent where sourceId=? and source='NFJS'",
				new ColumnMapRowMapper(), SHOW_ID);
		Long eventId = (Long) externalEventData.get("event");
		assertEquals("Event ID should be 1", Long.valueOf(1), eventId);
		assertEquals("Source ID should match", Long.valueOf(SHOW_ID), externalEventData.get("sourceId"));
		assertEquals("Source should be NFJS", "NFJS", externalEventData.get("source"));

		Map<String, Object> eventData = jdbcTemplate.queryForObject(
				"select id,  title, timeZone, startTime, endTime, slug, description, memberGroup from Event where id=?",
				new ColumnMapRowMapper(), eventId);
		assertEquals("Event ID should match", eventId, eventData.get("id"));
		assertEquals("Event title should match", title, eventData.get("title"));
		assertEquals("Time zone should match", timeZone, eventData.get("timeZone"));
		assertEquals("Start time should match", startTime, eventData.get("startTime").toString());
		assertEquals("End time should match", endTime, eventData.get("endTime").toString());
		assertEquals("Slug should match", slug, eventData.get("slug"));
		assertNull("Description should be null", eventData.get("description"));
		assertEquals("Member group should be 1", 1L, eventData.get("memberGroup"));
	}

	private void assertVenueData(String name, String postalAddress, double latitude, double longitude,
			String locationHint, long createdBy) {
		Map<String, Object> eventVenueData = jdbcTemplate
				.queryForObject("select event, venue from EventVenue where event=?", new ColumnMapRowMapper(), 1);
		assertEquals("Event ID should be 1", Long.valueOf(1), eventVenueData.get("event"));
		Long venueId = (Long) eventVenueData.get("venue");
		assertEquals("Venue ID should be 1", Long.valueOf(1), venueId);

		Map<String, Object> venueData = jdbcTemplate.queryForObject(
				"select id, name, postalAddress, latitude, longitude, locationHint, createdBy from Venue where id=?",
				new ColumnMapRowMapper(), venueId);
		assertEquals("Venue ID should match", Long.valueOf(1), venueData.get("id"));
		assertEquals("Venue name should match", name, venueData.get("name"));
		assertEquals("Postal address should match", postalAddress, venueData.get("postalAddress"));
		assertEquals("Latitude should match", latitude, venueData.get("latitude"));
		assertEquals("Longitude should match", longitude, venueData.get("longitude"));
		assertEquals("Location hint should match", locationHint, venueData.get("locationHint"));
		assertEquals("Created by should match", createdBy, venueData.get("createdBy"));
	}

	private void assertLeaderData(String name, String bio, String personalUrl, String twitterUsername) {
		Map<String, Object> externalLeaderData = jdbcTemplate.queryForObject(
				"select leader, sourceId, source from ExternalLeader where source='NFJS' and sourceId=?",
				new ColumnMapRowMapper(), 38);
		Long leaderId = (Long) externalLeaderData.get("leader");
		assertEquals("Leader ID should be 15", Long.valueOf(15), leaderId);
		assertEquals("Source ID should be 38", 38L, externalLeaderData.get("sourceId"));
		assertEquals("Source should be NFJS", "NFJS", externalLeaderData.get("source"));

		Map<String, Object> leaderData = jdbcTemplate.queryForObject(
				"select id, name, company, title, location, bio, personalUrl, companyUrl, twitterUsername, member from Leader where id=?",
				new ColumnMapRowMapper(), leaderId);
		assertEquals("Leader ID should match", leaderId, leaderData.get("id"));
		assertEquals("Leader name should match", name, leaderData.get("name"));
		assertNull("Company should be null", leaderData.get("company"));
		assertNull("Title should be null", leaderData.get("title"));
		assertNull("Location should be null", leaderData.get("location"));
		assertEquals("Bio should match", bio, leaderData.get("bio").toString().trim());
		assertEquals("Personal URL should match", personalUrl, leaderData.get("personalUrl"));
		assertNull("Company URL should be null", leaderData.get("companyUrl"));
		assertEquals("Twitter username should match", twitterUsername, leaderData.get("twitterUsername"));
		assertNull("Member should be null", leaderData.get("member")); // TODO: Might want to figure out how to
																		// associate this with GH member table
	}

	private void assertEventTimeSlotData(long id, long eventId, String label, String startTime, String endTime) {
		Map<String, Object> externalEventTimeSlotData = jdbcTemplate.queryForObject(
				"select timeSlot, sourceId, source from ExternalEventTimeSlot where timeSlot=?",
				new ColumnMapRowMapper(), id);
		assertEquals("Time slot ID should match", id, externalEventTimeSlotData.get("timeSlot"));
		assertEquals("Source ID should be 6311", 6311L, externalEventTimeSlotData.get("sourceId"));
		assertEquals("Source should be NFJS", "NFJS", externalEventTimeSlotData.get("source"));

		Map<String, Object> eventTimeSlotData = jdbcTemplate.queryForObject(
				"select id, event, label, startTime, endTime from EventTimeSlot where id=?", new ColumnMapRowMapper(),
				16);
		assertEquals("Time slot ID should match", id, eventTimeSlotData.get("id"));
		assertEquals("Event ID should match", eventId, eventTimeSlotData.get("event"));
		assertEquals("Label should match", label, eventTimeSlotData.get("label"));
		assertEquals("Start time should match", startTime, eventTimeSlotData.get("startTime").toString());
		assertEquals("End time should match", endTime, eventTimeSlotData.get("endTime").toString());
	}

	private MockRestServiceServer setupMockRestServiceServer(NFJSLoader loader, int numberOfLoads) {
		MockRestServiceServer mockServer = MockRestServiceServer.createServer(loader.getRestTemplate());
		for (int i = 0; i < numberOfLoads; i++) {
			mockServer.expect(
					requestTo("https://springone2gx.com/m/data/show_short.json?showId=" + SHOW_ID))
					.andExpect(method(GET))
					.andRespond(ResponseCreators.withSuccess(
							new ClassPathResource("show_short.json", NFJSLoaderTest.class), APPLICATION_JSON));
		}
		return mockServer;
	}

	private MockRestServiceServer setupMockRestServiceServerWithUpdates(NFJSLoader loader) {
		MockRestServiceServer mockServer = MockRestServiceServer.createServer(loader.getRestTemplate());
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_short.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_short.json", NFJSLoaderTest.class),
						APPLICATION_JSON));
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_short.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(
						new ClassPathResource("show_short_updated.json", NFJSLoaderTest.class), APPLICATION_JSON));
		return mockServer;
	}
}
