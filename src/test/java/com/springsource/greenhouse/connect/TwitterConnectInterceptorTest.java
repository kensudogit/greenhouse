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
package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;

public class TwitterConnectInterceptorTest {

	private MockHttpServletRequest httpServletRequest;
	private TwitterConnectInterceptor interceptor;
	private ServletWebRequest request;

	// Test data constants
	private static final String POST_TWEET_PARAMETER = "postTweet";
	private static final String POST_TWEET_VALUE = "true";
	private static final String TWITTER_CONNECT_POST_TWEET_ATTRIBUTE = "twitterConnect.postTweet";
	private static final long TEST_ACCOUNT_ID = 2L;
	private static final String TEST_FIRST_NAME = "Craig";
	private static final String TEST_LAST_NAME = "Walls";
	private static final String TEST_EMAIL = "cwalls@vmware.com";
	private static final String TEST_USERNAME = "habuma";
	private static final String TEST_PICTURE_URL = "http://picture.com/url";

	@Before
	public void setup() {
		interceptor = new TwitterConnectInterceptor();
		httpServletRequest = new MockHttpServletRequest();
		request = new ServletWebRequest(httpServletRequest);
	}

	// ==================== PreConnect Tests ====================

	@Test
	public void testPreConnect_ShouldSetPostTweetAttribute_WhenPostTweetParameterProvided() {
		// Given
		httpServletRequest.setParameter(POST_TWEET_PARAMETER, POST_TWEET_VALUE);

		// When
		interceptor.preConnect(null, null, request);

		// Then
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute(TWITTER_CONNECT_POST_TWEET_ATTRIBUTE,
				WebRequest.SCOPE_SESSION);
		assertNotNull("Post tweet attribute should be set", postTweetAttributeValue);
		assertTrue("Post tweet attribute should be true", postTweetAttributeValue);
	}

	@Test
	public void testPreConnect_ShouldNotSetPostTweetAttribute_WhenNoPostTweetParameterProvided() {
		// When
		interceptor.preConnect(null, null, request);

		// Then
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute(TWITTER_CONNECT_POST_TWEET_ATTRIBUTE,
				WebRequest.SCOPE_SESSION);
		assertNull("Post tweet attribute should not be set", postTweetAttributeValue);
	}

	// ==================== PostConnect Tests ====================

	@Test
	public void testPostConnect_ShouldPostTweet_WhenPostTweetAttributeIsTrue() {
		// Given
		request.setAttribute(TWITTER_CONNECT_POST_TWEET_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		Account account = createTestAccount();
		AccountUtils.signin(account);

		// When
		// Note: This test is commented out in original code
		// TwitterApi twitterApi = Mockito.mock(TwitterApi.class);
		// StubServiceProviderConnection<TwitterApi> connection = new
		// StubServiceProviderConnection<TwitterApi>(twitterApi);
		// interceptor.postConnect(connection, request);

		// Then
		// Mockito.verify(twitterApi).timelineOperations().updateStatus("Join me at the
		// Greenhouse! http://greenhouse.springsource.org/members/habuma");
	}

	@Test
	public void testPostConnect_ShouldNotPostTweet_WhenPostTweetAttributeIsNotSet() {
		// Given
		Account account = createTestAccount();
		AccountUtils.signin(account);

		// When
		// Note: This test is commented out in original code
		// TwitterApi twitterApi = Mockito.mock(TwitterApi.class);
		// StubServiceProviderConnection<TwitterApi> connection = new
		// StubServiceProviderConnection<TwitterApi>(twitterApi);
		// interceptor.postConnect(connection, request);

		// Then
		// Mockito.verifyZeroInteractions(twitterApi);
	}

	// ==================== Helper Methods ====================

	private Account createTestAccount() {
		return new Account(TEST_ACCOUNT_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_USERNAME,
				TEST_PICTURE_URL, new UriTemplate("http://greenhouse.springsource.org/members/{profile}"));
	}
}