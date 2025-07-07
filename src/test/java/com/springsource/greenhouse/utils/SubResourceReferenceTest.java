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
 * Test class for {@link SubResourceReference}.
 * 
 * @author Test Author
 */
public class SubResourceReferenceTest {

    // Test data constants
    private static final Long TEST_PARENT_ID = 100L;
    private static final Long TEST_CHILD_ID = 1L;
    private static final String TEST_LABEL = "Test Sub Resource";
    private static final String TEST_LABEL_2 = "Another Test Sub Resource";
    private static final Integer TEST_INT_PARENT_ID = 42;
    private static final String TEST_STRING_CHILD_ID = "child-123";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateSubResourceReference_WhenValidParametersProvided() {
        // When
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // Then
        assertNotNull("SubResourceReference should not be null", reference);
        assertEquals("Parent ID should match", TEST_PARENT_ID, reference.getParentId());
        assertEquals("Child ID should match", TEST_CHILD_ID, reference.getId());
        assertEquals("Label should match", TEST_LABEL, reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateSubResourceReference_WhenNullValuesProvided() {
        // When
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(null, null, null);

        // Then
        assertNotNull("SubResourceReference should not be null", reference);
        assertNull("Parent ID should be null", reference.getParentId());
        assertNull("Child ID should be null", reference.getId());
        assertNull("Label should be null", reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateSubResourceReference_WhenEmptyLabelProvided() {
        // When
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID, "");

        // Then
        assertNotNull("SubResourceReference should not be null", reference);
        assertEquals("Parent ID should match", TEST_PARENT_ID, reference.getParentId());
        assertEquals("Child ID should match", TEST_CHILD_ID, reference.getId());
        assertEquals("Label should be empty", "", reference.getLabel());
    }

    @Test
    public void testConstructor_ShouldCreateSubResourceReference_WithDifferentIdTypes() {
        // Test with Integer parent ID and String child ID
        SubResourceReference<Integer, String> reference = new SubResourceReference<>(TEST_INT_PARENT_ID,
                TEST_STRING_CHILD_ID, TEST_LABEL);
        assertEquals("Integer parent ID should match", TEST_INT_PARENT_ID, reference.getParentId());
        assertEquals("String child ID should match", TEST_STRING_CHILD_ID, reference.getId());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetParentId_ShouldReturnCorrectParentId() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // When
        Long parentId = reference.getParentId();

        // Then
        assertEquals("Parent ID should match", TEST_PARENT_ID, parentId);
    }

    @Test
    public void testGetId_ShouldReturnCorrectChildId() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // When
        Long childId = reference.getId();

        // Then
        assertEquals("Child ID should match", TEST_CHILD_ID, childId);
    }

    @Test
    public void testGetLabel_ShouldReturnCorrectLabel() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should match", TEST_LABEL, label);
    }

    @Test
    public void testGetParentId_ShouldReturnNull_WhenNullParentIdProvided() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(null, TEST_CHILD_ID, TEST_LABEL);

        // When
        Long parentId = reference.getParentId();

        // Then
        assertNull("Parent ID should be null", parentId);
    }

    @Test
    public void testGetId_ShouldReturnNull_WhenNullChildIdProvided() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, null, TEST_LABEL);

        // When
        Long childId = reference.getId();

        // Then
        assertNull("Child ID should be null", childId);
    }

    @Test
    public void testGetLabel_ShouldReturnNull_WhenNullLabelProvided() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID, null);

        // When
        String label = reference.getLabel();

        // Then
        assertNull("Label should be null", label);
    }

    @Test
    public void testGetLabel_ShouldReturnEmptyString_WhenEmptyLabelProvided() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID, "");

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should be empty string", "", label);
    }

    // ==================== Different ID Types Tests ====================

    @Test
    public void testSubResourceReference_ShouldWorkWithLongIds() {
        // Given
        Long parentId = 12345L;
        Long childId = 67890L;
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(parentId, childId, TEST_LABEL);

        // When
        Long retrievedParentId = reference.getParentId();
        Long retrievedChildId = reference.getId();

        // Then
        assertEquals("Long parent ID should match", parentId, retrievedParentId);
        assertEquals("Long child ID should match", childId, retrievedChildId);
    }

    @Test
    public void testSubResourceReference_ShouldWorkWithIntegerParentAndLongChild() {
        // Given
        Integer parentId = 12345;
        Long childId = 67890L;
        SubResourceReference<Integer, Long> reference = new SubResourceReference<>(parentId, childId, TEST_LABEL);

        // When
        Integer retrievedParentId = reference.getParentId();
        Long retrievedChildId = reference.getId();

        // Then
        assertEquals("Integer parent ID should match", parentId, retrievedParentId);
        assertEquals("Long child ID should match", childId, retrievedChildId);
    }

    @Test
    public void testSubResourceReference_ShouldWorkWithStringParentAndIntegerChild() {
        // Given
        String parentId = "parent-12345";
        Integer childId = 67890;
        SubResourceReference<String, Integer> reference = new SubResourceReference<>(parentId, childId, TEST_LABEL);

        // When
        String retrievedParentId = reference.getParentId();
        Integer retrievedChildId = reference.getId();

        // Then
        assertEquals("String parent ID should match", parentId, retrievedParentId);
        assertEquals("Integer child ID should match", childId, retrievedChildId);
    }

    @Test
    public void testSubResourceReference_ShouldWorkWithDoubleParentAndStringChild() {
        // Given
        Double parentId = 123.45;
        String childId = "child-67890";
        SubResourceReference<Double, String> reference = new SubResourceReference<>(parentId, childId, TEST_LABEL);

        // When
        Double retrievedParentId = reference.getParentId();
        String retrievedChildId = reference.getId();

        // Then
        assertEquals("Double parent ID should match", parentId, retrievedParentId);
        assertEquals("String child ID should match", childId, retrievedChildId);
    }

    // ==================== Label Content Tests ====================

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialLabel = "Sub Resource with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                specialLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle special characters", specialLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenUnicodeCharactersProvided() {
        // Given
        String unicodeLabel = "Sub Resource with unicode: こんにちは世界";
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                unicodeLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle unicode characters", unicodeLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenVeryLongLabelProvided() {
        // Given
        String longLabel = "A".repeat(1000);
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                longLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle very long text", longLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenWhitespaceOnlyLabelProvided() {
        // Given
        String whitespaceLabel = "   \t\n\r   ";
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                whitespaceLabel);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle whitespace-only text", whitespaceLabel, label);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testSubResourceReference_ShouldHandleZeroIds() {
        // Given
        Long zeroParentId = 0L;
        Long zeroChildId = 0L;
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(zeroParentId, zeroChildId, TEST_LABEL);

        // When
        Long parentId = reference.getParentId();
        Long childId = reference.getId();

        // Then
        assertEquals("Zero parent ID should be handled correctly", zeroParentId, parentId);
        assertEquals("Zero child ID should be handled correctly", zeroChildId, childId);
    }

    @Test
    public void testSubResourceReference_ShouldHandleNegativeIds() {
        // Given
        Long negativeParentId = -1L;
        Long negativeChildId = -2L;
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(negativeParentId, negativeChildId,
                TEST_LABEL);

        // When
        Long parentId = reference.getParentId();
        Long childId = reference.getId();

        // Then
        assertEquals("Negative parent ID should be handled correctly", negativeParentId, parentId);
        assertEquals("Negative child ID should be handled correctly", negativeChildId, childId);
    }

    @Test
    public void testSubResourceReference_ShouldHandleVeryLargeIds() {
        // Given
        Long largeParentId = Long.MAX_VALUE;
        Long largeChildId = Long.MAX_VALUE - 1;
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(largeParentId, largeChildId,
                TEST_LABEL);

        // When
        Long parentId = reference.getParentId();
        Long childId = reference.getId();

        // Then
        assertEquals("Very large parent ID should be handled correctly", largeParentId, parentId);
        assertEquals("Very large child ID should be handled correctly", largeChildId, childId);
    }

    @Test
    public void testSubResourceReference_ShouldHandleVerySmallIds() {
        // Given
        Long smallParentId = Long.MIN_VALUE;
        Long smallChildId = Long.MIN_VALUE + 1;
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(smallParentId, smallChildId,
                TEST_LABEL);

        // When
        Long parentId = reference.getParentId();
        Long childId = reference.getId();

        // Then
        assertEquals("Very small parent ID should be handled correctly", smallParentId, parentId);
        assertEquals("Very small child ID should be handled correctly", smallChildId, childId);
    }

    // ==================== Inheritance Tests ====================

    @Test
    public void testSubResourceReference_ShouldInheritFromResourceReference() {
        // Given
        SubResourceReference<Long, Long> subReference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // When
        boolean isInstanceOfResourceReference = subReference instanceof ResourceReference;

        // Then
        assertTrue("SubResourceReference should be instance of ResourceReference", isInstanceOfResourceReference);
    }

    @Test
    public void testSubResourceReference_ShouldAccessInheritedMethods() {
        // Given
        SubResourceReference<Long, Long> subReference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);

        // When
        Long childId = subReference.getId(); // Inherited method
        String label = subReference.getLabel(); // Inherited method
        Long parentId = subReference.getParentId(); // Own method

        // Then
        assertEquals("Should access inherited getId method", TEST_CHILD_ID, childId);
        assertEquals("Should access inherited getLabel method", TEST_LABEL, label);
        assertEquals("Should access own getParentId method", TEST_PARENT_ID, parentId);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testSubResourceReferenceShouldBeImmutable() {
        // Given
        SubResourceReference<Long, Long> reference = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);
        Long originalParentId = reference.getParentId();
        Long originalChildId = reference.getId();
        String originalLabel = reference.getLabel();

        // When - Create another reference with different content
        SubResourceReference<Long, Long> anotherReference = new SubResourceReference<>(TEST_PARENT_ID + 1,
                TEST_CHILD_ID + 1, TEST_LABEL_2);

        // Then - Original reference should remain unchanged
        assertEquals("Original parent ID should remain unchanged", originalParentId, reference.getParentId());
        assertEquals("Original child ID should remain unchanged", originalChildId, reference.getId());
        assertEquals("Original label should remain unchanged", originalLabel, reference.getLabel());
        assertNotEquals("New reference should have different parent ID", reference.getParentId(),
                anotherReference.getParentId());
        assertNotEquals("New reference should have different child ID", reference.getId(), anotherReference.getId());
        assertNotEquals("New reference should have different label", reference.getLabel(), anotherReference.getLabel());
    }

    // ==================== Type Safety Tests ====================

    @Test
    public void testTypeSafety_ShouldWorkCorrectly() {
        // Given
        SubResourceReference<Long, String> longStringReference = new SubResourceReference<>(TEST_PARENT_ID,
                TEST_STRING_CHILD_ID, TEST_LABEL);
        SubResourceReference<String, Integer> stringIntReference = new SubResourceReference<>("parent-123",
                TEST_INT_PARENT_ID, TEST_LABEL);

        // When
        Long parentId1 = longStringReference.getParentId();
        String childId1 = longStringReference.getId();
        String parentId2 = stringIntReference.getParentId();
        Integer childId2 = stringIntReference.getId();

        // Then
        assertTrue("Long parent ID should be instance of Long", parentId1 instanceof Long);
        assertTrue("String child ID should be instance of String", childId1 instanceof String);
        assertTrue("String parent ID should be instance of String", parentId2 instanceof String);
        assertTrue("Integer child ID should be instance of Integer", childId2 instanceof Integer);
        assertFalse("Long parent ID should not be instance of String", parentId1 instanceof String);
        assertFalse("String child ID should not be instance of Long", childId1 instanceof Long);
    }

    // ==================== Multiple Instances Tests ====================

    @Test
    public void testMultipleInstances_ShouldBeIndependent() {
        // Given
        SubResourceReference<Long, Long> reference1 = new SubResourceReference<>(TEST_PARENT_ID, TEST_CHILD_ID,
                TEST_LABEL);
        SubResourceReference<Long, Long> reference2 = new SubResourceReference<>(TEST_PARENT_ID + 1, TEST_CHILD_ID + 1,
                TEST_LABEL_2);

        // When
        Long parentId1 = reference1.getParentId();
        Long childId1 = reference1.getId();
        String label1 = reference1.getLabel();
        Long parentId2 = reference2.getParentId();
        Long childId2 = reference2.getId();
        String label2 = reference2.getLabel();

        // Then
        assertNotEquals("Parent IDs should be different", parentId1, parentId2);
        assertNotEquals("Child IDs should be different", childId1, childId2);
        assertNotEquals("Labels should be different", label1, label2);
    }
}