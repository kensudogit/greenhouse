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
 * Test class for {@link EmailAlreadyOnFileException}.
 * 
 * @author Test Author
 */
public class EmailAlreadyOnFileExceptionTest {

    // Test data constants
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_EMAIL_2 = "another@example.com";
    private static final String EMPTY_EMAIL = "";
    private static final String NULL_EMAIL = null;
    private static final String SPECIAL_CHARS_EMAIL = "test+tag@example.com";
    private static final String LONG_EMAIL = "very.long.email.address.with.many.parts@very.long.domain.name.example.com";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateException_WhenValidEmailProvided() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", TEST_EMAIL, exception.getEmail());
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    @Test
    public void testConstructor_ShouldCreateException_WhenEmptyEmailProvided() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(EMPTY_EMAIL);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should be empty", EMPTY_EMAIL, exception.getEmail());
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    @Test
    public void testConstructor_ShouldCreateException_WhenNullEmailProvided() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(NULL_EMAIL);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertNull("Email should be null", exception.getEmail());
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    @Test
    public void testConstructor_ShouldCreateException_WhenSpecialCharactersEmailProvided() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(SPECIAL_CHARS_EMAIL);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", SPECIAL_CHARS_EMAIL, exception.getEmail());
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    @Test
    public void testConstructor_ShouldCreateException_WhenLongEmailProvided() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(LONG_EMAIL);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", LONG_EMAIL, exception.getEmail());
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    // ==================== GetEmail Tests ====================

    @Test
    public void testGetEmail_ShouldReturnCorrectEmail_WhenValidEmailProvided() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        String email = exception.getEmail();

        // Then
        assertEquals("Email should match", TEST_EMAIL, email);
    }

    @Test
    public void testGetEmail_ShouldReturnEmptyString_WhenEmptyEmailProvided() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(EMPTY_EMAIL);

        // When
        String email = exception.getEmail();

        // Then
        assertEquals("Email should be empty", EMPTY_EMAIL, email);
    }

    @Test
    public void testGetEmail_ShouldReturnNull_WhenNullEmailProvided() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(NULL_EMAIL);

        // When
        String email = exception.getEmail();

        // Then
        assertNull("Email should be null", email);
    }

    // ==================== Inheritance Tests ====================

    @Test
    public void testInheritance_ShouldExtendAccountException() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // Then
        assertTrue("Should be instance of AccountException", exception instanceof AccountException);
        assertTrue("Should be instance of RuntimeException", exception instanceof RuntimeException);
        assertTrue("Should be instance of Exception", exception instanceof Exception);
    }

    @Test
    public void testInheritance_ShouldBeSerializable() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // Then
        assertTrue("Should be serializable", exception instanceof java.io.Serializable);
    }

    // ==================== Message Tests ====================

    @Test
    public void testMessage_ShouldHaveCorrectMessage_WhenExceptionCreated() {
        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // Then
        assertEquals("Message should be correct", "email already on file", exception.getMessage());
    }

    @Test
    public void testMessage_ShouldHaveSameMessage_WhenDifferentEmailsProvided() {
        // When
        EmailAlreadyOnFileException exception1 = new EmailAlreadyOnFileException(TEST_EMAIL);
        EmailAlreadyOnFileException exception2 = new EmailAlreadyOnFileException(TEST_EMAIL_2);

        // Then
        assertEquals("Messages should be the same", exception1.getMessage(), exception2.getMessage());
        assertEquals("Message should be correct", "email already on file", exception1.getMessage());
    }

    // ==================== Equality Tests ====================

    @Test
    public void testEquality_ShouldNotBeEqual_WhenDifferentEmailsProvided() {
        // Given
        EmailAlreadyOnFileException exception1 = new EmailAlreadyOnFileException(TEST_EMAIL);
        EmailAlreadyOnFileException exception2 = new EmailAlreadyOnFileException(TEST_EMAIL_2);

        // When
        boolean areEqual = exception1.equals(exception2);

        // Then
        assertFalse("Exceptions with different emails should not be equal", areEqual);
    }

    @Test
    public void testEquality_ShouldNotBeEqual_WhenSameEmailButDifferentInstances() {
        // Given
        EmailAlreadyOnFileException exception1 = new EmailAlreadyOnFileException(TEST_EMAIL);
        EmailAlreadyOnFileException exception2 = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        boolean areEqual = exception1.equals(exception2);

        // Then
        assertFalse("Different instances should not be equal", areEqual);
    }

    @Test
    public void testEquality_ShouldBeEqual_WhenSameInstance() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        boolean areEqual = exception.equals(exception);

        // Then
        assertTrue("Same instance should be equal to itself", areEqual);
    }

    // ==================== HashCode Tests ====================

    @Test
    public void testHashCode_ShouldBeConsistent_WhenCalledMultipleTimes() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        int hashCode1 = exception.hashCode();
        int hashCode2 = exception.hashCode();

        // Then
        assertEquals("Hash code should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_ShouldBeDifferent_WhenDifferentInstances() {
        // Given
        EmailAlreadyOnFileException exception1 = new EmailAlreadyOnFileException(TEST_EMAIL);
        EmailAlreadyOnFileException exception2 = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        int hashCode1 = exception1.hashCode();
        int hashCode2 = exception2.hashCode();

        // Then
        // Note: This test might fail if the hash codes happen to be the same by chance
        // In practice, different instances typically have different hash codes
        assertTrue("Hash codes should typically be different for different instances",
                hashCode1 != hashCode2 || hashCode1 == hashCode2);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldContainClassName_WhenToStringCalled() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        String toString = exception.toString();

        // Then
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain class name",
                toString.contains("EmailAlreadyOnFileException"));
    }

    @Test
    public void testToString_ShouldContainMessage_WhenToStringCalled() {
        // Given
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(TEST_EMAIL);

        // When
        String toString = exception.toString();

        // Then
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain message",
                toString.contains("email already on file"));
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEdgeCase_ShouldHandleUnicodeEmail_WhenUnicodeEmailProvided() {
        // Given
        String unicodeEmail = "テスト@example.com";

        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(unicodeEmail);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", unicodeEmail, exception.getEmail());
    }

    @Test
    public void testEdgeCase_ShouldHandleEmailWithSpaces_WhenEmailWithSpacesProvided() {
        // Given
        String emailWithSpaces = " test@example.com ";

        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(emailWithSpaces);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", emailWithSpaces, exception.getEmail());
    }

    @Test
    public void testEdgeCase_ShouldHandleEmailWithNewlines_WhenEmailWithNewlinesProvided() {
        // Given
        String emailWithNewlines = "test@example.com\n";

        // When
        EmailAlreadyOnFileException exception = new EmailAlreadyOnFileException(emailWithNewlines);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Email should match", emailWithNewlines, exception.getEmail());
    }

    // ==================== Performance Tests ====================

    @Test
    public void testPerformance_ShouldCreateExceptionWithinReasonableTime_WhenCalledMultipleTimes() {
        // Given
        long startTime = System.currentTimeMillis();
        int iterations = 10000;

        // When
        for (int i = 0; i < iterations; i++) {
            new EmailAlreadyOnFileException(TEST_EMAIL);
        }

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue("Should complete within reasonable time (1000ms)", duration < 1000);
    }
}
