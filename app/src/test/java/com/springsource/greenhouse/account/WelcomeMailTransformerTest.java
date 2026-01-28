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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.signup.WelcomeMailTransformer;

public class WelcomeMailTransformerTest {

	// Test data constants
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final long TEST_ACCOUNT_ID = 1L;
	private static final String TEST_FIRST_NAME = "Roy";
	private static final String TEST_LAST_NAME = "Clarkson";
	private static final String TEST_EMAIL = "rclarkson@vmware.com";
	private static final String TEST_USERNAME = "rclarkson";
	private static final String TEST_PICTURE_URL = "http://foo.com/bar.jpg";
	private static final String EXPECTED_FROM = "Greenhouse <noreply@springsource.com>";
	private static final String EXPECTED_SUBJECT = "Welcome to the Greenhouse!";
	private static final String EXPECTED_PROFILE_URL = "http://greenhouse.springsource.org/members/rclarkson";

	// ==================== Welcome Mail Tests ====================

	@Test
	public void testWelcomeMail_ShouldCreateCorrectMailMessage_WhenValidAccountProvided() {
		// Given
		WelcomeMailTransformer transformer = new WelcomeMailTransformer();
		Account account = createTestAccount();

		// When
		SimpleMailMessage welcomeMail = (SimpleMailMessage) transformer.welcomeMail(account);

		// Then
		assertEquals("To email should match", TEST_EMAIL, welcomeMail.getTo()[0]);
		assertEquals("From email should match", EXPECTED_FROM, welcomeMail.getFrom());
		assertEquals("Subject should match", EXPECTED_SUBJECT, welcomeMail.getSubject());

		String mailText = welcomeMail.getText();
		String expectedProfileText = "View your member profile at:" + NEWLINE + EXPECTED_PROFILE_URL;
		assertTrue("Mail text should contain profile URL", mailText.contains(expectedProfileText));
	}

	// ==================== Helper Methods ====================

	private Account createTestAccount() {
		return new Account(TEST_ACCOUNT_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_USERNAME,
				TEST_PICTURE_URL, new UriTemplate("http://greenhouse.springsource.org/members/{id}"));
	}
}
