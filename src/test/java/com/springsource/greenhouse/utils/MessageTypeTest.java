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
 * Test class for {@link MessageType}.
 * 
 * @author Test Author
 */
public class MessageTypeTest {

    // ==================== Enum Values Tests ====================

    @Test
    public void testEnumValues_ShouldContainAllMessageTypes() {
        // When
        MessageType[] values = MessageType.values();

        // Then
        assertEquals("Should have exactly 4 enum values", 4, values.length);
        assertTrue("Should contain INFO", contains(values, MessageType.INFO));
        assertTrue("Should contain SUCCESS", contains(values, MessageType.SUCCESS));
        assertTrue("Should contain WARNING", contains(values, MessageType.WARNING));
        assertTrue("Should contain ERROR", contains(values, MessageType.ERROR));
    }

    @Test
    public void testEnumValues_ShouldBeInCorrectOrder() {
        // When
        MessageType[] values = MessageType.values();

        // Then
        assertEquals("First value should be INFO", MessageType.INFO, values[0]);
        assertEquals("Second value should be SUCCESS", MessageType.SUCCESS, values[1]);
        assertEquals("Third value should be WARNING", MessageType.WARNING, values[2]);
        assertEquals("Fourth value should be ERROR", MessageType.ERROR, values[3]);
    }

    // ==================== Individual Enum Value Tests ====================

    @Test
    public void testInfo_ShouldExist() {
        // When
        MessageType info = MessageType.INFO;

        // Then
        assertNotNull("INFO should not be null", info);
        assertEquals("INFO should equal itself", MessageType.INFO, info);
    }

    @Test
    public void testSuccess_ShouldExist() {
        // When
        MessageType success = MessageType.SUCCESS;

        // Then
        assertNotNull("SUCCESS should not be null", success);
        assertEquals("SUCCESS should equal itself", MessageType.SUCCESS, success);
    }

    @Test
    public void testWarning_ShouldExist() {
        // When
        MessageType warning = MessageType.WARNING;

        // Then
        assertNotNull("WARNING should not be null", warning);
        assertEquals("WARNING should equal itself", MessageType.WARNING, warning);
    }

    @Test
    public void testError_ShouldExist() {
        // When
        MessageType error = MessageType.ERROR;

        // Then
        assertNotNull("ERROR should not be null", error);
        assertEquals("ERROR should equal itself", MessageType.ERROR, error);
    }

    // ==================== Get Css Class Tests ====================

    @Test
    public void testGetCssClass_ShouldReturnCorrectClass_ForInfo() {
        // When
        String cssClass = MessageType.INFO.getCssClass();

        // Then
        assertEquals("INFO should have css class 'info'", "info", cssClass);
    }

    @Test
    public void testGetCssClass_ShouldReturnCorrectClass_ForSuccess() {
        // When
        String cssClass = MessageType.SUCCESS.getCssClass();

        // Then
        assertEquals("SUCCESS should have css class 'success'", "success", cssClass);
    }

    @Test
    public void testGetCssClass_ShouldReturnCorrectClass_ForWarning() {
        // When
        String cssClass = MessageType.WARNING.getCssClass();

        // Then
        assertEquals("WARNING should have css class 'warning'", "warning", cssClass);
    }

    @Test
    public void testGetCssClass_ShouldReturnCorrectClass_ForError() {
        // When
        String cssClass = MessageType.ERROR.getCssClass();

        // Then
        assertEquals("ERROR should have css class 'error'", "error", cssClass);
    }

    @Test
    public void testGetCssClass_ShouldReturnLowerCaseName() {
        // Test all message types
        assertEquals("INFO css class should be lowercase", "info", MessageType.INFO.getCssClass());
        assertEquals("SUCCESS css class should be lowercase", "success", MessageType.SUCCESS.getCssClass());
        assertEquals("WARNING css class should be lowercase", "warning", MessageType.WARNING.getCssClass());
        assertEquals("ERROR css class should be lowercase", "error", MessageType.ERROR.getCssClass());
    }

    // ==================== ValueOf Tests ====================

    @Test
    public void testValueOf_ShouldReturnCorrectEnum_WhenValidStringProvided() {
        // When
        MessageType info = MessageType.valueOf("INFO");
        MessageType success = MessageType.valueOf("SUCCESS");
        MessageType warning = MessageType.valueOf("WARNING");
        MessageType error = MessageType.valueOf("ERROR");

        // Then
        assertEquals("Should return INFO for 'INFO'", MessageType.INFO, info);
        assertEquals("Should return SUCCESS for 'SUCCESS'", MessageType.SUCCESS, success);
        assertEquals("Should return WARNING for 'WARNING'", MessageType.WARNING, warning);
        assertEquals("Should return ERROR for 'ERROR'", MessageType.ERROR, error);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenInvalidStringProvided() {
        // When
        MessageType.valueOf("INVALID");

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenNullStringProvided() {
        // When
        MessageType.valueOf(null);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenEmptyStringProvided() {
        // When
        MessageType.valueOf("");

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_ShouldThrowException_WhenLowerCaseStringProvided() {
        // When
        MessageType.valueOf("info");

        // Then - Exception should be thrown
    }

    // ==================== Enum Equality Tests ====================

    @Test
    public void testEnumEquality_ShouldWorkCorrectly() {
        // Given
        MessageType info1 = MessageType.INFO;
        MessageType info2 = MessageType.INFO;
        MessageType success = MessageType.SUCCESS;

        // Then
        assertSame("Same enum instances should be identical", info1, info2);
        assertEquals("Same enum values should be equal", info1, info2);
        assertNotSame("Different enum values should not be identical", info1, success);
        assertNotEquals("Different enum values should not be equal", info1, success);
    }

    @Test
    public void testEnumComparison_ShouldWorkCorrectly() {
        // Given
        MessageType info = MessageType.INFO;
        MessageType success = MessageType.SUCCESS;
        MessageType warning = MessageType.WARNING;
        MessageType error = MessageType.ERROR;

        // Then
        assertTrue("INFO should equal INFO", info.equals(info));
        assertTrue("SUCCESS should equal SUCCESS", success.equals(success));
        assertTrue("WARNING should equal WARNING", warning.equals(warning));
        assertTrue("ERROR should equal ERROR", error.equals(error));
        assertTrue("INFO should not equal SUCCESS", !info.equals(success));
        assertTrue("SUCCESS should not equal WARNING", !success.equals(warning));
        assertTrue("WARNING should not equal ERROR", !warning.equals(error));
    }

    // ==================== Ordinal Tests ====================

    @Test
    public void testOrdinal_ShouldReturnCorrectValues() {
        // When
        int infoOrdinal = MessageType.INFO.ordinal();
        int successOrdinal = MessageType.SUCCESS.ordinal();
        int warningOrdinal = MessageType.WARNING.ordinal();
        int errorOrdinal = MessageType.ERROR.ordinal();

        // Then
        assertEquals("INFO should have ordinal 0", 0, infoOrdinal);
        assertEquals("SUCCESS should have ordinal 1", 1, successOrdinal);
        assertEquals("WARNING should have ordinal 2", 2, warningOrdinal);
        assertEquals("ERROR should have ordinal 3", 3, errorOrdinal);
    }

    // ==================== Name Tests ====================

    @Test
    public void testName_ShouldReturnCorrectStrings() {
        // When
        String infoName = MessageType.INFO.name();
        String successName = MessageType.SUCCESS.name();
        String warningName = MessageType.WARNING.name();
        String errorName = MessageType.ERROR.name();

        // Then
        assertEquals("INFO should have name 'INFO'", "INFO", infoName);
        assertEquals("SUCCESS should have name 'SUCCESS'", "SUCCESS", successName);
        assertEquals("WARNING should have name 'WARNING'", "WARNING", warningName);
        assertEquals("ERROR should have name 'ERROR'", "ERROR", errorName);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldReturnCorrectStrings() {
        // When
        String infoString = MessageType.INFO.toString();
        String successString = MessageType.SUCCESS.toString();
        String warningString = MessageType.WARNING.toString();
        String errorString = MessageType.ERROR.toString();

        // Then
        assertEquals("INFO toString should be 'INFO'", "INFO", infoString);
        assertEquals("SUCCESS toString should be 'SUCCESS'", "SUCCESS", successString);
        assertEquals("WARNING toString should be 'WARNING'", "WARNING", warningString);
        assertEquals("ERROR toString should be 'ERROR'", "ERROR", errorString);
    }

    // ==================== Switch Statement Tests ====================

    @Test
    public void testSwitchStatement_ShouldWorkWithAllValues() {
        // Test INFO
        String infoResult = getMessageTypeDescription(MessageType.INFO);
        assertEquals("INFO should return 'informative'", "informative", infoResult);

        // Test SUCCESS
        String successResult = getMessageTypeDescription(MessageType.SUCCESS);
        assertEquals("SUCCESS should return 'successful'", "successful", successResult);

        // Test WARNING
        String warningResult = getMessageTypeDescription(MessageType.WARNING);
        assertEquals("WARNING should return 'warning'", "warning", warningResult);

        // Test ERROR
        String errorResult = getMessageTypeDescription(MessageType.ERROR);
        assertEquals("ERROR should return 'error'", "error", errorResult);
    }

    // ==================== Array Operations Tests ====================

    @Test
    public void testArrayOperations_ShouldWorkCorrectly() {
        // Given
        MessageType[] types = MessageType.values();

        // When
        MessageType firstType = types[0];
        MessageType lastType = types[types.length - 1];

        // Then
        assertEquals("First type should be INFO", MessageType.INFO, firstType);
        assertEquals("Last type should be ERROR", MessageType.ERROR, lastType);
        assertEquals("Array length should be 4", 4, types.length);
    }

    // ==================== Css Class Consistency Tests ====================

    @Test
    public void testCssClassConsistency_ShouldBeConsistent() {
        // Test that css class is always lowercase version of enum name
        for (MessageType type : MessageType.values()) {
            String expectedCssClass = type.name().toLowerCase();
            assertEquals("CSS class should be lowercase version of enum name",
                    expectedCssClass, type.getCssClass());
        }
    }

    // ==================== Helper Methods ====================

    private boolean contains(MessageType[] values, MessageType target) {
        for (MessageType value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    private String getMessageTypeDescription(MessageType type) {
        switch (type) {
            case INFO:
                return "informative";
            case SUCCESS:
                return "successful";
            case WARNING:
                return "warning";
            case ERROR:
                return "error";
            default:
                return "unknown";
        }
    }
}