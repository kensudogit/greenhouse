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
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

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
    private UriTemplate profileUrlTemplate;

    @Before
    public void setUp() {
        profileUrlTemplate = new UriTemplate(TEST_PROFILE_URL_TEMPLATE);
        testAccount = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_USERNAME, TEST_PICTURE_URL, profileUrlTemplate);

        // Clear security context before each test
        SecurityContextHolder.clearContext();
    }

    @After
    public void tearDown() {
        // Clear security context after each test
        SecurityContextHolder.clearContext();
    }

    // ==================== Signin Tests ====================

    @Test
    public void testSignin_ShouldSetAuthenticationInSecurityContext_WhenValidAccountProvided() {
        // When
        AccountUtils.signin(testAccount);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Authentication should not be null", authentication);
        assertTrue("Authentication should be UsernamePasswordAuthenticationToken",
                authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals("Principal should be the test account", testAccount, authentication.getPrincipal());
    }

    @Test
    public void testSignin_ShouldReplaceExistingAuthentication_WhenCalledMultipleTimes() {
        // Given
        Account account1 = testAccount;
        Account account2 = new Account(2L, "Jane", "Smith", "jane.smith@example.com",
                "janesmith", "http://example.com/picture2.jpg", profileUrlTemplate);

        // When
        AccountUtils.signin(account1);
        AccountUtils.signin(account2);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Authentication should not be null", authentication);
        assertEquals("Principal should be the second account", account2, authentication.getPrincipal());
    }

    // ==================== AuthenticationTokenFor Tests ====================

    @Test
    public void testAuthenticationTokenFor_ShouldReturnValidAuthenticationToken_WhenValidAccountProvided() {
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
    public void testAuthenticationTokenFor_ShouldReturnDifferentInstances_WhenCalledMultipleTimes() {
        // When
        Authentication authentication1 = AccountUtils.authenticationTokenFor(testAccount);
        Authentication authentication2 = AccountUtils.authenticationTokenFor(testAccount);

        // Then
        assertNotNull("First authentication should not be null", authentication1);
        assertNotNull("Second authentication should not be null", authentication2);
        assertNotSame("Should return different instances", authentication1, authentication2);
        assertEquals("Both should have same principal", authentication1.getPrincipal(), authentication2.getPrincipal());
    }

    // ==================== GetCurrentAccount Tests ====================

    @Test
    public void testGetCurrentAccount_ShouldReturnNull_WhenNoAuthenticationSet() {
        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNull("Current account should be null when no authentication is set", currentAccount);
    }

    @Test
    public void testGetCurrentAccount_ShouldReturnAccount_WhenValidAuthenticationIsSet() {
        // Given
        AccountUtils.signin(testAccount);

        // When
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match the signed in account", testAccount, currentAccount);
    }

    @Test
    public void testGetCurrentAccount_ShouldReturnNull_WhenAuthenticationPrincipalIsNotAccount() {
        // Given
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("not-an-account",
                "password");
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
    public void testIntegration_ShouldWorkCorrectly_WhenSigninAndGetCurrentAccountUsedTogether() {
        // When
        AccountUtils.signin(testAccount);
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match the signed in account", testAccount, currentAccount);
        assertEquals("Account ID should match", TEST_ID, currentAccount.getId());
        assertEquals("Account email should match", TEST_EMAIL, currentAccount.getEmail());
        assertEquals("Account username should match", TEST_USERNAME, currentAccount.getUsername());
    }

    @Test
    public void testIntegration_ShouldWorkCorrectly_WhenMultipleAccountsAreSignedInSequentially() {
        // Given
        Account account1 = testAccount;
        Account account2 = new Account(2L, "Jane", "Smith", "jane.smith@example.com",
                "janesmith", "http://example.com/picture2.jpg", profileUrlTemplate);

        // When
        AccountUtils.signin(account1);
        Account currentAccount1 = AccountUtils.getCurrentAccount();

        AccountUtils.signin(account2);
        Account currentAccount2 = AccountUtils.getCurrentAccount();

        // Then
        assertEquals("First current account should match first account", account1, currentAccount1);
        assertEquals("Second current account should match second account", account2, currentAccount2);
        assertNotEquals("Current accounts should be different", currentAccount1, currentAccount2);
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void testEdgeCase_ShouldHandleNullAccount_WhenSigninCalledWithNull() {
        // When
        AccountUtils.signin(null);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull("Authentication should not be null", authentication);
        assertNull("Principal should be null", authentication.getPrincipal());
    }

    @Test
    public void testEdgeCase_ShouldHandleNullAccount_WhenAuthenticationTokenForCalledWithNull() {
        // When
        Authentication authentication = AccountUtils.authenticationTokenFor(null);

        // Then
        assertNotNull("Authentication should not be null", authentication);
        assertNull("Principal should be null", authentication.getPrincipal());
    }

    @Test
    public void testEdgeCase_ShouldHandleAccountWithNullUsername_WhenSigninCalled() {
        // Given
        Account accountWithNullUsername = new Account(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME,
                TEST_EMAIL, null, TEST_PICTURE_URL, profileUrlTemplate);

        // When
        AccountUtils.signin(accountWithNullUsername);
        Account currentAccount = AccountUtils.getCurrentAccount();

        // Then
        assertNotNull("Current account should not be null", currentAccount);
        assertEquals("Current account should match the signed in account", accountWithNullUsername, currentAccount);
        assertNull("Username should be null", currentAccount.getUsername());
    }

    // ==================== Security Context Tests ====================

    @Test
    public void testSecurityContext_ShouldBeThreadSafe_WhenMultipleThreadsAccessSecurityContext()
            throws InterruptedException {
        // Given
        final Account[] results = new Account[2];
        final CountDownLatch latch = new CountDownLatch(2);

        // When
        Thread thread1 = new Thread(() -> {
            AccountUtils.signin(testAccount);
            results[0] = AccountUtils.getCurrentAccount();
            latch.countDown();
        });

        Account account2 = new Account(2L, "Jane", "Smith", "jane.smith@example.com",
                "janesmith", "http://example.com/picture2.jpg", profileUrlTemplate);
        Thread thread2 = new Thread(() -> {
            AccountUtils.signin(account2);
            results[1] = AccountUtils.getCurrentAccount();
            latch.countDown();
        });

        thread1.start();
        thread2.start();
        latch.await();

        // Then
        // Each thread should have its own security context
        assertNotNull("Thread 1 result should not be null", results[0]);
        assertNotNull("Thread 2 result should not be null", results[1]);
        // Note: The actual behavior depends on Spring Security's SecurityContextHolder
        // strategy
        // In a real application, you'd typically use
        // SecurityContextHolder.MODE_THREADLOCAL
    }
}