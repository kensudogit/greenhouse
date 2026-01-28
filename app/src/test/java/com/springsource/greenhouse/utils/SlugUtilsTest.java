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
 * Test class for {@link SlugUtils}.
 * 
 * @author Test Author
 */
public class SlugUtilsTest {

	// ==================== Basic Functionality Tests ====================

	@Test
	public void testToSlug_ShouldConvertSimpleString_WhenValidInputProvided() {
		// Given
		String input = "Hello World";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world", result);
	}

	@Test
	public void testToSlug_ShouldConvertStringWithSpaces_WhenMultipleSpacesProvided() {
		// Given
		String input = "Hello   World   Test";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello---world---test", result);
	}

	@Test
	public void testToSlug_ShouldConvertStringWithSpecialCharacters_WhenSpecialCharactersProvided() {
		// Given
		String input = "Hello!@#$%^&*()World";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("helloworld", result);
	}

	@Test
	public void testToSlug_ShouldConvertStringWithNumbers_WhenNumbersProvided() {
		// Given
		String input = "Hello123World456";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello123world456", result);
	}

	@Test
	public void testToSlug_ShouldConvertStringWithHyphens_WhenHyphensProvided() {
		// Given
		String input = "Hello-World-Test";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world-test", result);
	}

	@Test
	public void testToSlug_ShouldConvertStringWithUnderscores_WhenUnderscoresProvided() {
		// Given
		String input = "Hello_World_Test";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello_world_test", result);
	}

	// ==================== Edge Case Tests ====================

	@Test(expected = IllegalArgumentException.class)
	public void testToSlug_ShouldThrowException_WhenNullInputProvided() {
		// When & Then
		SlugUtils.toSlug(null);
	}

	@Test
	public void testToSlug_ShouldHandleEmptyString_WhenEmptyStringProvided() {
		// Given
		String input = "";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testToSlug_ShouldHandleStringWithOnlySpaces_WhenOnlySpacesProvided() {
		// Given
		String input = "   ";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("---", result);
	}

	@Test
	public void testToSlug_ShouldHandleStringWithOnlySpecialCharacters_WhenOnlySpecialCharactersProvided() {
		// Given
		String input = "!@#$%^&*()";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("", result);
	}

	@Test
	public void testToSlug_ShouldHandleStringWithOnlyNumbers_WhenOnlyNumbersProvided() {
		// Given
		String input = "123456789";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("123456789", result);
	}

	// ==================== Unicode and International Tests ====================

	@Test
	public void testToSlug_ShouldHandleUnicodeCharacters_WhenUnicodeCharactersProvided() {
		// Given
		String input = "こんにちは世界";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("", result); // Non-Latin characters should be removed
	}

	@Test
	public void testToSlug_ShouldHandleMixedUnicodeAndLatin_WhenMixedCharactersProvided() {
		// Given
		String input = "Hello世界World";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("helloworld", result);
	}

	@Test
	public void testToSlug_ShouldHandleAccentedCharacters_WhenAccentedCharactersProvided() {
		// Given
		String input = "Café naïve résumé";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("caf-na-ve-r-sum", result);
	}

	// ==================== Case Sensitivity Tests ====================

	@Test
	public void testToSlug_ShouldConvertToLowerCase_WhenUpperCaseProvided() {
		// Given
		String input = "HELLO WORLD";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world", result);
	}

	@Test
	public void testToSlug_ShouldConvertToLowerCase_WhenMixedCaseProvided() {
		// Given
		String input = "HeLLo WoRLd";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world", result);
	}

	// ==================== Complex String Tests ====================

	@Test
	public void testToSlug_ShouldHandleComplexString_WhenComplexStringProvided() {
		// Given
		String input = "Hello World! This is a test @#$% string with numbers 123 and symbols.";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world-this-is-a-test-string-with-numbers-123-and-symbols", result);
	}

	@Test
	public void testToSlug_ShouldHandleStringWithTabsAndNewlines_WhenTabsAndNewlinesProvided() {
		// Given
		String input = "Hello\tWorld\nTest";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("hello-world-test", result);
	}

	// ==================== Consistency Tests ====================

	@Test
	public void testToSlug_ShouldBeConsistent_WhenSameInputProvidedMultipleTimes() {
		// Given
		String input = "Hello World Test";

		// When
		String result1 = SlugUtils.toSlug(input);
		String result2 = SlugUtils.toSlug(input);
		String result3 = SlugUtils.toSlug(input);

		// Then
		assertEquals("Results should be consistent", result1, result2);
		assertEquals("Results should be consistent", result2, result3);
		assertEquals("hello-world-test", result1);
	}

	// ==================== Performance Tests ====================

	@Test
	public void testToSlug_ShouldCompleteWithinReasonableTime_WhenLongStringProvided() {
		// Given
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append("Hello World ");
		}
		String longInput = sb.toString();
		long startTime = System.currentTimeMillis();

		// When
		String result = SlugUtils.toSlug(longInput);

		// Then
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		assertNotNull("Result should not be null", result);
		assertTrue("Should complete within reasonable time (1000ms)", duration < 1000);
	}

	// ==================== Real-world Examples Tests ====================

	@Test
	public void testToSlug_ShouldHandleTitleLikeString_WhenTitleProvided() {
		// Given
		String input = "Spring Framework 3.0 - New Features and Improvements";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("spring-framework-30-new-features-and-improvements", result);
	}

	@Test
	public void testToSlug_ShouldHandleNameLikeString_WhenNameProvided() {
		// Given
		String input = "John O'Connor-Smith";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("john-oconnor-smith", result);
	}

	@Test
	public void testToSlug_ShouldHandleProductName_WhenProductNameProvided() {
		// Given
		String input = "iPhone 15 Pro Max (256GB)";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("iphone-15-pro-max-256gb", result);
	}
}