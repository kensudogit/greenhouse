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
 * Test class for {@link ResourceReference}.
 * 
 * @author Test Author
 */
public class ResourceReferenceTest {

    // Test data constants
    private static final Long TEST_ID = 1L;
    private static final String TEST_LABEL = "Test Resource";
    private static final String TEST_LABEL_2 = "Another Test Resource";
    private static final Integer TEST_INT_ID = 42;
    private static final String TEST_STRING_ID = "resource-123";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateResourceReference_WhenValidParametersProvided() {
        // When
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, TEST_LABEL);

        // Then
        assertNotNull("ResourceReference should not be null", reference);
        assertEquals("ID should match", TEST_ID, reference.getId());
        assertEquals("Label should match", TEST_LABEL, reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateResourceReference_WhenNullValuesProvided() {
        // When
        ResourceReference<Long> reference = new ResourceReference<>(null, null);

        // Then
        assertNotNull("ResourceReference should not be null", reference);
        assertNull("ID should be null", reference.getId());
        assertNull("Label should be null", reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateResourceReference_WhenEmptyLabelProvided() {
        // When
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, "");

        // Then
        assertNotNull("ResourceReference should not be null", reference);
        assertEquals("ID should match", TEST_ID, reference.getId());
        assertEquals("Label should be empty", "", reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateResourceReference_WithDifferentIdTypes() {
        // Test with Integer ID
        ResourceReference<Integer> intReference = new ResourceReference<>(TEST_INT_ID, TEST_LABEL);
        assertEquals("Integer ID should match", TEST_INT_ID, intReference.getId());

        // Test with String ID
        ResourceReference<String> stringReference = new ResourceReference<>(TEST_STRING_ID, TEST_LABEL);
        assertEquals("String ID should match", TEST_STRING_ID, stringReference.getId());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetId_ShouldReturnCorrectId() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("ID should match", TEST_ID, id);
    }

    @Test
    public void testGetLabel_ShouldReturnCorrectLabel() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, TEST_LABEL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should match", TEST_LABEL, label);
    }

    @Test
    public void testGetId_ShouldReturnNull_WhenNullIdProvided() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(null, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertNull("ID should be null", id);
    }

    @Test
    public void testGetLabel_ShouldReturnNull_WhenNullLabelProvided() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, null);

        // When
        String label = reference.getLabel();

        // Then
        assertNull("Label should be null", label);
    }

    @Test
    public void testGetLabel_ShouldReturnEmptyString_WhenEmptyLabelProvided() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, "");

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should be empty string", "", label);
    }

    // ==================== Different ID Types Tests ====================

    @Test
    public void testResourceReference_ShouldWorkWithLongId() {
        // Given
        Long longId = 12345L;
        ResourceReference<Long> reference = new ResourceReference<>(longId, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Long ID should match", longId, id);
    }

    @Test
    public void testResourceReference_ShouldWorkWithIntegerId() {
        // Given
        Integer intId = 12345;
        ResourceReference<Integer> reference = new ResourceReference<>(intId, TEST_LABEL);

        // When
        Integer id = reference.getId();

        // Then
        assertEquals("Integer ID should match", intId, id);
    }

    @Test
    public void testResourceReference_ShouldWorkWithStringId() {
        // Given
        String stringId = "resource-12345";
        ResourceReference<String> reference = new ResourceReference<>(stringId, TEST_LABEL);

        // When
        String id = reference.getId();

        // Then
        assertEquals("String ID should match", stringId, id);
    }

    @Test
    public void testResourceReference_ShouldWorkWithDoubleId() {
        // Given
        Double doubleId = 123.45;
        ResourceReference<Double> reference = new ResourceReference<>(doubleId, TEST_LABEL);

        // When
        Double id = reference.getId();

        // Then
        assertEquals("Double ID should match", doubleId, id);
    }

    // ==================== Label Content Tests ====================

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialLabel = "Resource with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, specialLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle special characters", specialLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenUnicodeCharactersProvided() {
        // Given
        String unicodeLabel = "Resource with unicode: こんにちは世界";
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, unicodeLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle unicode characters", unicodeLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenVeryLongLabelProvided() {
        // Given
        String longLabel = "A".repeat(1000);
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, longLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle very long text", longLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenWhitespaceOnlyLabelProvided() {
        // Given
        String whitespaceLabel = "   \t\n\r   ";
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, whitespaceLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle whitespace-only text", whitespaceLabel, label);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testResourceReference_ShouldHandleZeroId() {
        // Given
        Long zeroId = 0L;
        ResourceReference<Long> reference = new ResourceReference<>(zeroId, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Zero ID should be handled correctly", zeroId, id);
    }

    @Test
    public void testResourceReference_ShouldHandleNegativeId() {
        // Given
        Long negativeId = -1L;
        ResourceReference<Long> reference = new ResourceReference<>(negativeId, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Negative ID should be handled correctly", negativeId, id);
    }

    @Test
    public void testResourceReference_ShouldHandleVeryLargeId() {
        // Given
        Long largeId = Long.MAX_VALUE;
        ResourceReference<Long> reference = new ResourceReference<>(largeId, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Very large ID should be handled correctly", largeId, id);
    }

    @Test
    public void testResourceReference_ShouldHandleVerySmallId() {
        // Given
        Long smallId = Long.MIN_VALUE;
        ResourceReference<Long> reference = new ResourceReference<>(smallId, TEST_LABEL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Very small ID should be handled correctly", smallId, id);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testResourceReferenceShouldBeImmutable() {
        // Given
        ResourceReference<Long> reference = new ResourceReference<>(TEST_ID, TEST_LABEL);
        Long originalId = reference.getId();
        String originalLabel = reference.getLabel();

        // When - Create another reference with different content
        ResourceReference<Long> anotherReference = new ResourceReference<>(TEST_ID + 1, TEST_LABEL_2);

        // Then - Original reference should remain unchanged
        assertEquals("Original ID should remain unchanged", originalId, reference.getId());
        assertEquals("Original label should remain unchanged", originalLabel, reference.getLabel());
        assertNotEquals("New reference should have different ID", reference.getId(), anotherReference.getId());
        assertNotEquals("New reference should have different label", reference.getLabel(), anotherReference.getLabel());
    }

    // ==================== Type Safety Tests ====================

    @Test
    public void testTypeSafety_ShouldWorkCorrectly() {
        // Given
        ResourceReference<Long> longReference = new ResourceReference<>(TEST_ID, TEST_LABEL);
        ResourceReference<String> stringReference = new ResourceReference<>(TEST_STRING_ID, TEST_LABEL);

        // When
        Long longId = longReference.getId();
        String stringId = stringReference.getId();

        // Then
        assertTrue("Long ID should be instance of Long", longId instanceof Long);
        assertTrue("String ID should be instance of String", stringId instanceof String);
        assertFalse("Long ID should not be instance of String", longId instanceof String);
        assertFalse("String ID should not be instance of Long", stringId instanceof Long);
    }

    // ==================== Multiple Instances Tests ====================

    @Test
    public void testMultipleInstances_ShouldBeIndependent() {
        // Given
        ResourceReference<Long> reference1 = new ResourceReference<>(TEST_ID, TEST_LABEL);
        ResourceReference<Long> reference2 = new ResourceReference<>(TEST_ID + 1, TEST_LABEL_2);

        // When
        Long id1 = reference1.getId();
        String label1 = reference1.getLabel();
        Long id2 = reference2.getId();
        String label2 = reference2.getLabel();

        // Then
        assertNotEquals("IDs should be different", id1, id2);
        assertNotEquals("Labels should be different", label1, label2);
    }
}