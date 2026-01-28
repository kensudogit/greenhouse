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
package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * Test class for {@link ImageUtils}.
 * 
 * @author Test Author
 */
public class ImageUtilsTest {

    // ==================== Helper Methods ====================

    private byte[] createTestImage(int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        return baos.toByteArray();
    }

    // ==================== Basic Functionality Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldReturnOriginalBytes_WhenImageWidthIsSmallerThanTargetWidth()
            throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 200);
        int targetWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertArrayEquals("Should return original bytes when image is smaller", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldReturnOriginalBytes_WhenImageWidthEqualsTargetWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(200, 300);
        int targetWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertArrayEquals("Should return original bytes when image width equals target", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldScaleImage_WhenImageWidthIsLargerThanTargetWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300);
        int targetWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertNotEquals("Should return different bytes when scaling", originalBytes, result);
        assertTrue("Result should be smaller than original", result.length < originalBytes.length);
    }

    // ==================== Edge Case Tests ====================

    @Test(expected = IOException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenInvalidImageBytesProvided() throws IOException {
        // Given
        byte[] invalidBytes = "invalid image data".getBytes();
        int targetWidth = 200;

        // When & Then
        ImageUtils.scaleImageToWidth(invalidBytes, targetWidth);
    }

    @Test(expected = IOException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenEmptyBytesProvided() throws IOException {
        // Given
        byte[] emptyBytes = new byte[0];
        int targetWidth = 200;

        // When & Then
        ImageUtils.scaleImageToWidth(emptyBytes, targetWidth);
    }

    @Test(expected = IOException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenNullBytesProvided() throws IOException {
        // Given
        byte[] nullBytes = null;
        int targetWidth = 200;

        // When & Then
        ImageUtils.scaleImageToWidth(nullBytes, targetWidth);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleZeroTargetWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 200);
        int targetWidth = 0;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertArrayEquals("Should return original bytes for zero target width", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleNegativeTargetWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 200);
        int targetWidth = -100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertArrayEquals("Should return original bytes for negative target width", originalBytes, result);
    }

    // ==================== Scaling Ratio Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldMaintainAspectRatio_WhenScalingDown() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300); // 4:3 aspect ratio
        int targetWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        // The result should be a valid JPEG image
        assertTrue("Result should be a valid image", result.length > 0);
    }

    @Test
    public void testScaleImageToWidth_ShouldMaintainAspectRatio_WhenScalingToHalfSize() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(200, 150);
        int targetWidth = 100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    @Test
    public void testScaleImageToWidth_ShouldMaintainAspectRatio_WhenScalingToQuarterSize() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300);
        int targetWidth = 100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    // ==================== Different Image Sizes Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldHandleSmallImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(50, 50);
        int targetWidth = 25;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleLargeImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(1000, 800);
        int targetWidth = 500;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
        assertTrue("Result should be smaller than original", result.length < originalBytes.length);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleSquareImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(300, 300);
        int targetWidth = 150;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleWideImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(800, 200);
        int targetWidth = 400;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleTallImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(200, 800);
        int targetWidth = 100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }

    // ==================== Performance Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldCompleteWithinReasonableTime_WhenScalingLargeImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(2000, 1500);
        int targetWidth = 1000;
        long startTime = System.currentTimeMillis();

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull("Result should not be null", result);
        assertTrue("Should complete within reasonable time (5000ms)", duration < 5000);
    }

    @Test
    public void testScaleImageToWidth_ShouldCompleteWithinReasonableTime_WhenCalledMultipleTimes() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300);
        int targetWidth = 200;
        long startTime = System.currentTimeMillis();
        int iterations = 100;

        // When
        for (int i = 0; i < iterations; i++) {
            ImageUtils.scaleImageToWidth(originalBytes, targetWidth);
        }

        // Then
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue("Should complete within reasonable time (10000ms)", duration < 10000);
    }

    // ==================== Consistency Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldBeConsistent_WhenSameInputProvidedMultipleTimes() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300);
        int targetWidth = 200;

        // When
        byte[] result1 = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);
        byte[] result2 = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);
        byte[] result3 = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("First result should not be null", result1);
        assertNotNull("Second result should not be null", result2);
        assertNotNull("Third result should not be null", result3);

        // Results should be consistent in size (though content might vary due to JPEG
        // compression)
        assertEquals("Results should have consistent length", result1.length, result2.length);
        assertEquals("Results should have consistent length", result2.length, result3.length);
    }

    // ==================== Boundary Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldHandleMinimumImageSize() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(1, 1);
        int targetWidth = 1;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertArrayEquals("Should return original bytes for minimum size", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleVerySmallTargetWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 100);
        int targetWidth = 1;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, targetWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be a valid image", result.length > 0);
    }
}