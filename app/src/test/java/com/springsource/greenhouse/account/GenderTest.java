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
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link Gender}.
 * 
 * @author Test Author
 */
public class GenderTest {

    // ==================== Enum Values Tests ====================

    @Test
    public void testEnumValues_ShouldHaveCorrectValues() {
        // When
        Gender[] values = Gender.values();

        // Then
        assertEquals("Should have exactly 2 values", 2, values.length);
        assertTrue("Should contain MALE", java.util.Arrays.asList(values).contains(Gender.MALE));
        assertTrue("Should contain FEMALE", java.util.Arrays.asList(values).contains(Gender.FEMALE));
    }

    @Test
    public void testEnumValues_ShouldBeInCorrectOrder() {
        // When
        Gender[] values = Gender.values();

        // Then
        assertEquals("First value should be MALE", Gender.MALE, values[0]);
        assertEquals("Second value should be FEMALE", Gender.FEMALE, values[1]);
    }

    // ==================== Code Tests ====================

    @Test
    public void testCode_ShouldReturnCorrectCode_WhenMale() {
        // When
        char code = Gender.MALE.code();

        // Then
        assertEquals("MALE should have code 'M'", 'M', code);
    }

    @Test
    public void testCode_ShouldReturnCorrectCode_WhenFemale() {
        // When
        char code = Gender.FEMALE.code();

        // Then
        assertEquals("FEMALE should have code 'F'", 'F', code);
    }

    @Test
    public void testCode_ShouldReturnDifferentCodes_WhenDifferentGenders() {
        // When
        char maleCode = Gender.MALE.code();
        char femaleCode = Gender.FEMALE.code();

        // Then
        assertNotEquals("Male and female codes should be different", maleCode, femaleCode);
    }

    // ==================== ValueOf Tests ====================

    @Test
    public void testValueOf_ShouldReturnMale_WhenMProvided() {
        // When
        Gender gender = Gender.valueOf('M');

        // Then
        assertEquals("Should return MALE for 'M'", Gender.MALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenFProvided() {
        // When
        Gender gender = Gender.valueOf('F');

        // Then
        assertEquals("Should return FEMALE for 'F'", Gender.FEMALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenLowercaseFProvided() {
        // When
        Gender gender = Gender.valueOf('f');

        // Then
        assertEquals("Should return FEMALE for 'f'", Gender.FEMALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenLowercaseMProvided() {
        // When
        Gender gender = Gender.valueOf('m');

        // Then
        assertEquals("Should return MALE for 'm'", Gender.MALE, gender);
    }

    // ==================== Round Trip Tests ====================

    @Test
    public void testRoundTrip_ShouldWorkCorrectly_WhenMaleCodeIsEncodedAndDecoded() {
        // Given
        Gender originalGender = Gender.MALE;

        // When
        char code = originalGender.code();
        Gender decodedGender = Gender.valueOf(code);

        // Then
        assertEquals("Decoded gender should match original", originalGender, decodedGender);
    }

    @Test
    public void testRoundTrip_ShouldWorkCorrectly_WhenFemaleCodeIsEncodedAndDecoded() {
        // Given
        Gender originalGender = Gender.FEMALE;

        // When
        char code = originalGender.code();
        Gender decodedGender = Gender.valueOf(code);

        // Then
        assertEquals("Decoded gender should match original", originalGender, decodedGender);
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEdgeCase_ShouldHandleInvalidCharacter_WhenNonGenderCharacterProvided() {
        // When
        Gender gender = Gender.valueOf('X');

        // Then
        assertEquals("Should return FEMALE for invalid character", Gender.FEMALE, gender);
    }

    @Test
    public void testEdgeCase_ShouldHandleSpecialCharacters_WhenSpecialCharactersProvided() {
        // Test various special characters
        char[] specialChars = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
                '+', '-', '=', '[', ']', '{', '}', '|', ';', ':',
                '"', '\'', ',', '.', '/', '<', '>', '?' };

        for (char specialChar : specialChars) {
            // When
            Gender gender = Gender.valueOf(specialChar);

            // Then
            assertEquals("Should return FEMALE for special character: " + specialChar,
                    Gender.FEMALE, gender);
        }
    }

    @Test
    public void testEdgeCase_ShouldHandleNumericCharacters_WhenNumericCharactersProvided() {
        // Test numeric characters
        for (char digit = '0'; digit <= '9'; digit++) {
            // When
            Gender gender = Gender.valueOf(digit);

            // Then
            assertEquals("Should return FEMALE for digit: " + digit,
                    Gender.FEMALE, gender);
        }
    }

    @Test
    public void testEdgeCase_ShouldHandleUnicodeCharacters_WhenUnicodeCharactersProvided() {
        // Test some Unicode characters
        char[] unicodeChars = { 'あ', 'い', 'う', 'え', 'お', 'α', 'β', 'γ', 'δ', 'ε' };

        for (char unicodeChar : unicodeChars) {
            // When
            Gender gender = Gender.valueOf(unicodeChar);

            // Then
            assertEquals("Should return FEMALE for Unicode character: " + unicodeChar,
                    Gender.FEMALE, gender);
        }
    }

    // ==================== Consistency Tests ====================

    @Test
    public void testConsistency_ShouldBeConsistent_WhenSameCharacterProvidedMultipleTimes() {
        // Given
        char testChar = 'M';

        // When
        Gender gender1 = Gender.valueOf(testChar);
        Gender gender2 = Gender.valueOf(testChar);
        Gender gender3 = Gender.valueOf(testChar);

        // Then
        assertEquals("All results should be the same", gender1, gender2);
        assertEquals("All results should be the same", gender2, gender3);
        assertEquals("Should be MALE", Gender.MALE, gender1);
    }

    @Test
    public void testConsistency_ShouldBeConsistent_WhenDifferentCasesProvided() {
        // When
        Gender upperCase = Gender.valueOf('M');
        Gender lowerCase = Gender.valueOf('m');

        // Then
        assertEquals("Upper and lower case should return same gender", upperCase, lowerCase);
        assertEquals("Should be MALE", Gender.MALE, upperCase);
    }

    // ==================== Performance Tests ====================

    @Test
    public void testPerformance_ShouldCompleteWithinReasonableTime_WhenCalledMultipleTimes() {
        // Given
        long startTime = System.currentTimeMillis();
        int iterations = 10000;

        // When
        for (int i = 0; i < iterations; i++) {
            Gender.valueOf('M');
            Gender.valueOf('F');
        }

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue("Should complete within reasonable time (1000ms)", duration < 1000);
    }

    // ==================== Enum Standard Tests ====================

    @Test
    public void testEnumStandard_ShouldHaveCorrectToString() {
        // When
        String maleString = Gender.MALE.toString();
        String femaleString = Gender.FEMALE.toString();

        // Then
        assertEquals("MALE toString should be 'MALE'", "MALE", maleString);
        assertEquals("FEMALE toString should be 'FEMALE'", "FEMALE", femaleString);
    }

    @Test
    public void testEnumStandard_ShouldHaveCorrectName() {
        // When
        String maleName = Gender.MALE.name();
        String femaleName = Gender.FEMALE.name();

        // Then
        assertEquals("MALE name should be 'MALE'", "MALE", maleName);
        assertEquals("FEMALE name should be 'FEMALE'", "FEMALE", femaleName);
    }

    @Test
    public void testEnumStandard_ShouldHaveCorrectOrdinal() {
        // When
        int maleOrdinal = Gender.MALE.ordinal();
        int femaleOrdinal = Gender.FEMALE.ordinal();

        // Then
        assertEquals("MALE ordinal should be 0", 0, maleOrdinal);
        assertEquals("FEMALE ordinal should be 1", 1, femaleOrdinal);
    }

    @Test
    public void testEnumStandard_ShouldBeComparable() {
        // When
        int comparison = Gender.MALE.compareTo(Gender.FEMALE);

        // Then
        assertTrue("MALE should be less than FEMALE", comparison < 0);

        // When
        comparison = Gender.FEMALE.compareTo(Gender.MALE);

        // Then
        assertTrue("FEMALE should be greater than MALE", comparison > 0);

        // When
        comparison = Gender.MALE.compareTo(Gender.MALE);

        // Then
        assertEquals("MALE should equal MALE", 0, comparison);
    }
}