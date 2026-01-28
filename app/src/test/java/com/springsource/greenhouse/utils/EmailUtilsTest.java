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

import org.junit.Test;

/**
 * Test class for {@link EmailUtils}.
 * 
 * @author Test Author
 */
public class EmailUtilsTest {

  // ==================== Valid Email Tests ====================

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenValidEmailProvided() {
    // Given
    String validEmail = "test@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for valid email", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithNumbersProvided() {
    // Given
    String validEmail = "test123@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with numbers", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithDotsProvided() {
    // Given
    String validEmail = "test.name@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with dots", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithUnderscoresProvided() {
    // Given
    String validEmail = "test_name@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with underscores", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithHyphensProvided() {
    // Given
    String validEmail = "test-name@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with hyphens", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithPlusSignProvided() {
    // Given
    String validEmail = "test+tag@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with plus sign", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithPercentSignProvided() {
    // Given
    String validEmail = "test%tag@example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with percent sign", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithShortDomainProvided() {
    // Given
    String validEmail = "test@example.co";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with short domain", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithLongDomainProvided() {
    // Given
    String validEmail = "test@example.info";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with long domain", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenEmailWithSubdomainProvided() {
    // Given
    String validEmail = "test@mail.example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Should return true for email with subdomain", result);
  }

  // ==================== Invalid Email Tests ====================

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNullProvided() {
    // When
    boolean result = EmailUtils.isEmail(null);

    // Then
    assertFalse("Should return false for null", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenEmptyStringProvided() {
    // Given
    String invalidEmail = "";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for empty string", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNoAtSymbolProvided() {
    // Given
    String invalidEmail = "testexample.com";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email without @ symbol", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenMultipleAtSymbolsProvided() {
    // Given
    String invalidEmail = "test@example@com";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with multiple @ symbols", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNoDomainProvided() {
    // Given
    String invalidEmail = "test@";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email without domain", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNoLocalPartProvided() {
    // Given
    String invalidEmail = "@example.com";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email without local part", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenInvalidDomainProvided() {
    // Given
    String invalidEmail = "test@example";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with invalid domain", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenDomainTooShortProvided() {
    // Given
    String invalidEmail = "test@example.c";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with domain too short", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenDomainTooLongProvided() {
    // Given
    String invalidEmail = "test@example.comm";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with domain too long", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenSpacesInEmailProvided() {
    // Given
    String invalidEmail = "test @example.com";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with spaces", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenSpecialCharactersInDomainProvided() {
    // Given
    String invalidEmail = "test@example!.com";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for email with special characters in domain", result);
  }

  // ==================== Edge Case Tests ====================

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenOnlyAtSymbolProvided() {
    // Given
    String invalidEmail = "@";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for only @ symbol", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenOnlyDotProvided() {
    // Given
    String invalidEmail = ".";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for only dot", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenWhitespaceOnlyProvided() {
    // Given
    String invalidEmail = "   ";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for whitespace only", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNumbersOnlyProvided() {
    // Given
    String invalidEmail = "123456";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Should return false for numbers only", result);
  }

  // ==================== Real-world Examples Tests ====================

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenCommonEmailFormatsProvided() {
    // Test various common email formats
    String[] validEmails = {
        "user@gmail.com",
        "john.doe@company.co.uk",
        "test+newsletter@example.org",
        "admin@subdomain.example.com",
        "contact@my-site.info"
    };

    for (String email : validEmails) {
      // When
      boolean result = EmailUtils.isEmail(email);

      // Then
      assertTrue("Should return true for valid email: " + email, result);
    }
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenCommonInvalidFormatsProvided() {
    // Test various common invalid email formats
    String[] invalidEmails = {
        "user@",
        "@gmail.com",
        "user@.com",
        "user@com",
        "user name@gmail.com",
        "user@gmail",
        "user@@gmail.com",
        "user@gmail..com"
    };

    for (String email : invalidEmails) {
      // When
      boolean result = EmailUtils.isEmail(email);

      // Then
      assertFalse("Should return false for invalid email: " + email, result);
    }
  }

  // ==================== Performance Tests ====================

  @Test
  public void testIsEmail_ShouldCompleteWithinReasonableTime_WhenCalledMultipleTimes() {
    // Given
    String email = "test@example.com";
    long startTime = System.currentTimeMillis();
    int iterations = 10000;

    // When
    for (int i = 0; i < iterations; i++) {
      EmailUtils.isEmail(email);
    }

    // Then
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;

    assertTrue("Should complete within reasonable time (1000ms)", duration < 1000);
  }

  // ==================== Consistency Tests ====================

  @Test
  public void testIsEmail_ShouldBeConsistent_WhenSameInputProvidedMultipleTimes() {
    // Given
    String email = "test@example.com";

    // When
    boolean result1 = EmailUtils.isEmail(email);
    boolean result2 = EmailUtils.isEmail(email);
    boolean result3 = EmailUtils.isEmail(email);

    // Then
    assertEquals("Results should be consistent", result1, result2);
    assertEquals("Results should be consistent", result2, result3);
    assertTrue("Should return true", result1);
  }
}