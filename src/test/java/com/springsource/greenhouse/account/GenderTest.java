/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.14.2/userguide/building_java_projects.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
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
    public void testEnumValues_ShouldContainMaleAndFemale() {
        // When
        Gender[] values = Gender.values();

        // Then
        assertEquals("Should have exactly 2 enum values", 2, values.length);
        assertTrue("Should contain MALE", contains(values, Gender.MALE));
        assertTrue("Should contain FEMALE", contains(values, Gender.FEMALE));
    }

    @Test
    public void testMale_ShouldHaveCorrectCode() {
        // When
        char code = Gender.MALE.code();

        // Then
        assertEquals("MALE should have code 'M'", 'M', code);
    }

    @Test
    public void testFemale_ShouldHaveCorrectCode() {
        // When
        char code = Gender.FEMALE.code();

        // Then
        assertEquals("FEMALE should have code 'F'", 'F', code);
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
    public void testValueOf_ShouldReturnFemale_WhenLowerCaseFProvided() {
        // When
        Gender gender = Gender.valueOf('f');

        // Then
        assertEquals("Should return FEMALE for 'f'", Gender.FEMALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenAnyOtherCharacterProvided() {
        // When
        Gender gender = Gender.valueOf('X');

        // Then
        assertEquals("Should return FEMALE for any character other than 'M'", Gender.FEMALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenSpecialCharacterProvided() {
        // When
        Gender gender = Gender.valueOf('@');

        // Then
        assertEquals("Should return FEMALE for special character", Gender.FEMALE, gender);
    }

    @Test
    public void testValueOf_ShouldReturnFemale_WhenNumberProvided() {
        // When
        Gender gender = Gender.valueOf('1');

        // Then
        assertEquals("Should return FEMALE for number", Gender.FEMALE, gender);
    }

    // ==================== Code Method Tests ====================

    @Test
    public void testCode_ShouldReturnM_ForMale() {
        // When
        char code = Gender.MALE.code();

        // Then
        assertEquals("MALE code should be 'M'", 'M', code);
    }

    @Test
    public void testCode_ShouldReturnF_ForFemale() {
        // When
        char code = Gender.FEMALE.code();

        // Then
        assertEquals("FEMALE code should be 'F'", 'F', code);
    }

    @Test
    public void testCode_ShouldReturnCorrectCode_WhenValueOfIsUsed() {
        // Given
        Gender maleGender = Gender.valueOf('M');
        Gender femaleGender = Gender.valueOf('F');

        // When
        char maleCode = maleGender.code();
        char femaleCode = femaleGender.code();

        // Then
        assertEquals("Male gender code should be 'M'", 'M', maleCode);
        assertEquals("Female gender code should be 'F'", 'F', femaleCode);
    }

    // ==================== Round Trip Tests ====================

    @Test
    public void testRoundTrip_ShouldWorkForMale() {
        // Given
        char originalCode = 'M';

        // When
        Gender gender = Gender.valueOf(originalCode);
        char retrievedCode = gender.code();

        // Then
        assertEquals("Retrieved code should match original", originalCode, retrievedCode);
        assertEquals("Gender should be MALE", Gender.MALE, gender);
    }

    @Test
    public void testRoundTrip_ShouldWorkForFemale() {
        // Given
        char originalCode = 'F';

        // When
        Gender gender = Gender.valueOf(originalCode);
        char retrievedCode = gender.code();

        // Then
        assertEquals("Retrieved code should match original", originalCode, retrievedCode);
        assertEquals("Gender should be FEMALE", Gender.FEMALE, gender);
    }

    // ==================== Enum Behavior Tests ====================

    @Test
    public void testEnumEquality_ShouldWorkCorrectly() {
        // Given
        Gender male1 = Gender.MALE;
        Gender male2 = Gender.MALE;
        Gender female = Gender.FEMALE;

        // Then
        assertSame("Same enum instances should be identical", male1, male2);
        assertEquals("Same enum values should be equal", male1, male2);
        assertNotSame("Different enum values should not be identical", male1, female);
        assertNotEquals("Different enum values should not be equal", male1, female);
    }

    @Test
    public void testEnumComparison_ShouldWorkCorrectly() {
        // Given
        Gender male = Gender.MALE;
        Gender female = Gender.FEMALE;

        // Then
        assertTrue("MALE should not equal FEMALE", !male.equals(female));
        assertTrue("FEMALE should not equal MALE", !female.equals(male));
        assertTrue("MALE should equal MALE", male.equals(male));
        assertTrue("FEMALE should equal FEMALE", female.equals(female));
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testValueOf_ShouldHandleAllCharacters() {
        // Test various characters to ensure the logic works as expected
        char[] testChars = { 'A', 'B', 'C', 'D', 'E', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z' };

        for (char c : testChars) {
            // When
            Gender gender = Gender.valueOf(c);

            // Then
            assertEquals("Any character other than 'M' should return FEMALE", Gender.FEMALE, gender);
        }
    }

    @Test
    public void testValueOf_ShouldHandleLowerCaseM() {
        // When
        Gender gender = Gender.valueOf('m');

        // Then
        assertEquals("Lowercase 'm' should return FEMALE (not MALE)", Gender.FEMALE, gender);
    }

    // ==================== Helper Methods ====================

    private boolean contains(Gender[] values, Gender target) {
        for (Gender value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
}