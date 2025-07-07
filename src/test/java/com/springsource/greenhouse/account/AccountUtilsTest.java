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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriTemplate;

/**
 * Test class for {@link AccountUtils}.
 * 
 * @author Test Author
 */
public class AccountUtilsTest {

    // Test data constants
    private static final Long TEST_ID = 1L;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_USERNAME = "johndoe";
    private static final String TEST_PICTURE_URL = "http://example.com/picture.jpg";
    private static final String TEST_PROFILE_URL_TEMPLATE = "http://example.com/members/{profileKey}";

    private Account testAccount;

    @Before
    public void setUp() {
        // Clear security context before each test
        SecurityContextHolder.clearContext();

        // Create test account
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        testAccount = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_USERNAME, TEST_PICTURE_URL, profileUrlTemplate);
    }

    @After
    public void tearDown() {
        // Clear security context after each test
        SecurityContextHolder.clearContext();
    }

    // ==================== Signin Tests ====================

    @Test
    public void testSignin_ShouldSetAuthenticationInSecurityContext() {
        // When
        AccountUtils.signin(testAccount);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Authentication should not be null", authentication);
        assertEquals("Principal should be the test account", testAccount, authentication.getPrincipal());
        assertTrue("Authentication should be UsernamePasswordAuthenticationToken",
                authentication instanceof UsernamePasswordAuthenticationToken);
    }

    @Test
    public void testSignin_ShouldReplaceExistingAuthentication() {
        // Given
        AccountUtils.signin(testAccount);
        Authentication firstAuth = SecurityContextHolder.getContext().getAuthentication();

        // Create another account
        UriTemplate profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        Account secondAccount = new Account(2L, "Jane", "Smith", "jane@example.com",
                "janesmith", TEST_PICTURE_URL, profileUrlTemplate);

        // When
        AccountUtils.signin(secondAccount);

        // Then
        Authentication secondAuth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Second authentication should not be null", secondAuth);
        assertEquals("Principal should be the second account", secondAccount, secondAuth.getPrincipal());
        assertNotSame("Authentication should be different", firstAuth, secondAuth);
    }

    // ==================== Authentication Token Tests ====================

    @Test
    public void testAuthenticationTokenFor_ShouldCreateValidAuthenticationToken() {
        // When
        Authentication authentication = AccountUtils.authenticationTokenFor(testAccount);

        // Then
        assertNotNull("Authentication should not be null", authentication);
        assertTrue("Authentication should be UsernamePasswordAuthenticationToken",
                authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals("Principal should be the test account", testAccount, authentication.getPrincipal());
        assertNull("Credentials should be null", authentication.getCredentials());
        assertNull("Authorities should be null", authentication.getAuthorities());
    }

    @Test
    public void testAuthenticationTokenFor_ShouldCreateTokenWithNullAccount() {
        // When
        Authentication authentication = AccountUtils.authenticationTokenFor(null);

        // Then
        assertNotNull("Authentication should not be null", authentication);
        assertTrue("Authentication should be UsernamePasswordAuthenticationToken",
                authentication instanceof UsernamePasswordAuthenticationToken);
        assertNull("Principal should be null", authentication.getPrincipal());
        assertNull("Credentials should be null", authentication.getCredentials());
        assertNull("Authorities should be null", authentication.getAuthorities());
    }

    // ==================== Get Current Account Tests ====================

    @Test
    public void testGetCurrentAccount_ShouldReturnAccount_WhenAccountIsAuthenticated() {
        // Given
        AccountUtils.signin(testAccount);

        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match test account", testAccount, currentAccount);
    }

    @Test
    public void testGetCurrentAccount_ShouldReturnNull_WhenNoAuthentication() {
        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNull("Current account should be null when no authentication", currentAccount);
    }

    @Test
    public void testGetCurrentAccount_ShouldReturnNull_WhenAuthenticationHasNonAccountPrincipal() {
        // Given
        String nonAccountPrincipal = "someString";
        Authentication authentication = new UsernamePasswordAuthenticationToken(nonAccountPrincipal, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNull("Current account should be null when principal is not an Account", currentAccount);
    }

    @Test
    public void testGetCurrentAccount_ShouldReturnNull_WhenAuthenticationIsNull() {
        // Given
        SecurityContextHolder.getContext().setAuthentication(null);

        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNull("Current account should be null when authentication is null", currentAccount);
    }

    // ==================== Integration Tests ====================

    @Test
    public void testSigninAndGetCurrentAccount_ShouldWorkTogether() {
        // When
        AccountUtils.signin(testAccount);
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match the signed in account", testAccount, currentAccount);
        assertEquals("Account ID should match", TEST_ID, currentAccount.getId());
        assertEquals("Account email should match", TEST_EMAIL, currentAccount.getEmail());
    }

    @Test
    public void testAuthenticationTokenForAndSignin_ShouldWorkTogether() {
        // Given
        Authentication authentication = AccountUtils.authenticationTokenFor(testAccount);

        // When
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match the test account", testAccount, currentAccount);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testSignin_ShouldHandleNullAccount() {
        // When
        AccountUtils.signin(null);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Authentication should not be null", authentication);
        assertNull("Principal should be null", authentication.getPrincipal());
    }

    @Test
    public void testGetCurrentAccount_ShouldHandleNullAuthenticationPrincipal() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNull("Current account should be null when principal is null", currentAccount);
    }

    // ==================== Security Context Tests ====================

    @Test
    public void testSecurityContextIsolation_ShouldNotAffectOtherTests() {
        // Given
        AccountUtils.signin(testAccount);
        assertNotNull("Authentication should be set", SecurityContextHolder.getContext().getAuthentication());

        // When - This test should not affect other tests due to @After cleanup
        // The actual verification happens in other tests that should still pass
    }
}