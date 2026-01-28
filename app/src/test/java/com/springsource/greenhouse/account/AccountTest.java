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
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.util.UriTemplate;

/**
 * Test class for {@link Account}.
 * 
 * @author Test Author
 */
public class AccountTest {

    // Test data constants
    private static final Long TEST_ID = 1L;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_USERNAME = "johndoe";
    private static final String TEST_PICTURE_URL = "http://example.com/picture.jpg";
    private static final String TEST_PROFILE_URL_TEMPLATE = "http://example.com/members/{profileKey}";

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateAccount_WhenValidParametersProvided() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);

        // When
        Account account = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_USERNAME, TEST_PICTURE_URL, profileUrlTemplate);

        // Then
        assertNotNull("Account should not be null", account);
        assertEquals("ID should match", TEST_ID, account.getId());
        assertEquals("First name should match", TEST_FIRST_NAME, account.getFirstName());
        assertEquals("Last name should match", TEST_LAST_NAME, account.getLastName());
        assertEquals("Email should match", TEST_EMAIL, account.getEmail());
        assertEquals("Username should match", TEST_USERNAME, account.getUsername());
        assertEquals("Picture URL should match", TEST_PICTURE_URL, account.getPictureUrl());
    }

    @Test
    public void testConstructor_ShouldCreateAccount_WhenUsernameIsNull() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);

        // When
        Account account = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                null, TEST_PICTURE_URL, profileUrlTemplate);

        // Then
        assertNotNull("Account should not be null", account);
        assertNull("Username should be null", account.getUsername());
        assertEquals("Profile URL should use ID when username is null",
                "http://example.com/members/1", account.getProfileUrl());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetId_ShouldReturnCorrectId() {
        // Given
        Account account = createTestAccount();

        // When
        Long id = account.getId();

        // Then
        assertEquals("ID should match", TEST_ID, id);
    }

    @Test
    public void testGetFirstName_ShouldReturnCorrectFirstName() {
        // Given
        Account account = createTestAccount();

        // When
        String firstName = account.getFirstName();

        // Then
        assertEquals("First name should match", TEST_FIRST_NAME, firstName);
    }

    @Test
    public void testGetLastName_ShouldReturnCorrectLastName() {
        // Given
        Account account = createTestAccount();

        // When
        String lastName = account.getLastName();

        // Then
        assertEquals("Last name should match", TEST_LAST_NAME, lastName);
    }

    @Test
    public void testGetEmail_ShouldReturnCorrectEmail() {
        // Given
        Account account = createTestAccount();

        // When
        String email = account.getEmail();

        // Then
        assertEquals("Email should match", TEST_EMAIL, email);
    }

    @Test
    public void testGetUsername_ShouldReturnCorrectUsername() {
        // Given
        Account account = createTestAccount();

        // When
        String username = account.getUsername();

        // Then
        assertEquals("Username should match", TEST_USERNAME, username);
    }

    @Test
    public void testGetPictureUrl_ShouldReturnCorrectPictureUrl() {
        // Given
        Account account = createTestAccount();

        // When
        String pictureUrl = account.getPictureUrl();

        // Then
        assertEquals("Picture URL should match", TEST_PICTURE_URL, pictureUrl);
    }

    // ==================== Full Name Tests ====================

    @Test
    public void testGetFullName_ShouldReturnCombinedNames() {
        // Given
        Account account = createTestAccount();

        // When
        String fullName = account.getFullName();

        // Then
        assertEquals("Full name should be first and last name combined",
                TEST_FIRST_NAME + " " + TEST_LAST_NAME, fullName);
    }

    @Test
    public void testGetFullName_ShouldHandleEmptyNames() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        Account account = new Account(TEST_ID, "", "", TEST_EMAIL, TEST_USERNAME,
                TEST_PICTURE_URL, profileUrlTemplate);

        // When
        String fullName = account.getFullName();

        // Then
        assertEquals("Full name should handle empty names", " ", fullName);
    }

    // ==================== Profile URL Tests ====================

    @Test
    public void testGetProfileUrl_ShouldReturnCorrectUrl_WhenUsernameProvided() {
        // Given
        Account account = createTestAccount();

        // When
        String profileUrl = account.getProfileUrl();

        // Then
        assertEquals("Profile URL should use username",
                "http://example.com/members/johndoe", profileUrl);
    }

    @Test
    public void testGetProfileUrl_ShouldReturnCorrectUrl_WhenUsernameIsNull() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        Account account = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                null, TEST_PICTURE_URL, profileUrlTemplate);

        // When
        String profileUrl = account.getProfileUrl();

        // Then
        assertEquals("Profile URL should use ID when username is null",
                "http://example.com/members/1", profileUrl);
    }

    // ==================== Profile ID Tests ====================

    @Test
    public void testGetProfileId_ShouldReturnUsername_WhenUsernameProvided() {
        // Given
        Account account = createTestAccount();

        // When
        String profileId = account.getProfileId();

        // Then
        assertEquals("Profile ID should be username", TEST_USERNAME, profileId);
    }

    @Test
    public void testGetProfileId_ShouldReturnIdAsString_WhenUsernameIsNull() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        Account account = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                null, TEST_PICTURE_URL, profileUrlTemplate);

        // When
        String profileId = account.getProfileId();

        // Then
        assertEquals("Profile ID should be ID as string", TEST_ID.toString(), profileId);
    }

    // ==================== Principal Interface Tests ====================

    @Test
    public void testGetName_ShouldReturnUsername_WhenUsernameProvided() {
        // Given
        Account account = createTestAccount();

        // When
        String name = account.getName();

        // Then
        assertEquals("Name should be username", TEST_USERNAME, name);
    }

    @Test
    public void testGetName_ShouldReturnEmail_WhenUsernameIsNull() {
        // Given
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        Account account = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                null, TEST_PICTURE_URL, profileUrlTemplate);

        // When
        String name = account.getName();

        // Then
        assertEquals("Name should be email when username is null", TEST_EMAIL, name);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString_ShouldReturnIdAsString() {
        // Given
        Account account = createTestAccount();

        // When
        String result = account.toString();

        // Then
        assertEquals("ToString should return ID as string", TEST_ID.toString(), result);
    }

    // ==================== Helper Methods ====================

    private Account createTestAccount() {
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        return new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_USERNAME, TEST_PICTURE_URL, profileUrlTemplate);
    }
}