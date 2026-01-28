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
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link PictureSize}.
 * 
 * @author Test Author
 */
public class PictureSizeTest {

    // ==================== Enum Values Tests ====================

    @Test
    public void testEnumValues_ShouldContainAllPictureSizes() {
        // When
        PictureSize[] values = PictureSize.values();

        // Then
        assertEquals("Should have exactly 3 enum values", 3, values.length);
        assertTrue("Should contain SMALL", contains(values, PictureSize.SMALL));
        assertTrue("Should contain NORMAL", contains(values, PictureSize.NORMAL));
        assertTrue("Should contain LARGE", contains(values, PictureSize.LARGE));
    }

    @Test
    public void testEnumValues_ShouldBeInCorrectOrder() {
        // When
        PictureSize[] values = PictureSize.values();

        // Then
        assertEquals("First value should be SMALL", PictureSize.SMALL, values[0]);
        assertEquals("Second value should be NORMAL", PictureSize.NORMAL, values[1]);
        assertEquals("Third value should be LARGE", PictureSize.LARGE, values[2]);
    }

    // ==================== Individual Enum Value Tests ====================

    @Test
    public void testSmall_ShouldExist() {
        // When
        PictureSize small = PictureSize.SMALL;

        // Then
        assertNotNull("SMALL should not be null", small);
        assertEquals("SMALL should equal itself", PictureSize.SMALL, small);
    }

    @Test
    public void testNormal_ShouldExist() {
        // When
        PictureSize normal = PictureSize.NORMAL;

        // Then
        assertNotNull("NORMAL should not be null", normal);
        assertEquals("NORMAL should equal itself", PictureSize.NORMAL, normal);
    }

    @Test
    public void testLarge_ShouldExist() {
        // When
        PictureSize large = PictureSize.LARGE;

        // Then
        assertNotNull("LARGE should not be null", large);
        assertEquals("LARGE should equal itself", PictureSize.LARGE, large);
    }

    // ==================== Enum Equality Tests ====================

    @Test
    public void testEnumEquality_ShouldWorkCorrectly() {
        // Given
        PictureSize small1 = PictureSize.SMALL;
        PictureSize small2 = PictureSize.SMALL;
        PictureSize normal = PictureSize.NORMAL;
        PictureSize large = PictureSize.LARGE;

        // Then
        assertSame("Same enum instances should be identical", small1, small2);
        assertEquals("Same enum values should be equal", small1, small2);
        assertNotSame("Different enum values should not be identical", small1, normal);
        assertNotEquals("Different enum values should not be equal", small1, normal);
        assertNotEquals("Different enum values should not be equal", small1, large);
        assertNotEquals("Different enum values should not be equal", normal, large);
    }

    @Test
    public void testEnumComparison_ShouldWorkCorrectly() {
        // Given
        PictureSize small = PictureSize.SMALL;
        PictureSize normal = PictureSize.NORMAL;
        PictureSize large = PictureSize.LARGE;

        // Then
        assertTrue("SMALL should equal SMALL", small.equals(small));
        assertTrue("NORMAL should equal NORMAL", normal.equals(normal));
        assertTrue("LARGE should equal LARGE", large.equals(large));
        assertTrue("SMALL should not equal NORMAL", !small.equals(normal));
        assertTrue("SMALL should not equal LARGE", !small.equals(large));
        assertTrue("NORMAL should not equal LARGE", !normal.equals(large));
    }

    // ==================== ValueOf Tests ====================

    @Test
    public void testValueOf_ShouldReturnCorrectEnum_WhenValidStringProvided() {
        // When
        PictureSize small = PictureSize.valueOf("SMALL");
        PictureSize normal = PictureSize.valueOf("NORMAL");
        PictureSize large = PictureSize.valueOf("LARGE");

        // Then
        assertEquals("Should return SMALL for 'SMALL'", PictureSize.SMALL, small);
        assertEquals("Should return NORMAL for 'NORMAL'", PictureSize.NORMAL, normal);
        assertEquals("Should return LARGE for 'LARGE'", PictureSize.LARGE, large);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenInvalidStringProvided() {
        // When
        PictureSize.valueOf("INVALID");

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenNullStringProvided() {
        // When
        PictureSize.valueOf(null);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenEmptyStringProvided() {
        // When
        PictureSize.valueOf("");

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenLowerCaseStringProvided() {
        // When
        PictureSize.valueOf("small");

        // Then - Exception should be thrown
    }

    // ==================== Ordinal Tests ====================

    @Test
    public void testOrdinal_ShouldReturnCorrectValues() {
        // When
        int smallOrdinal = PictureSize.SMALL.ordinal();
        int normalOrdinal = PictureSize.NORMAL.ordinal();
        int largeOrdinal = PictureSize.LARGE.ordinal();

        // Then
        assertEquals("SMALL should have ordinal 0", 0, smallOrdinal);
        assertEquals("NORMAL should have ordinal 1", 1, normalOrdinal);
        assertEquals("LARGE should have ordinal 2", 2, largeOrdinal);
    }

    // ==================== Name Tests ====================

    @Test
    public void testName_ShouldReturnCorrectStrings() {
        // When
        String smallName = PictureSize.SMALL.name();
        String normalName = PictureSize.NORMAL.name();
        String largeName = PictureSize.LARGE.name();

        // Then
        assertEquals("SMALL should have name 'SMALL'", "SMALL", smallName);
        assertEquals("NORMAL should have name 'NORMAL'", "NORMAL", normalName);
        assertEquals("LARGE should have name 'LARGE'", "LARGE", largeName);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldReturnCorrectStrings() {
        // When
        String smallString = PictureSize.SMALL.toString();
        String normalString = PictureSize.NORMAL.toString();
        String largeString = PictureSize.LARGE.toString();

        // Then
        assertEquals("SMALL toString should be 'SMALL'", "SMALL", smallString);
        assertEquals("NORMAL toString should be 'NORMAL'", "NORMAL", normalString);
        assertEquals("LARGE toString should be 'LARGE'", "LARGE", largeString);
    }

    // ==================== Switch Statement Tests ====================

    @Test
    public void testSwitchStatement_ShouldWorkWithAllValues() {
        // Test SMALL
        String smallResult = getPictureSizeDescription(PictureSize.SMALL);
        assertEquals("SMALL should return 'small'", "small", smallResult);

        // Test NORMAL
        String normalResult = getPictureSizeDescription(PictureSize.NORMAL);
        assertEquals("NORMAL should return 'normal'", "normal", normalResult);

        // Test LARGE
        String largeResult = getPictureSizeDescription(PictureSize.LARGE);
        assertEquals("LARGE should return 'large'", "large", largeResult);
    }

    // ==================== Array Operations Tests ====================

    @Test
    public void testArrayOperations_ShouldWorkCorrectly() {
        // Given
        PictureSize[] sizes = PictureSize.values();

        // When
        PictureSize firstSize = sizes[0];
        PictureSize lastSize = sizes[sizes.length - 1];

        // Then
        assertEquals("First size should be SMALL", PictureSize.SMALL, firstSize);
        assertEquals("Last size should be LARGE", PictureSize.LARGE, lastSize);
        assertEquals("Array length should be 3", 3, sizes.length);
    }

    // ==================== Helper Methods ====================

    private boolean contains(PictureSize[] values, PictureSize target) {
        for (PictureSize value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    private String getPictureSizeDescription(PictureSize size) {
        switch (size) {
            case SMALL:
                return "small";
            case NORMAL:
                return "normal";
            case LARGE:
                return "large";
            default:
                return "unknown";
        }
    }
}