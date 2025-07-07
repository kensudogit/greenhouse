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

public class SlugUtilsTest {

	// ==================== Basic Slug Tests ====================

	@Test
	public void testToSlug_ShouldReturnSameString_WhenNoSpaces() {
		// Given
		String input = "test";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Single word should remain unchanged", "test", result);
	}

	@Test
	public void testToSlug_ShouldReplaceSpacesWithHyphens() {
		// Given
		String input = "test message";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Spaces should be replaced with hyphens", "test-message", result);
	}

	@Test
	public void testToSlug_ShouldHandleMultipleWords() {
		// Given
		String input = "test message with more than five words";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Multiple words should be hyphenated", "test-message-with-more-than-five-words", result);
	}

	// ==================== Special Character Tests ====================

	@Test
	public void testToSlug_ShouldHandleColons() {
		// Given
		String input = "test message: colon edition";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Colons should be handled properly", "test-message-colon-edition", result);
	}

	@Test
	public void testToSlug_ShouldHandleNumbers() {
		// Given
		String input = "test message number 5";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Numbers should be preserved", "test-message-number-5", result);
	}

	@Test
	public void testToSlug_ShouldRemoveSpecialCharacters() {
		// Given
		String input = "another @%$#! test messages";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Special characters should be removed", "another--test-messages", result);
	}

	// ==================== Case Handling Tests ====================

	@Test
	public void testToSlug_ShouldConvertToLowerCase() {
		// Given
		String input = "This ONE wAs In CaPs";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Mixed case should be converted to lowercase", "this-one-was-in-caps", result);
	}

	@Test
	public void testToSlug_ShouldHandleAllUpperCase() {
		// Given
		String input = "ALL UPPERCASE TEXT";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("All uppercase should be converted to lowercase", "all-uppercase-text", result);
	}

	@Test
	public void testToSlug_ShouldHandleAllLowerCase() {
		// Given
		String input = "all lowercase text";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("All lowercase should remain lowercase", "all-lowercase-text", result);
	}

	// ==================== Edge Case Tests ====================

	@Test
	public void testToSlug_ShouldHandleEmptyString() {
		// Given
		String input = "";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Empty string should return empty string", "", result);
	}

	@Test
	public void testToSlug_ShouldHandleSingleCharacter() {
		// Given
		String input = "a";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Single character should remain unchanged", "a", result);
	}

	@Test
	public void testToSlug_ShouldHandleMultipleHyphens() {
		// Given
		String input = "word1---word2";

		// When
		String result = SlugUtils.toSlug(input);

		// Then
		assertEquals("Multiple hyphens should be handled", "word1---word2", result);
	}
}
