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

public class EmailUtilsTest {

  // ==================== Valid Email Tests ====================

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenValidEmailProvided() {
    // Given
    String validEmail = "test@test.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Valid email should be recognized", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenValidEmailWithSubdomain() {
    // Given
    String validEmail = "user@subdomain.example.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Valid email with subdomain should be recognized", result);
  }

  @Test
  public void testIsEmail_ShouldReturnTrue_WhenValidEmailWithNumbers() {
    // Given
    String validEmail = "user123@example123.com";

    // When
    boolean result = EmailUtils.isEmail(validEmail);

    // Then
    assertTrue("Valid email with numbers should be recognized", result);
  }

  // ==================== Invalid Email Tests ====================

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNoAtSymbol() {
    // Given
    String invalidEmail = "chuck";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Email without @ symbol should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenOnlyAtSymbol() {
    // Given
    String invalidEmail = "@";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Email with only @ symbol should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenTooShortDomain() {
    // Given
    String invalidEmail = "a@b.c";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Email with too short domain should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenInvalidDomain() {
    // Given
    String invalidEmail = "a@b.chuck";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Email with invalid domain should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenInvalidTopLevelDomain() {
    // Given
    String invalidEmail = "a@b.cd1";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Email with invalid top-level domain should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenEmptyString() {
    // Given
    String invalidEmail = "";

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Empty string should be rejected", result);
  }

  @Test
  public void testIsEmail_ShouldReturnFalse_WhenNullString() {
    // Given
    String invalidEmail = null;

    // When
    boolean result = EmailUtils.isEmail(invalidEmail);

    // Then
    assertFalse("Null string should be rejected", result);
  }
}
