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
 * Test class for {@link VisualResourceReference}.
 * 
 * @author Test Author
 */
public class VisualResourceReferenceTest {

    // Test data constants
    private static final Long TEST_ID = 1L;
    private static final String TEST_LABEL = "Test Visual Resource";
    private static final String TEST_IMAGE_URL = "http://example.com/image.jpg";
    private static final String TEST_LABEL_2 = "Another Test Visual Resource";
    private static final String TEST_IMAGE_URL_2 = "http://example.com/another-image.png";
    private static final Integer TEST_INT_ID = 42;
    private static final String TEST_STRING_ID = "visual-resource-123";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateVisualResourceReference_WhenValidParametersProvided() {
        // When
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);

        // Then
        assertNotNull("VisualResourceReference should not be null", reference);
        assertEquals("ID should match", TEST_ID, reference.getId());
        assertEquals("Label should match", TEST_LABEL, reference.getLabel());
        assertEquals("Image URL should match", TEST_IMAGE_URL, reference.getImageUrl());
    }

    @Test
    public void testConstructor_ShouldCreateVisualResourceReference_WhenNullValuesProvided() {
        // When
        VisualResourceReference<Long> reference = new VisualResourceReference<>(null, null, null);

        // Then
        assertNotNull("VisualResourceReference should not be null", reference);
        assertNull("ID should be null", reference.getId());
        assertNull("Label should be null", reference.getLabel());
        assertNull("Image URL should be null", reference.getImageUrl());
    }

    @Test
    public void testConstructor_ShouldCreateVisualResourceReference_WhenEmptyStringsProvided() {
        // When
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, "", "");

        // Then
        assertNotNull("VisualResourceReference should not be null", reference);
        assertEquals("ID should match", TEST_ID, reference.getId());
        assertEquals("Label should be empty", "", reference.getLabel());
        assertEquals("Image URL should be empty", "", reference.getImageUrl());
    }

    @Test
    public void testConstructor_ShouldCreateVisualResourceReference_WithDifferentIdTypes() {
        // Test with Integer ID
        VisualResourceReference<Integer> intReference = new VisualResourceReference<>(TEST_INT_ID, TEST_LABEL,
                TEST_IMAGE_URL);
        assertEquals("Integer ID should match", TEST_INT_ID, intReference.getId());

        // Test with String ID
        VisualResourceReference<String> stringReference = new VisualResourceReference<>(TEST_STRING_ID, TEST_LABEL,
                TEST_IMAGE_URL);
        assertEquals("String ID should match", TEST_STRING_ID, stringReference.getId());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetId_ShouldReturnCorrectId() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("ID should match", TEST_ID, id);
    }

    @Test
    public void testGetLabel_ShouldReturnCorrectLabel() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should match", TEST_LABEL, label);
    }

    @Test
    public void testGetImageUrl_ShouldReturnCorrectImageUrl() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should match", TEST_IMAGE_URL, imageUrl);
    }

    @Test
    public void testGetId_ShouldReturnNull_WhenNullIdProvided() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(null, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertNull("ID should be null", id);
    }

    @Test
    public void testGetLabel_ShouldReturnNull_WhenNullLabelProvided() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, null, TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertNull("Label should be null", label);
    }

    @Test
    public void testGetImageUrl_ShouldReturnNull_WhenNullImageUrlProvided() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, null);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertNull("Image URL should be null", imageUrl);
    }

    @Test
    public void testGetLabel_ShouldReturnEmptyString_WhenEmptyLabelProvided() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, "", TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should be empty string", "", label);
    }

    @Test
    public void testGetImageUrl_ShouldReturnEmptyString_WhenEmptyImageUrlProvided() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, "");

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should be empty string", "", imageUrl);
    }

    // ==================== Different ID Types Tests ====================

    @Test
    public void testVisualResourceReference_ShouldWorkWithLongId() {
        // Given
        Long longId = 12345L;
        VisualResourceReference<Long> reference = new VisualResourceReference<>(longId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Long ID should match", longId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldWorkWithIntegerId() {
        // Given
        Integer intId = 12345;
        VisualResourceReference<Integer> reference = new VisualResourceReference<>(intId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Integer id = reference.getId();

        // Then
        assertEquals("Integer ID should match", intId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldWorkWithStringId() {
        // Given
        String stringId = "visual-resource-12345";
        VisualResourceReference<String> reference = new VisualResourceReference<>(stringId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        String id = reference.getId();

        // Then
        assertEquals("String ID should match", stringId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldWorkWithDoubleId() {
        // Given
        Double doubleId = 123.45;
        VisualResourceReference<Double> reference = new VisualResourceReference<>(doubleId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Double id = reference.getId();

        // Then
        assertEquals("Double ID should match", doubleId, id);
    }

    // ==================== Image URL Content Tests ====================

    @Test
    public void testGetImageUrl_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialImageUrl = "http://example.com/image with spaces & special chars.jpg";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, specialImageUrl);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should handle special characters", specialImageUrl, imageUrl);
    }

    @Test
    public void testGetImageUrl_ShouldReturnExactString_WhenUnicodeCharactersProvided() {
        // Given
        String unicodeImageUrl = "http://example.com/画像/こんにちは.jpg";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, unicodeImageUrl);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should handle unicode characters", unicodeImageUrl, imageUrl);
    }

    @Test
    public void testGetImageUrl_ShouldReturnExactString_WhenVeryLongUrlProvided() {
        // Given
        String longImageUrl = "http://example.com/" + "A".repeat(1000) + ".jpg";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, longImageUrl);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should handle very long URL", longImageUrl, imageUrl);
    }

    @Test
    public void testGetImageUrl_ShouldReturnExactString_WhenWhitespaceOnlyUrlProvided() {
        // Given
        String whitespaceImageUrl = "   \t\n\r   ";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL,
                whitespaceImageUrl);

        // When
        String imageUrl = reference.getImageUrl();

        // Then
        assertEquals("Image URL should handle whitespace-only URL", whitespaceImageUrl, imageUrl);
    }

    @Test
    public void testGetImageUrl_ShouldReturnExactString_WhenDifferentImageFormatsProvided() {
        // Test different image formats
        String[] imageFormats = {
                "http://example.com/image.jpg",
                "http://example.com/image.png",
                "http://example.com/image.gif",
                "http://example.com/image.bmp",
                "http://example.com/image.webp",
                "http://example.com/image.svg"
        };

        for (String format : imageFormats) {
            VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, format);
            assertEquals("Image URL should handle " + format, format, reference.getImageUrl());
        }
    }

    // ==================== Label Content Tests ====================

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialLabel = "Visual Resource with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, specialLabel, TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle special characters", specialLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenUnicodeCharactersProvided() {
        // Given
        String unicodeLabel = "Visual Resource with unicode: こんにちは世界";
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, unicodeLabel, TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle unicode characters", unicodeLabel, label);
    }

    @Test
    public void testGetLabel_ShouldReturnExactString_WhenVeryLongLabelProvided() {
        // Given
        String longLabel = "A".repeat(1000);
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, longLabel, TEST_IMAGE_URL);

        // When
        String label = reference.getLabel();

        // Then
        assertEquals("Label should handle very long text", longLabel, label);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testVisualResourceReference_ShouldHandleZeroId() {
        // Given
        Long zeroId = 0L;
        VisualResourceReference<Long> reference = new VisualResourceReference<>(zeroId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Zero ID should be handled correctly", zeroId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldHandleNegativeId() {
        // Given
        Long negativeId = -1L;
        VisualResourceReference<Long> reference = new VisualResourceReference<>(negativeId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Negative ID should be handled correctly", negativeId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldHandleVeryLargeId() {
        // Given
        Long largeId = Long.MAX_VALUE;
        VisualResourceReference<Long> reference = new VisualResourceReference<>(largeId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Very large ID should be handled correctly", largeId, id);
    }

    @Test
    public void testVisualResourceReference_ShouldHandleVerySmallId() {
        // Given
        Long smallId = Long.MIN_VALUE;
        VisualResourceReference<Long> reference = new VisualResourceReference<>(smallId, TEST_LABEL, TEST_IMAGE_URL);

        // When
        Long id = reference.getId();

        // Then
        assertEquals("Very small ID should be handled correctly", smallId, id);
    }

    // ==================== Inheritance Tests ====================

    @Test
    public void testVisualResourceReference_ShouldInheritFromResourceReference() {
        // Given
        VisualResourceReference<Long> visualReference = new VisualResourceReference<>(TEST_ID, TEST_LABEL,
                TEST_IMAGE_URL);

        // When
        boolean isInstanceOfResourceReference = visualReference instanceof ResourceReference;

        // Then
        assertTrue("VisualResourceReference should be instance of ResourceReference", isInstanceOfResourceReference);
    }

    @Test
    public void testVisualResourceReference_ShouldAccessInheritedMethods() {
        // Given
        VisualResourceReference<Long> visualReference = new VisualResourceReference<>(TEST_ID, TEST_LABEL,
                TEST_IMAGE_URL);

        // When
        Long id = visualReference.getId(); // Inherited method
        String label = visualReference.getLabel(); // Inherited method
        String imageUrl = visualReference.getImageUrl(); // Own method

        // Then
        assertEquals("Should access inherited getId method", TEST_ID, id);
        assertEquals("Should access inherited getLabel method", TEST_LABEL, label);
        assertEquals("Should access own getImageUrl method", TEST_IMAGE_URL, imageUrl);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testVisualResourceReferenceShouldBeImmutable() {
        // Given
        VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);
        Long originalId = reference.getId();
        String originalLabel = reference.getLabel();
        String originalImageUrl = reference.getImageUrl();

        // When - Create another reference with different content
        VisualResourceReference<Long> anotherReference = new VisualResourceReference<>(TEST_ID + 1, TEST_LABEL_2,
                TEST_IMAGE_URL_2);

        // Then - Original reference should remain unchanged
        assertEquals("Original ID should remain unchanged", originalId, reference.getId());
        assertEquals("Original label should remain unchanged", originalLabel, reference.getLabel());
        assertEquals("Original image URL should remain unchanged", originalImageUrl, reference.getImageUrl());
        assertNotEquals("New reference should have different ID", reference.getId(), anotherReference.getId());
        assertNotEquals("New reference should have different label", reference.getLabel(), anotherReference.getLabel());
        assertNotEquals("New reference should have different image URL", reference.getImageUrl(),
                anotherReference.getImageUrl());
    }

    // ==================== Type Safety Tests ====================

    @Test
    public void testTypeSafety_ShouldWorkCorrectly() {
        // Given
        VisualResourceReference<Long> longReference = new VisualResourceReference<>(TEST_ID, TEST_LABEL,
                TEST_IMAGE_URL);
        VisualResourceReference<String> stringReference = new VisualResourceReference<>(TEST_STRING_ID, TEST_LABEL,
                TEST_IMAGE_URL);

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
        VisualResourceReference<Long> reference1 = new VisualResourceReference<>(TEST_ID, TEST_LABEL, TEST_IMAGE_URL);
        VisualResourceReference<Long> reference2 = new VisualResourceReference<>(TEST_ID + 1, TEST_LABEL_2,
                TEST_IMAGE_URL_2);

        // When
        Long id1 = reference1.getId();
        String label1 = reference1.getLabel();
        String imageUrl1 = reference1.getImageUrl();
        Long id2 = reference2.getId();
        String label2 = reference2.getLabel();
        String imageUrl2 = reference2.getImageUrl();

        // Then
        assertNotEquals("IDs should be different", id1, id2);
        assertNotEquals("Labels should be different", label1, label2);
        assertNotEquals("Image URLs should be different", imageUrl1, imageUrl2);
    }

    // ==================== URL Format Tests ====================

    @Test
    public void testGetImageUrl_ShouldHandleDifferentUrlSchemes() {
        // Test different URL schemes
        String[] urlSchemes = {
                "http://example.com/image.jpg",
                "https://example.com/image.jpg",
                "ftp://example.com/image.jpg",
                "file:///path/to/image.jpg",
                "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAv/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAX/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCdABmX/9k="
        };

        for (String scheme : urlSchemes) {
            VisualResourceReference<Long> reference = new VisualResourceReference<>(TEST_ID, TEST_LABEL, scheme);
            assertEquals("Image URL should handle " + scheme, scheme, reference.getImageUrl());
        }
    }
}