/*
 * Copyright 2010-2011 the original author or authors.
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

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link GreenhousePasswordEncoder}.
 * 
 * @author Test Author
 */
public class GreenhousePasswordEncoderTest {

    // Test data constants
    private static final String TEST_SECRET = "test-secret-key";
    private static final String TEST_PASSWORD = "testPassword123";
    private static final String TEST_PASSWORD_2 = "anotherPassword456";
    private static final String EMPTY_PASSWORD = "";
    private static final String NULL_PASSWORD = null;
    private static final String SPECIAL_CHARS_PASSWORD = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
    private static final String UNICODE_PASSWORD = "パスワード123";
    private static final String LONG_PASSWORD = "a".repeat(1000);

    private GreenhousePasswordEncoder encoder;

    @Before
    public void setUp() {
        encoder = new GreenhousePasswordEncoder(TEST_SECRET);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateEncoder_WhenValidSecretProvided() {
        // When
        GreenhousePasswordEncoder testEncoder = new GreenhousePasswordEncoder(TEST_SECRET);

        // Then
        assertNotNull("Encoder should not be null", testEncoder);
    }

    @Test
    public void testConstructor_ShouldCreateEncoder_WhenCustomAlgorithmAndProviderProvided() {
        // When
        GreenhousePasswordEncoder testEncoder = new GreenhousePasswordEncoder("SHA-256", "SUN", TEST_SECRET);

        // Then
        assertNotNull("Encoder should not be null", testEncoder);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_ShouldThrowException_WhenInvalidAlgorithmProvided() {
        // When & Then
        new GreenhousePasswordEncoder("INVALID-ALGORITHM", "SUN", TEST_SECRET);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_ShouldThrowException_WhenInvalidProviderProvided() {
        // When & Then
        new GreenhousePasswordEncoder("SHA-256", "INVALID-PROVIDER", TEST_SECRET);
    }

    // ==================== Encode Tests ====================

    @Test
    public void testEncode_ShouldReturnEncodedPassword_WhenValidPasswordProvided() {
        // When
        String encodedPassword = encoder.encode(TEST_PASSWORD);

        // Then
        assertNotNull("Encoded password should not be null", encodedPassword);
        assertFalse("Encoded password should not be empty", encodedPassword.isEmpty());
        assertNotEquals("Encoded password should be different from original", TEST_PASSWORD, encodedPassword);
    }

    @Test
    public void testEncode_ShouldReturnDifferentEncodedPasswords_WhenSamePasswordEncodedMultipleTimes() {
        // When
        String encodedPassword1 = encoder.encode(TEST_PASSWORD);
        String encodedPassword2 = encoder.encode(TEST_PASSWORD);

        // Then
        assertNotEquals("Encoded passwords should be different due to random salt",
                encodedPassword1, encodedPassword2);
    }

    @Test
    public void testEncode_ShouldHandleEmptyPassword() {
        // When
        String encodedPassword = encoder.encode(EMPTY_PASSWORD);

        // Then
        assertNotNull("Encoded password should not be null", encodedPassword);
        assertFalse("Encoded password should not be empty", encodedPassword.isEmpty());
    }

    @Test
    public void testEncode_ShouldHandleSpecialCharacters() {
        // When
        String encodedPassword = encoder.encode(SPECIAL_CHARS_PASSWORD);

        // Then
        assertNotNull("Encoded password should not be null", encodedPassword);
        assertFalse("Encoded password should not be empty", encodedPassword.isEmpty());
    }

    @Test
    public void testEncode_ShouldHandleUnicodeCharacters() {
        // When
        String encodedPassword = encoder.encode(UNICODE_PASSWORD);

        // Then
        assertNotNull("Encoded password should not be null", encodedPassword);
        assertFalse("Encoded password should not be empty", encodedPassword.isEmpty());
    }

    @Test
    public void testEncode_ShouldHandleLongPassword() {
        // When
        String encodedPassword = encoder.encode(LONG_PASSWORD);

        // Then
        assertNotNull("Encoded password should not be null", encodedPassword);
        assertFalse("Encoded password should not be empty", encodedPassword.isEmpty());
    }

    // ==================== Matches Tests ====================

    @Test
    public void testMatches_ShouldReturnTrue_WhenPasswordMatchesEncodedPassword() {
        // Given
        String encodedPassword = encoder.encode(TEST_PASSWORD);

        // When
        boolean matches = encoder.matches(TEST_PASSWORD, encodedPassword);

        // Then
        assertTrue("Password should match encoded password", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenPasswordDoesNotMatchEncodedPassword() {
        // Given
        String encodedPassword = encoder.encode(TEST_PASSWORD);

        // When
        boolean matches = encoder.matches(TEST_PASSWORD_2, encodedPassword);

        // Then
        assertFalse("Password should not match encoded password", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenEmptyPasswordProvided() {
        // Given
        String encodedPassword = encoder.encode(TEST_PASSWORD);

        // When
        boolean matches = encoder.matches(EMPTY_PASSWORD, encodedPassword);

        // Then
        assertFalse("Empty password should not match", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenNullPasswordProvided() {
        // Given
        String encodedPassword = encoder.encode(TEST_PASSWORD);

        // When
        boolean matches = encoder.matches(NULL_PASSWORD, encodedPassword);

        // Then
        assertFalse("Null password should not match", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenNullEncodedPasswordProvided() {
        // When
        boolean matches = encoder.matches(TEST_PASSWORD, null);

        // Then
        assertFalse("Should return false when encoded password is null", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenEmptyEncodedPasswordProvided() {
        // When
        boolean matches = encoder.matches(TEST_PASSWORD, "");

        // Then
        assertFalse("Should return false when encoded password is empty", matches);
    }

    @Test
    public void testMatches_ShouldReturnFalse_WhenInvalidEncodedPasswordProvided() {
        // When
        boolean matches = encoder.matches(TEST_PASSWORD, "invalid-encoded-password");

        // Then
        assertFalse("Should return false when encoded password is invalid", matches);
    }

    // ==================== Round Trip Tests ====================

    @Test
    public void testRoundTrip_ShouldWorkCorrectly_WhenPasswordIsEncodedAndThenMatched() {
        // Given
        String password = TEST_PASSWORD;

        // When
        String encodedPassword = encoder.encode(password);
        boolean matches = encoder.matches(password, encodedPassword);

        // Then
        assertTrue("Password should match after round trip", matches);
    }

    @Test
    public void testRoundTrip_ShouldWorkCorrectly_WhenSpecialCharactersPasswordIsEncodedAndThenMatched() {
        // Given
        String password = SPECIAL_CHARS_PASSWORD;

        // When
        String encodedPassword = encoder.encode(password);
        boolean matches = encoder.matches(password, encodedPassword);

        // Then
        assertTrue("Special characters password should match after round trip", matches);
    }

    @Test
    public void testRoundTrip_ShouldWorkCorrectly_WhenUnicodePasswordIsEncodedAndThenMatched() {
        // Given
        String password = UNICODE_PASSWORD;

        // When
        String encodedPassword = encoder.encode(password);
        boolean matches = encoder.matches(password, encodedPassword);

        // Then
        assertTrue("Unicode password should match after round trip", matches);
    }

    // ==================== Security Tests ====================

    @Test
    public void testSecurity_ShouldNotRevealOriginalPassword_WhenEncoded() {
        // Given
        String password = TEST_PASSWORD;

        // When
        String encodedPassword = encoder.encode(password);

        // Then
        assertFalse("Encoded password should not contain original password",
                encodedPassword.contains(password));
    }

    @Test
    public void testSecurity_ShouldProduceConsistentResults_WhenSamePasswordEncodedMultipleTimes() {
        // Given
        String password = TEST_PASSWORD;

        // When
        String encodedPassword1 = encoder.encode(password);
        String encodedPassword2 = encoder.encode(password);

        // Then
        // Both should be valid but different due to salt
        assertTrue("First encoded password should be valid",
                encoder.matches(password, encodedPassword1));
        assertTrue("Second encoded password should be valid",
                encoder.matches(password, encodedPassword2));
        assertNotEquals("Encoded passwords should be different",
                encodedPassword1, encodedPassword2);
    }

    @Test
    public void testSecurity_ShouldHandleDifferentSecrets_WhenDifferentEncodersUsed() {
        // Given
        GreenhousePasswordEncoder encoder1 = new GreenhousePasswordEncoder("secret1");
        GreenhousePasswordEncoder encoder2 = new GreenhousePasswordEncoder("secret2");
        String password = TEST_PASSWORD;

        // When
        String encodedPassword1 = encoder1.encode(password);
        String encodedPassword2 = encoder2.encode(password);

        // Then
        assertTrue("First encoder should match its own encoding",
                encoder1.matches(password, encodedPassword1));
        assertTrue("Second encoder should match its own encoding",
                encoder2.matches(password, encodedPassword2));
        assertFalse("First encoder should not match second encoder's encoding",
                encoder1.matches(password, encodedPassword2));
        assertFalse("Second encoder should not match first encoder's encoding",
                encoder2.matches(password, encodedPassword1));
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEdgeCase_ShouldHandleVeryShortPassword() {
        // Given
        String shortPassword = "a";

        // When
        String encodedPassword = encoder.encode(shortPassword);
        boolean matches = encoder.matches(shortPassword, encodedPassword);

        // Then
        assertTrue("Very short password should work", matches);
    }

    @Test
    public void testEdgeCase_ShouldHandlePasswordWithOnlySpaces() {
        // Given
        String spacePassword = "   ";

        // When
        String encodedPassword = encoder.encode(spacePassword);
        boolean matches = encoder.matches(spacePassword, encodedPassword);

        // Then
        assertTrue("Password with only spaces should work", matches);
    }

    @Test
    public void testEdgeCase_ShouldHandlePasswordWithNewlines() {
        // Given
        String newlinePassword = "password\nwith\nnewlines";

        // When
        String encodedPassword = encoder.encode(newlinePassword);
        boolean matches = encoder.matches(newlinePassword, encodedPassword);

        // Then
        assertTrue("Password with newlines should work", matches);
    }

    // ==================== Performance Tests ====================

    @Test
    public void testPerformance_ShouldEncodePasswordWithinReasonableTime() {
        // Given
        String password = TEST_PASSWORD;
        long startTime = System.currentTimeMillis();

        // When
        String encodedPassword = encoder.encode(password);

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull("Encoded password should not be null", encodedPassword);
        assertTrue("Encoding should complete within reasonable time (1000ms)", duration < 1000);
    }

    @Test
    public void testPerformance_ShouldMatchPasswordWithinReasonableTime() {
        // Given
        String password = TEST_PASSWORD;
        String encodedPassword = encoder.encode(password);
        long startTime = System.currentTimeMillis();

        // When
        boolean matches = encoder.matches(password, encodedPassword);

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue("Password should match", matches);
        assertTrue("Matching should complete within reasonable time (1000ms)", duration < 1000);
    }
}
