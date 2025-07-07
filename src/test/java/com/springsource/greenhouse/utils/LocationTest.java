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
package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;

/**
 * Test class for {@link Location}.
 * 
 * @author Test Author
 */
public class LocationTest {

    // Test data constants
    private static final Double TEST_LATITUDE = 35.6762;
    private static final Double TEST_LONGITUDE = 139.6503;
    private static final Double TEST_LATITUDE_2 = 40.7128;
    private static final Double TEST_LONGITUDE_2 = -74.0060;

    private Location testLocation;
    private Location testLocation2;

    @Before
    public void setUp() {
        testLocation = new Location(TEST_LATITUDE, TEST_LONGITUDE);
        testLocation2 = new Location(TEST_LATITUDE_2, TEST_LONGITUDE_2);

        // Clear request context before each test
        RequestContextHolder.resetRequestAttributes();
    }

    @After
    public void tearDown() {
        // Clear request context after each test
        RequestContextHolder.resetRequestAttributes();
    }

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateLocation_WhenValidParametersProvided() {
        // When
        Location location = new Location(TEST_LATITUDE, TEST_LONGITUDE);

        // Then
        assertNotNull("Location should not be null", location);
        assertEquals("Latitude should match", TEST_LATITUDE, location.getLatitude());
        assertEquals("Longitude should match", TEST_LONGITUDE, location.getLongitude());
    }

    @Test
    public void testConstructor_ShouldCreateLocation_WhenNullValuesProvided() {
        // When
        Location location = new Location(null, null);

        // Then
        assertNotNull("Location should not be null", location);
        assertNull("Latitude should be null", location.getLatitude());
        assertNull("Longitude should be null", location.getLongitude());
    }

    @Test
    public void testConstructor_ShouldCreateLocation_WhenNegativeValuesProvided() {
        // Given
        Double negativeLatitude = -35.6762;
        Double negativeLongitude = -139.6503;

        // When
        Location location = new Location(negativeLatitude, negativeLongitude);

        // Then
        assertNotNull("Location should not be null", location);
        assertEquals("Latitude should match", negativeLatitude, location.getLatitude());
        assertEquals("Longitude should match", negativeLongitude, location.getLongitude());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetLatitude_ShouldReturnCorrectValue() {
        // When
        Double latitude = testLocation.getLatitude();

        // Then
        assertEquals("Latitude should match", TEST_LATITUDE, latitude);
    }

    @Test
    public void testGetLongitude_ShouldReturnCorrectValue() {
        // When
        Double longitude = testLocation.getLongitude();

        // Then
        assertEquals("Longitude should match", TEST_LONGITUDE, longitude);
    }

    @Test
    public void testGetLatitude_ShouldReturnNull_WhenNullValueProvided() {
        // Given
        Location location = new Location(null, TEST_LONGITUDE);

        // When
        Double latitude = location.getLatitude();

        // Then
        assertNull("Latitude should be null", latitude);
    }

    @Test
    public void testGetLongitude_ShouldReturnNull_WhenNullValueProvided() {
        // Given
        Location location = new Location(TEST_LATITUDE, null);

        // When
        Double longitude = location.getLongitude();

        // Then
        assertNull("Longitude should be null", longitude);
    }

    // ==================== Equals Tests ====================

    @Test
    public void testEquals_ShouldReturnTrue_WhenSameLocationProvided() {
        // Given
        Location sameLocation = new Location(TEST_LATITUDE, TEST_LONGITUDE);

        // When
        boolean result = testLocation.equals(sameLocation);

        // Then
        assertTrue("Should be equal when same coordinates", result);
    }

    @Test
    public void testEquals_ShouldReturnTrue_WhenSameInstanceProvided() {
        // When
        boolean result = testLocation.equals(testLocation);

        // Then
        assertTrue("Should be equal when same instance", result);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenDifferentLocationProvided() {
        // When
        boolean result = testLocation.equals(testLocation2);

        // Then
        assertFalse("Should not be equal when different coordinates", result);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenNullProvided() {
        // When
        boolean result = testLocation.equals(null);

        // Then
        assertFalse("Should not be equal when null provided", result);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenDifferentTypeProvided() {
        // When
        boolean result = testLocation.equals("Not a Location");

        // Then
        assertFalse("Should not be equal when different type provided", result);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenDifferentLatitudeProvided() {
        // Given
        Location differentLatitude = new Location(TEST_LATITUDE_2, TEST_LONGITUDE);

        // When
        boolean result = testLocation.equals(differentLatitude);

        // Then
        assertFalse("Should not be equal when different latitude", result);
    }

    @Test
    public void testEquals_ShouldReturnFalse_WhenDifferentLongitudeProvided() {
        // Given
        Location differentLongitude = new Location(TEST_LATITUDE, TEST_LONGITUDE_2);

        // When
        boolean result = testLocation.equals(differentLongitude);

        // Then
        assertFalse("Should not be equal when different longitude", result);
    }

    @Test
    public void testEquals_ShouldHandleNullCoordinates() {
        // Given
        Location nullLocation = new Location(null, null);
        Location nullLocation2 = new Location(null, null);

        // When
        boolean result = nullLocation.equals(nullLocation2);

        // Then
        assertTrue("Should be equal when both have null coordinates", result);
    }

    @Test
    public void testEquals_ShouldHandleMixedNullCoordinates() {
        // Given
        Location nullLatitude = new Location(null, TEST_LONGITUDE);
        Location nullLongitude = new Location(TEST_LATITUDE, null);

        // When
        boolean result = nullLatitude.equals(nullLongitude);

        // Then
        assertFalse("Should not be equal when one has null latitude and other has null longitude", result);
    }

    // ==================== HashCode Tests ====================

    @Test
    public void testHashCode_ShouldReturnSameValue_WhenSameLocationProvided() {
        // Given
        Location sameLocation = new Location(TEST_LATITUDE, TEST_LONGITUDE);

        // When
        int hashCode1 = testLocation.hashCode();
        int hashCode2 = sameLocation.hashCode();

        // Then
        assertEquals("Hash codes should be equal for same coordinates", hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_ShouldReturnDifferentValue_WhenDifferentLocationProvided() {
        // When
        int hashCode1 = testLocation.hashCode();
        int hashCode2 = testLocation2.hashCode();

        // Then
        assertNotEquals("Hash codes should be different for different coordinates", hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_ShouldHandleNullCoordinates() {
        // Given
        Location nullLocation = new Location(null, null);

        // When
        int hashCode = nullLocation.hashCode();

        // Then
        // Should not throw NullPointerException
        assertNotNull("Hash code should not be null", hashCode);
    }

    @Test
    public void testHashCode_ShouldHandleMixedNullCoordinates() {
        // Given
        Location nullLatitude = new Location(null, TEST_LONGITUDE);
        Location nullLongitude = new Location(TEST_LATITUDE, null);

        // When
        int hashCode1 = nullLatitude.hashCode();
        int hashCode2 = nullLongitude.hashCode();

        // Then
        // Should not throw NullPointerException and should be different
        assertNotEquals("Hash codes should be different for mixed null coordinates", hashCode1, hashCode2);
    }

    // ==================== Get Current Location Tests ====================

    @Test
    public void testGetCurrentLocation_ShouldReturnNull_WhenNoRequestContext() {
        // When
        Location currentLocation = Location.getCurrentLocation();

        // Then
        assertNull("Should return null when no request context", currentLocation);
    }

    @Test
    public void testGetCurrentLocation_ShouldReturnNull_WhenNoLocationAttribute() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        // When
        Location currentLocation = Location.getCurrentLocation();

        // Then
        assertNull("Should return null when no location attribute", currentLocation);
    }

    @Test
    public void testGetCurrentLocation_ShouldReturnLocation_WhenLocationAttributeExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        attributes.setAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE,
                testLocation, RequestAttributes.SCOPE_REQUEST);

        // When
        Location currentLocation = Location.getCurrentLocation();

        // Then
        assertNotNull("Should return location when attribute exists", currentLocation);
        assertEquals("Should return correct location", testLocation, currentLocation);
    }

    @Test
    public void testGetCurrentLocation_ShouldReturnCorrectLocation_WhenLocationAttributeExists() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        attributes.setAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE,
                testLocation2, RequestAttributes.SCOPE_REQUEST);

        // When
        Location currentLocation = Location.getCurrentLocation();

        // Then
        assertNotNull("Should return location when attribute exists", currentLocation);
        assertEquals("Should return correct location", testLocation2, currentLocation);
        assertEquals("Should return correct latitude", TEST_LATITUDE_2, currentLocation.getLatitude());
        assertEquals("Should return correct longitude", TEST_LONGITUDE_2, currentLocation.getLongitude());
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testEquals_ShouldHandleVeryLargeCoordinates() {
        // Given
        Double largeLatitude = 90.0;
        Double largeLongitude = 180.0;
        Location largeLocation = new Location(largeLatitude, largeLongitude);
        Location largeLocation2 = new Location(largeLatitude, largeLongitude);

        // When
        boolean result = largeLocation.equals(largeLocation2);

        // Then
        assertTrue("Should be equal for very large coordinates", result);
    }

    @Test
    public void testEquals_ShouldHandleVerySmallCoordinates() {
        // Given
        Double smallLatitude = -90.0;
        Double smallLongitude = -180.0;
        Location smallLocation = new Location(smallLatitude, smallLongitude);
        Location smallLocation2 = new Location(smallLatitude, smallLongitude);

        // When
        boolean result = smallLocation.equals(smallLocation2);

        // Then
        assertTrue("Should be equal for very small coordinates", result);
    }

    @Test
    public void testEquals_ShouldHandleZeroCoordinates() {
        // Given
        Location zeroLocation = new Location(0.0, 0.0);
        Location zeroLocation2 = new Location(0.0, 0.0);

        // When
        boolean result = zeroLocation.equals(zeroLocation2);

        // Then
        assertTrue("Should be equal for zero coordinates", result);
    }

    @Test
    public void testHashCode_ShouldHandleZeroCoordinates() {
        // Given
        Location zeroLocation = new Location(0.0, 0.0);

        // When
        int hashCode = zeroLocation.hashCode();

        // Then
        assertNotNull("Hash code should not be null for zero coordinates", hashCode);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testLocationShouldBeImmutable() {
        // Given
        Double originalLatitude = testLocation.getLatitude();
        Double originalLongitude = testLocation.getLongitude();

        // When - Create another location with different coordinates
        Location anotherLocation = new Location(TEST_LATITUDE_2, TEST_LONGITUDE_2);

        // Then - Original location should remain unchanged
        assertEquals("Original latitude should remain unchanged", originalLatitude, testLocation.getLatitude());
        assertEquals("Original longitude should remain unchanged", originalLongitude, testLocation.getLongitude());
        assertNotEquals("New location should have different coordinates", testLocation, anotherLocation);
    }
}