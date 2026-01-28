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

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.springsource.greenhouse.utils.ResourceReference;

/**
 * Test class for {@link Event}.
 * 
 * @author Test Author
 */
public class EventTest {

    // Test data constants
    private static final Long TEST_ID = 1L;
    private static final String TEST_TITLE = "Spring Framework Conference";
    private static final String TEST_SLUG = "spring-conference";
    private static final String TEST_DESCRIPTION = "A comprehensive conference about Spring Framework";
    private static final String TEST_HASHTAG = "#SpringConf";
    private static final DateTimeZone TEST_TIMEZONE = DateTimeZone.UTC;
    private static final DateTime TEST_START_TIME = new DateTime(2024, 6, 15, 9, 0, 0, TEST_TIMEZONE);
    private static final DateTime TEST_END_TIME = new DateTime(2024, 6, 15, 17, 0, 0, TEST_TIMEZONE);

    // ==================== Builder Tests ====================

    @Test
    public void testBuilder_ShouldCreateEvent_WhenAllFieldsProvided() {
        // Given
        ResourceReference<String> group = new ResourceReference<String>("spring-group", "Spring Group");

        // When
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .timeZone(TEST_TIMEZONE)
                .startTime(TEST_START_TIME)
                .endTime(TEST_END_TIME)
                .slug(TEST_SLUG)
                .description(TEST_DESCRIPTION)
                .hashtag(TEST_HASHTAG)
                .group(group)
                .build();

        // Then
        assertNotNull("Event should not be null", event);
        assertEquals("ID should match", TEST_ID, event.getId());
        assertEquals("Title should match", TEST_TITLE, event.getTitle());
        assertEquals("TimeZone should match", TEST_TIMEZONE, event.getTimeZone());
        assertEquals("Start time should match", TEST_START_TIME, event.getStartTime());
        assertEquals("End time should match", TEST_END_TIME, event.getEndTime());
        assertEquals("Slug should match", TEST_SLUG, event.getSlug());
        assertEquals("Description should match", TEST_DESCRIPTION, event.getDescription());
        assertEquals("Hashtag should match", TEST_HASHTAG, event.getHashtag());
        assertEquals("Group should match", group, event.getGroup());
    }

    @Test
    public void testBuilder_ShouldCreateEvent_WhenMinimalFieldsProvided() {
        // When
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();

        // Then
        assertNotNull("Event should not be null", event);
        assertEquals("ID should match", TEST_ID, event.getId());
        assertEquals("Title should match", TEST_TITLE, event.getTitle());
        assertNull("TimeZone should be null", event.getTimeZone());
        assertNull("Start time should be null", event.getStartTime());
        assertNull("End time should be null", event.getEndTime());
        assertNull("Slug should be null", event.getSlug());
        assertNull("Description should be null", event.getDescription());
        assertNull("Hashtag should be null", event.getHashtag());
        assertNull("Group should be null", event.getGroup());
    }

    @Test
    public void testBuilder_ShouldAllowFluentChaining_WhenMultipleMethodsCalled() {
        // When
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .slug(TEST_SLUG)
                .description(TEST_DESCRIPTION)
                .hashtag(TEST_HASHTAG)
                .build();

        // Then
        assertNotNull("Event should not be null", event);
        assertEquals("ID should match", TEST_ID, event.getId());
        assertEquals("Title should match", TEST_TITLE, event.getTitle());
        assertEquals("Slug should match", TEST_SLUG, event.getSlug());
        assertEquals("Description should match", TEST_DESCRIPTION, event.getDescription());
        assertEquals("Hashtag should match", TEST_HASHTAG, event.getHashtag());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetters_ShouldReturnCorrectValues_WhenEventCreated() {
        // Given
        ResourceReference<String> group = new ResourceReference<String>("spring-group", "Spring Group");
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .timeZone(TEST_TIMEZONE)
                .startTime(TEST_START_TIME)
                .endTime(TEST_END_TIME)
                .slug(TEST_SLUG)
                .description(TEST_DESCRIPTION)
                .hashtag(TEST_HASHTAG)
                .group(group)
                .build();

        // When & Then
        assertEquals("ID should match", TEST_ID, event.getId());
        assertEquals("Title should match", TEST_TITLE, event.getTitle());
        assertEquals("TimeZone should match", TEST_TIMEZONE, event.getTimeZone());
        assertEquals("Start time should match", TEST_START_TIME, event.getStartTime());
        assertEquals("End time should match", TEST_END_TIME, event.getEndTime());
        assertEquals("Slug should match", TEST_SLUG, event.getSlug());
        assertEquals("Description should match", TEST_DESCRIPTION, event.getDescription());
        assertEquals("Hashtag should match", TEST_HASHTAG, event.getHashtag());
        assertEquals("Group should match", group, event.getGroup());
    }

    // ==================== Venue Tests ====================

    @Test
    public void testAddVenue_ShouldAddVenue_WhenVenueProvided() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        Venue venue = new Venue.Builder()
                .id(1L)
                .name("Convention Center")
                .build();

        // When
        event.addVenue(venue);

        // Then
        Set<Venue> venues = event.getVenues();
        assertNotNull("Venues should not be null", venues);
        assertEquals("Should have one venue", 1, venues.size());
        assertTrue("Should contain the venue", venues.contains(venue));
    }

    @Test
    public void testAddVenue_ShouldAddMultipleVenues_WhenMultipleVenuesProvided() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        Venue venue1 = new Venue.Builder()
                .id(1L)
                .name("Convention Center")
                .build();
        Venue venue2 = new Venue.Builder()
                .id(2L)
                .name("Hotel Ballroom")
                .build();

        // When
        event.addVenue(venue1);
        event.addVenue(venue2);

        // Then
        Set<Venue> venues = event.getVenues();
        assertNotNull("Venues should not be null", venues);
        assertEquals("Should have two venues", 2, venues.size());
        assertTrue("Should contain venue1", venues.contains(venue1));
        assertTrue("Should contain venue2", venues.contains(venue2));
    }

    @Test
    public void testGetVenues_ShouldReturnUnmodifiableSet_WhenVenuesAdded() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        Venue venue = new Venue.Builder()
                .id(1L)
                .name("Convention Center")
                .build();
        event.addVenue(venue);

        // When
        Set<Venue> venues = event.getVenues();

        // Then
        try {
            venues.add(new Venue.Builder().id(2L).name("Another Venue").build());
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    // ==================== Compatibility Methods Tests ====================

    @Test
    public void testGetLocation_ShouldReturnFirstVenueName_WhenVenuesExist() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        Venue venue1 = new Venue.Builder()
                .id(1L)
                .name("Convention Center")
                .build();
        Venue venue2 = new Venue.Builder()
                .id(2L)
                .name("Hotel Ballroom")
                .build();
        event.addVenue(venue1);
        event.addVenue(venue2);

        // When
        String location = event.getLocation();

        // Then
        assertEquals("Should return first venue name", "Convention Center", location);
    }

    @Test
    public void testGetGroupName_ShouldReturnGroupLabel_WhenGroupProvided() {
        // Given
        ResourceReference<String> group = new ResourceReference<String>("spring-group", "Spring Group");
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .group(group)
                .build();

        // When
        String groupName = event.getGroupName();

        // Then
        assertEquals("Should return group label", "Spring Group", groupName);
    }

    @Test
    public void testGetGroupSlug_ShouldReturnGroupId_WhenGroupProvided() {
        // Given
        ResourceReference<String> group = new ResourceReference<String>("spring-group", "Spring Group");
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .group(group)
                .build();

        // When
        String groupSlug = event.getGroupSlug();

        // Then
        assertEquals("Should return group ID", "spring-group", groupSlug);
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEvent_ShouldHandleNullValues_WhenNullValuesProvided() {
        // When
        Event event = new Event.Builder()
                .id(null)
                .title(null)
                .timeZone(null)
                .startTime(null)
                .endTime(null)
                .slug(null)
                .description(null)
                .hashtag(null)
                .group(null)
                .build();

        // Then
        assertNotNull("Event should not be null", event);
        assertNull("ID should be null", event.getId());
        assertNull("Title should be null", event.getTitle());
        assertNull("TimeZone should be null", event.getTimeZone());
        assertNull("Start time should be null", event.getStartTime());
        assertNull("End time should be null", event.getEndTime());
        assertNull("Slug should be null", event.getSlug());
        assertNull("Description should be null", event.getDescription());
        assertNull("Hashtag should be null", event.getHashtag());
        assertNull("Group should be null", event.getGroup());
    }

    @Test
    public void testAddVenue_ShouldHandleDuplicateVenues_WhenSameVenueAddedMultipleTimes() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        Venue venue = new Venue.Builder()
                .id(1L)
                .name("Convention Center")
                .build();

        // When
        event.addVenue(venue);
        event.addVenue(venue); // Add same venue again

        // Then
        Set<Venue> venues = event.getVenues();
        assertEquals("Should have only one venue due to Set behavior", 1, venues.size());
    }

    @Test
    public void testGetVenues_ShouldReturnEmptySet_WhenNoVenuesAdded() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();

        // When
        Set<Venue> venues = event.getVenues();

        // Then
        assertNotNull("Venues should not be null", venues);
        assertTrue("Should be empty", venues.isEmpty());
    }

    // ==================== Time Zone Tests ====================

    @Test
    public void testEvent_ShouldHandleDifferentTimeZones_WhenDifferentTimeZonesProvided() {
        // Given
        DateTimeZone tokyoTimeZone = DateTimeZone.forID("Asia/Tokyo");
        DateTimeZone newYorkTimeZone = DateTimeZone.forID("America/New_York");

        // When
        Event tokyoEvent = new Event.Builder()
                .id(1L)
                .title("Tokyo Event")
                .timeZone(tokyoTimeZone)
                .build();

        Event newYorkEvent = new Event.Builder()
                .id(2L)
                .title("New York Event")
                .timeZone(newYorkTimeZone)
                .build();

        // Then
        assertEquals("Tokyo timezone should match", tokyoTimeZone, tokyoEvent.getTimeZone());
        assertEquals("New York timezone should match", newYorkTimeZone, newYorkEvent.getTimeZone());
    }

    // ==================== Performance Tests ====================

    @Test
    public void testEvent_ShouldCreateWithinReasonableTime_WhenManyVenuesAdded() {
        // Given
        Event event = new Event.Builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .build();
        long startTime = System.currentTimeMillis();

        // When
        for (int i = 0; i < 1000; i++) {
            Venue venue = new Venue.Builder()
                    .id((long) i)
                    .name("Venue " + i)
                    .build();
            event.addVenue(venue);
        }

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals("Should have 1000 venues", 1000, event.getVenues().size());
        assertTrue("Should complete within reasonable time (1000ms)", duration < 1000);
    }
}
