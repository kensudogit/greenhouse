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

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.test.transaction.Transactional;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountRepositoryTest {

	private JdbcAccountRepository accountRepository;
	private JdbcTemplate jdbcTemplate;

	// Test data constants
	private static final String TEST_USERNAME = "kdonald";
	private static final String TEST_PASSWORD = "password";
	private static final String TEST_EMAIL = "cwalls@vmware.com";
	private static final String TEST_USERNAME_2 = "habuma";
	private static final long TEST_ACCOUNT_ID = 1L;
	private static final long TEST_ACCOUNT_ID_2 = 2L;

	public JdbcAccountRepositoryTest() {
		EmbeddedDatabase db = new GreenhouseTestDatabaseBuilder().member().testData(getClass()).getDatabase();
		transactional = new Transactional(db);
		jdbcTemplate = new JdbcTemplate(db);
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(),
				"http://localhost:8080/members/{profileKey}");
		accountRepository = new JdbcAccountRepository(jdbcTemplate, NoOpPasswordEncoder.getInstance(), accountMapper);
	}

	// ==================== Account Creation Tests ====================

	@Test
	public void testCreateAccount_ShouldCreateNewAccount_WhenValidPersonProvided() throws EmailAlreadyOnFileException {
		// Given
		Person person = new Person("Jack", "Black", "jack@black.com", "foobie", Gender.MALE,
				new LocalDate(1977, 12, 1));

		// When
		Account account = accountRepository.createAccount(person);

		// Then
		assertEquals("Account ID should be 3", 3L, (long) account.getId());
		assertEquals("Full name should match", "Jack Black", account.getFullName());
		assertEquals("Email should match", "jack@black.com", account.getEmail());
		assertEquals("Profile URL should be correct", "http://localhost:8080/members/3", account.getProfileUrl());
		assertEquals("Picture URL should be correct", "http://localhost:8080/resources/profile-pics/male/small.jpg",
				account.getPictureUrl());
	}

	// ==================== Authentication Tests ====================

	@Test
	public void testAuthenticate_ShouldReturnAccount_WhenValidCredentialsProvided()
			throws SignInNotFoundException, InvalidPasswordException {
		// When
		Account account = accountRepository.authenticate(TEST_USERNAME, TEST_PASSWORD);

		// Then
		assertEquals("Account full name should match", "Keith Donald", account.getFullName());
	}

	@Test(expected = InvalidPasswordException.class)
	public void testAuthenticate_ShouldThrowException_WhenInvalidPasswordProvided()
			throws SignInNotFoundException, InvalidPasswordException {
		// When
		accountRepository.authenticate(TEST_USERNAME, "bogus");

		// Then - Exception should be thrown
	}

	// ==================== Account Finding Tests ====================

	@Test
	public void testFindById_ShouldReturnAccount_WhenValidIdProvided() {
		// When
		Account account = accountRepository.findById(TEST_ACCOUNT_ID);

		// Then
		assertExpectedAccount(account);
	}

	@Test
	public void testFindProfileReferencesByIds_ShouldReturnReferences_WhenValidIdsProvided() {
		// Given
		List<Long> accountIds = Arrays.asList(TEST_ACCOUNT_ID, TEST_ACCOUNT_ID_2);

		// When
		List<ProfileReference> references = accountRepository.findProfileReferencesByIds(accountIds);

		// Then
		assertEquals("Should return 2 profile references", 2, references.size());
	}

	@Test
	public void testFindBySignin_ShouldReturnAccount_WhenValidEmailProvided() throws Exception {
		// When
		Account account = accountRepository.findBySignin(TEST_EMAIL);

		// Then
		assertExpectedAccount(account);
	}

	@Test
	public void testFindBySignin_ShouldReturnAccount_WhenValidUsernameProvided() throws Exception {
		// When
		Account account = accountRepository.findBySignin(TEST_USERNAME_2);

		// Then
		assertExpectedAccount(account);
	}

	// ==================== Error Handling Tests ====================

	@Test(expected = SignInNotFoundException.class)
	public void testFindBySignin_ShouldThrowException_WhenUsernameNotFound() throws Exception {
		// When
		accountRepository.findBySignin("strangerdanger");

		// Then - Exception should be thrown
	}

	@Test(expected = SignInNotFoundException.class)
	public void testFindBySignin_ShouldThrowException_WhenEmailNotFound() throws Exception {
		// When
		accountRepository.findBySignin("stranger@danger.com");

		// Then - Exception should be thrown
	}

	// ==================== Helper Methods ====================

	private void assertExpectedAccount(Account account) {
		assertEquals("First name should match", "Craig", account.getFirstName());
		assertEquals("Last name should match", "Walls", account.getLastName());
		assertEquals("Full name should match", "Craig Walls", account.getFullName());
		assertEquals("Email should match", "cwalls@vmware.com", account.getEmail());
		assertEquals("Username should match", "habuma", account.getUsername());
		assertEquals("Profile URL should match", "http://localhost:8080/members/habuma", account.getProfileUrl());
		assertEquals("Picture URL should match", "http://localhost:8080/resources/profile-pics/male/small.jpg",
				account.getPictureUrl());
	}

	@Rule
	public Transactional transactional;
}