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

import java.io.IOException;

import org.junit.Test;

/**
 * Test class for {@link GeoLocation}.
 * 
 * @author Test Author
 */
public class GeoLocationTest {

    // Test data constants
    private static final String TEST_LATITUDE = "35.6762";
    private static final String TEST_LONGITUDE = "139.6503";
    private static final String TEST_ADDRESS = "Tokyo, Japan";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateGeoLocation_WhenValidParametersProvided() {
        // When
        GeoLocation geoLocation = createTestGeoLocation();

        // Then
        assertNotNull("GeoLocation should not be null", geoLocation);
        assertEquals("Latitude should match", TEST_LATITUDE, geoLocation.toLatitude().toString());
        assertEquals("Longitude should match", TEST_LONGITUDE, geoLocation.toLongitude().toString());
    }

    @Test
    public void testConstructor_ShouldCreateGeoLocation_WhenNullValuesProvided() {
        // When
        GeoLocation geoLocation = new GeoLocation(null, null);

        // Then
        assertNotNull("GeoLocation should not be null", geoLocation);
        assertNull("Latitude should be null", geoLocation.toLatitude());
        assertNull("Longitude should be null", geoLocation.toLongitude());
    }

    @Test
    public void testConstructor_ShouldCreateGeoLocation_WhenEmptyStringsProvided() {
        // When
        GeoLocation geoLocation = new GeoLocation("", "");

        // Then
        assertNotNull("GeoLocation should not be null", geoLocation);
        assertEquals("Latitude should be empty string converted to 0.0", 0.0, geoLocation.toLatitude(), 0.001);
        assertEquals("Longitude should be empty string converted to 0.0", 0.0, geoLocation.toLongitude(), 0.001);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldReturnCorrectFormat() {
        // Given
        GeoLocation geoLocation = createTestGeoLocation();

        // When
        String result = geoLocation.toString();

        // Then
        assertEquals("ToString should return correct format",
                "Lat: " + TEST_LATITUDE + ", Lon: " + TEST_LONGITUDE, result);
    }

    @Test
    public void testToString_ShouldHandleNullValues() {
        // Given
        GeoLocation geoLocation = new GeoLocation(null, null);

        // When
        String result = geoLocation.toString();

        // Then
        assertEquals("ToString should handle null values", "Lat: null, Lon: null", result);
    }

    @Test
    public void testToString_ShouldHandleEmptyStrings() {
        // Given
        GeoLocation geoLocation = new GeoLocation("", "");

        // When
        String result = geoLocation.toString();

        // Then
        assertEquals("ToString should handle empty strings", "Lat: , Lon: ", result);
    }

    // ==================== ToLongitude Tests ====================

    @Test
    public void testToLongitude_ShouldReturnCorrectValue() {
        // Given
        GeoLocation geoLocation = createTestGeoLocation();

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should match", Double.valueOf(TEST_LONGITUDE), longitude);
    }

    @Test
    public void testToLongitude_ShouldReturnNull_WhenNullValueProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, null);

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertNull("Longitude should be null", longitude);
    }

    @Test
    public void testToLongitude_ShouldReturnZero_WhenEmptyStringProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, "");

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should be 0.0 for empty string", 0.0, longitude, 0.001);
    }

    @Test
    public void testToLongitude_ShouldReturnCorrectValue_WhenNegativeValueProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, "-139.6503");

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should handle negative values", -139.6503, longitude, 0.001);
    }

    // ==================== ToLatitude Tests ====================

    @Test
    public void testToLatitude_ShouldReturnCorrectValue() {
        // Given
        GeoLocation geoLocation = createTestGeoLocation();

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should match", Double.valueOf(TEST_LATITUDE), latitude);
    }

    @Test
    public void testToLatitude_ShouldReturnNull_WhenNullValueProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation(null, TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertNull("Latitude should be null", latitude);
    }

    @Test
    public void testToLatitude_ShouldReturnZero_WhenEmptyStringProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation("", TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should be 0.0 for empty string", 0.0, latitude, 0.001);
    }

    @Test
    public void testToLatitude_ShouldReturnCorrectValue_WhenNegativeValueProvided() {
        // Given
        GeoLocation geoLocation = new GeoLocation("-35.6762", TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should handle negative values", -35.6762, latitude, 0.001);
    }

    // ==================== Get GeoLocation Tests ====================

    @Test(expected = IllegalArgumentException.class)
    public void testGetGeoLocation_ShouldThrowException_WhenNullAddressProvided()
            throws IOException, InterruptedException {
        // When
        GeoLocation.getGeoLocation(null);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGeoLocation_ShouldThrowException_WhenEmptyAddressProvided()
            throws IOException, InterruptedException {
        // When
        GeoLocation.getGeoLocation("");

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGeoLocation_ShouldThrowException_WhenWhitespaceAddressProvided()
            throws IOException, InterruptedException {
        // When
        GeoLocation.getGeoLocation("   ");

        // Then - Exception should be thrown
    }

    // Note: The following tests are commented out because they require actual HTTP
    // calls to Google Maps API
    // In a real testing environment, these would be mocked or use a test API key

    /*
     * @Test
     * public void
     * testGetGeoLocation_ShouldReturnValidLocation_WhenValidAddressProvided()
     * throws IOException, InterruptedException {
     * // When
     * GeoLocation geoLocation = GeoLocation.getGeoLocation(TEST_ADDRESS);
     * 
     * // Then
     * assertNotNull("GeoLocation should not be null", geoLocation);
     * assertNotNull("Latitude should not be null", geoLocation.toLatitude());
     * assertNotNull("Longitude should not be null", geoLocation.toLongitude());
     * assertTrue("Latitude should be within valid range", geoLocation.toLatitude()
     * >= -90 && geoLocation.toLatitude() <= 90);
     * assertTrue("Longitude should be within valid range",
     * geoLocation.toLongitude() >= -180 && geoLocation.toLongitude() <= 180);
     * }
     * 
     * @Test
     * public void testGetGeoLocation_ShouldReturnNull_WhenAddressNotFound() throws
     * IOException, InterruptedException {
     * // When
     * GeoLocation geoLocation =
     * GeoLocation.getGeoLocation("ThisAddressDoesNotExist12345");
     * 
     * // Then
     * assertNull("GeoLocation should be null for non-existent address",
     * geoLocation);
     * }
     * 
     * @Test(expected = IOException.class)
     * public void testGetGeoLocation_ShouldThrowException_WhenBadRequest() throws
     * IOException, InterruptedException {
     * // When
     * GeoLocation.getGeoLocation(""); // This should trigger a 400 status code
     * 
     * // Then - Exception should be thrown
     * }
     */

    // ==================== Edge Cases Tests ====================

    @Test
    public void testToLatitude_ShouldHandleVeryLargeNumbers() {
        // Given
        GeoLocation geoLocation = new GeoLocation("90.0", TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should handle maximum value", 90.0, latitude, 0.001);
    }

    @Test
    public void testToLongitude_ShouldHandleVeryLargeNumbers() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, "180.0");

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should handle maximum value", 180.0, longitude, 0.001);
    }

    @Test
    public void testToLatitude_ShouldHandleVerySmallNumbers() {
        // Given
        GeoLocation geoLocation = new GeoLocation("-90.0", TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should handle minimum value", -90.0, latitude, 0.001);
    }

    @Test
    public void testToLongitude_ShouldHandleVerySmallNumbers() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, "-180.0");

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should handle minimum value", -180.0, longitude, 0.001);
    }

    @Test
    public void testToLatitude_ShouldHandleDecimalPrecision() {
        // Given
        GeoLocation geoLocation = new GeoLocation("35.6761919", TEST_LONGITUDE);

        // When
        Double latitude = geoLocation.toLatitude();

        // Then
        assertEquals("Latitude should handle decimal precision", 35.6761919, latitude, 0.0000001);
    }

    @Test
    public void testToLongitude_ShouldHandleDecimalPrecision() {
        // Given
        GeoLocation geoLocation = new GeoLocation(TEST_LATITUDE, "139.6503102");

        // When
        Double longitude = geoLocation.toLongitude();

        // Then
        assertEquals("Longitude should handle decimal precision", 139.6503102, longitude, 0.0000001);
    }

    // ==================== Helper Methods ====================

    private GeoLocation createTestGeoLocation() {
        return new GeoLocation(TEST_LATITUDE, TEST_LONGITUDE);
    }
}