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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

    // ==================== Scale Image To Width Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldReturnOriginalBytes_WhenOriginalWidthIsSmallerThanScaledWidth()
            throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 200); // 100x200 image
        int scaledWidth = 150;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        assertArrayEquals("Should return original bytes when original width is smaller", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldReturnOriginalBytes_WhenOriginalWidthEqualsScaledWidth()
            throws IOException {
        // Given
        byte[] originalBytes = createTestImage(200, 300); // 200x300 image
        int scaledWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        assertArrayEquals("Should return original bytes when original width equals scaled width", originalBytes,
                result);
    }

    @Test
    public void testScaleImageToWidth_ShouldScaleImage_WhenOriginalWidthIsLargerThanScaledWidth() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(400, 300); // 400x300 image
        int scaledWidth = 200;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        assertNotNull("Result should not be null", result);
        assertNotEquals("Result should be different from original", originalBytes, result);

        // Verify the scaled image dimensions
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should match requested width", scaledWidth, scaledImage.getWidth());
        assertEquals("Scaled height should be proportional", 150, scaledImage.getHeight()); // 300 * (200/400) = 150
    }

    @Test
    public void testScaleImageToWidth_ShouldMaintainAspectRatio() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(800, 600); // 4:3 aspect ratio
        int scaledWidth = 400;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 400", 400, scaledImage.getWidth());
        assertEquals("Scaled height should maintain aspect ratio", 300, scaledImage.getHeight()); // 600 * (400/800) =
                                                                                                  // 300
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleSquareImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(500, 500); // Square image
        int scaledWidth = 250;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 250", 250, scaledImage.getWidth());
        assertEquals("Scaled height should be 250", 250, scaledImage.getHeight());
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleTallImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(200, 800); // Tall image
        int scaledWidth = 100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 100", 100, scaledImage.getWidth());
        assertEquals("Scaled height should be proportional", 400, scaledImage.getHeight()); // 800 * (100/200) = 400
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleWideImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(1200, 200); // Wide image
        int scaledWidth = 300;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 300", 300, scaledImage.getWidth());
        assertEquals("Scaled height should be proportional", 50, scaledImage.getHeight()); // 200 * (300/1200) = 50
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleSmallScaleFactor() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(1000, 1000); // Large square image
        int scaledWidth = 50; // Very small scale

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 50", 50, scaledImage.getWidth());
        assertEquals("Scaled height should be 50", 50, scaledImage.getHeight());
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleLargeScaleFactor() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(50, 50); // Small square image
        int scaledWidth = 1000; // Very large scale

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        assertArrayEquals("Should return original bytes when scaling up", originalBytes, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenNullBytesProvided() throws IOException {
        // When
        ImageUtils.scaleImageToWidth(null, 100);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenEmptyBytesProvided() throws IOException {
        // When
        ImageUtils.scaleImageToWidth(new byte[0], 100);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenInvalidImageBytesProvided() throws IOException {
        // When
        ImageUtils.scaleImageToWidth("This is not an image".getBytes(), 100);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenNegativeWidthProvided() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 100);

        // When
        ImageUtils.scaleImageToWidth(originalBytes, -50);

        // Then - Exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleImageToWidth_ShouldThrowException_WhenZeroWidthProvided() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(100, 100);

        // When
        ImageUtils.scaleImageToWidth(originalBytes, 0);

        // Then - Exception should be thrown
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testScaleImageToWidth_ShouldHandleSinglePixelImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(1, 1); // Single pixel image
        int scaledWidth = 100;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        assertArrayEquals("Should return original bytes for single pixel image", originalBytes, result);
    }

    @Test
    public void testScaleImageToWidth_ShouldHandleVeryLargeImage() throws IOException {
        // Given
        byte[] originalBytes = createTestImage(2000, 1500); // Very large image
        int scaledWidth = 500;

        // When
        byte[] result = ImageUtils.scaleImageToWidth(originalBytes, scaledWidth);

        // Then
        BufferedImage scaledImage = ImageIO.read(new ByteArrayInputStream(result));
        assertEquals("Scaled width should be 500", 500, scaledImage.getWidth());
        assertEquals("Scaled height should be proportional", 375, scaledImage.getHeight()); // 1500 * (500/2000) = 375
    }

    // ==================== Helper Methods ====================

    private byte[] createTestImage(int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Fill with a gradient pattern for testing
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = (x * 255) / width;
                int green = (y * 255) / height;
                int blue = 128;
                int rgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }

        g.dispose();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", byteStream);
        return byteStream.toByteArray();
    }
}