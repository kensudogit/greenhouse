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

import org.junit.Test;

/**
 * Test class for {@link Message}.
 * 
 * @author Test Author
 */
public class MessageTest {

    // Test data constants
    private static final String TEST_TEXT = "This is a test message";
    private static final String TEST_TEXT_2 = "Another test message";
    private static final String EMPTY_TEXT = "";
    private static final String NULL_TEXT = null;

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateMessage_WhenValidParametersProvided() {
        // When
        Message message = new Message(MessageType.INFO, TEST_TEXT);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should match", MessageType.INFO, message.getType());
        assertEquals("Message text should match", TEST_TEXT, message.getText());
    }

    @Test
    public void testConstructor_ShouldCreateMessage_WhenNullTextProvided() {
        // When
        Message message = new Message(MessageType.ERROR, null);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should match", MessageType.ERROR, message.getType());
        assertNull("Message text should be null", message.getText());
    }

    @Test
    public void testConstructor_ShouldCreateMessage_WhenEmptyTextProvided() {
        // When
        Message message = new Message(MessageType.WARNING, "");

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should match", MessageType.WARNING, message.getType());
        assertEquals("Message text should be empty", "", message.getText());
    }

    // ==================== Factory Method Tests ====================

    @Test
    public void testSuccess_ShouldCreateSuccessMessage() {
        // When
        Message message = Message.success(TEST_TEXT);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be SUCCESS", MessageType.SUCCESS, message.getType());
        assertEquals("Message text should match", TEST_TEXT, message.getText());
    }

    @Test
    public void testInfo_ShouldCreateInfoMessage() {
        // When
        Message message = Message.info(TEST_TEXT);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be INFO", MessageType.INFO, message.getType());
        assertEquals("Message text should match", TEST_TEXT, message.getText());
    }

    @Test
    public void testWarning_ShouldCreateWarningMessage() {
        // When
        Message message = Message.warning(TEST_TEXT);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be WARNING", MessageType.WARNING, message.getType());
        assertEquals("Message text should match", TEST_TEXT, message.getText());
    }

    @Test
    public void testError_ShouldCreateErrorMessage() {
        // When
        Message message = Message.error(TEST_TEXT);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be ERROR", MessageType.ERROR, message.getType());
        assertEquals("Message text should match", TEST_TEXT, message.getText());
    }

    @Test
    public void testSuccess_ShouldHandleNullText() {
        // When
        Message message = Message.success(null);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be SUCCESS", MessageType.SUCCESS, message.getType());
        assertNull("Message text should be null", message.getText());
    }

    @Test
    public void testInfo_ShouldHandleNullText() {
        // When
        Message message = Message.info(null);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be INFO", MessageType.INFO, message.getType());
        assertNull("Message text should be null", message.getText());
    }

    @Test
    public void testWarning_ShouldHandleNullText() {
        // When
        Message message = Message.warning(null);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be WARNING", MessageType.WARNING, message.getType());
        assertNull("Message text should be null", message.getText());
    }

    @Test
    public void testError_ShouldHandleNullText() {
        // When
        Message message = Message.error(null);

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be ERROR", MessageType.ERROR, message.getType());
        assertNull("Message text should be null", message.getText());
    }

    @Test
    public void testSuccess_ShouldHandleEmptyText() {
        // When
        Message message = Message.success("");

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be SUCCESS", MessageType.SUCCESS, message.getType());
        assertEquals("Message text should be empty", "", message.getText());
    }

    @Test
    public void testInfo_ShouldHandleEmptyText() {
        // When
        Message message = Message.info("");

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be INFO", MessageType.INFO, message.getType());
        assertEquals("Message text should be empty", "", message.getText());
    }

    @Test
    public void testWarning_ShouldHandleEmptyText() {
        // When
        Message message = Message.warning("");

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be WARNING", MessageType.WARNING, message.getType());
        assertEquals("Message text should be empty", "", message.getText());
    }

    @Test
    public void testError_ShouldHandleEmptyText() {
        // When
        Message message = Message.error("");

        // Then
        assertNotNull("Message should not be null", message);
        assertEquals("Message type should be ERROR", MessageType.ERROR, message.getType());
        assertEquals("Message text should be empty", "", message.getText());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetType_ShouldReturnCorrectType() {
        // Given
        Message message = new Message(MessageType.SUCCESS, TEST_TEXT);

        // When
        MessageType type = message.getType();

        // Then
        assertEquals("Message type should match", MessageType.SUCCESS, type);
    }

    @Test
    public void testGetText_ShouldReturnCorrectText() {
        // Given
        Message message = new Message(MessageType.INFO, TEST_TEXT);

        // When
        String text = message.getText();

        // Then
        assertEquals("Message text should match", TEST_TEXT, text);
    }

    @Test
    public void testGetType_ShouldReturnCorrectType_ForAllMessageTypes() {
        // Test all message types
        Message successMessage = new Message(MessageType.SUCCESS, TEST_TEXT);
        Message infoMessage = new Message(MessageType.INFO, TEST_TEXT);
        Message warningMessage = new Message(MessageType.WARNING, TEST_TEXT);
        Message errorMessage = new Message(MessageType.ERROR, TEST_TEXT);

        assertEquals("Success message type should be SUCCESS", MessageType.SUCCESS, successMessage.getType());
        assertEquals("Info message type should be INFO", MessageType.INFO, infoMessage.getType());
        assertEquals("Warning message type should be WARNING", MessageType.WARNING, warningMessage.getType());
        assertEquals("Error message type should be ERROR", MessageType.ERROR, errorMessage.getType());
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldReturnCorrectFormat() {
        // Given
        Message message = new Message(MessageType.INFO, TEST_TEXT);

        // When
        String result = message.toString();

        // Then
        assertEquals("ToString should return correct format", "INFO: " + TEST_TEXT, result);
    }

    @Test
    public void testToString_ShouldHandleNullText() {
        // Given
        Message message = new Message(MessageType.ERROR, null);

        // When
        String result = message.toString();

        // Then
        assertEquals("ToString should handle null text", "ERROR: null", result);
    }

    @Test
    public void testToString_ShouldHandleEmptyText() {
        // Given
        Message message = new Message(MessageType.WARNING, "");

        // When
        String result = message.toString();

        // Then
        assertEquals("ToString should handle empty text", "WARNING: ", result);
    }

    @Test
    public void testToString_ShouldReturnCorrectFormat_ForAllMessageTypes() {
        // Test all message types
        Message successMessage = new Message(MessageType.SUCCESS, TEST_TEXT);
        Message infoMessage = new Message(MessageType.INFO, TEST_TEXT);
        Message warningMessage = new Message(MessageType.WARNING, TEST_TEXT);
        Message errorMessage = new Message(MessageType.ERROR, TEST_TEXT);

        assertEquals("Success message toString should be correct", "SUCCESS: " + TEST_TEXT, successMessage.toString());
        assertEquals("Info message toString should be correct", "INFO: " + TEST_TEXT, infoMessage.toString());
        assertEquals("Warning message toString should be correct", "WARNING: " + TEST_TEXT, warningMessage.toString());
        assertEquals("Error message toString should be correct", "ERROR: " + TEST_TEXT, errorMessage.toString());
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testConstructor_ShouldHandleSpecialCharactersInText() {
        // Given
        String specialText = "Message with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        Message message = new Message(MessageType.INFO, specialText);

        // When
        String result = message.getText();

        // Then
        assertEquals("Message should handle special characters", specialText, result);
    }

    @Test
    public void testConstructor_ShouldHandleUnicodeCharactersInText() {
        // Given
        String unicodeText = "Message with unicode: こんにちは世界";
        Message message = new Message(MessageType.INFO, unicodeText);

        // When
        String result = message.getText();

        // Then
        assertEquals("Message should handle unicode characters", unicodeText, result);
    }

    @Test
    public void testConstructor_ShouldHandleVeryLongText() {
        // Given
        String longText = "A".repeat(1000);
        Message message = new Message(MessageType.INFO, longText);

        // When
        String result = message.getText();

        // Then
        assertEquals("Message should handle very long text", longText, result);
    }

    @Test
    public void testConstructor_ShouldHandleWhitespaceOnlyText() {
        // Given
        String whitespaceText = "   \t\n\r   ";
        Message message = new Message(MessageType.INFO, whitespaceText);

        // When
        String result = message.getText();

        // Then
        assertEquals("Message should handle whitespace-only text", whitespaceText, result);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testMessageShouldBeImmutable() {
        // Given
        Message message = new Message(MessageType.INFO, TEST_TEXT);
        MessageType originalType = message.getType();
        String originalText = message.getText();

        // When - Create another message with different content
        Message anotherMessage = new Message(MessageType.ERROR, TEST_TEXT_2);

        // Then - Original message should remain unchanged
        assertEquals("Original message type should remain unchanged", originalType, message.getType());
        assertEquals("Original message text should remain unchanged", originalText, message.getText());
        assertNotEquals("New message should have different type", message.getType(), anotherMessage.getType());
        assertNotEquals("New message should have different text", message.getText(), anotherMessage.getText());
    }

    // ==================== Factory Method Comparison Tests ====================

    @Test
    public void testFactoryMethods_ShouldCreateDifferentMessages() {
        // When
        Message successMessage = Message.success(TEST_TEXT);
        Message infoMessage = Message.info(TEST_TEXT);
        Message warningMessage = Message.warning(TEST_TEXT);
        Message errorMessage = Message.error(TEST_TEXT);

        // Then
        assertNotEquals("Success and info messages should be different", successMessage.getType(),
                infoMessage.getType());
        assertNotEquals("Info and warning messages should be different", infoMessage.getType(),
                warningMessage.getType());
        assertNotEquals("Warning and error messages should be different", warningMessage.getType(),
                errorMessage.getType());
        assertNotEquals("Success and error messages should be different", successMessage.getType(),
                errorMessage.getType());

        // But all should have the same text
        assertEquals("All messages should have the same text", TEST_TEXT, successMessage.getText());
        assertEquals("All messages should have the same text", TEST_TEXT, infoMessage.getText());
        assertEquals("All messages should have the same text", TEST_TEXT, warningMessage.getText());
        assertEquals("All messages should have the same text", TEST_TEXT, errorMessage.getText());
    }
}