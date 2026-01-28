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
package com.springsource.greenhouse.signup;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.result.MockMvcResultMatchers;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Person;

public class SignupControllerTest {

	// Test data constants
	private static final String VALID_SIGNUP_JSON = "{\"first-name\":\"Roy\",\"last-name\":\"Clarkson\",\"email\":\"roy@clarkson.com\",\"confirm-email\":\"roy@clarkson.com\",\"gender\":\"M\",\"birthdate\":{\"month\":7,\"day\":8,\"year\":1976},\"password\":\"letmein\"}";
	private static final String INVALID_FIRST_NAME_JSON = "{\"first-name\":null,\"last-name\":\"Clarkson\",\"email\":\"roy@clarkson.com\",\"confirm-email\":\"roy@clarkson.com\",\"gender\":\"M\",\"birthdate\":{\"month\":7,\"day\":8,\"year\":1976},\"password\":\"letmein\"}";
	private static final String MISMATCHED_EMAIL_JSON = "{\"first-name\":\"Roy\",\"last-name\":\"Clarkson\",\"email\":\"roy@clarkson.com\",\"confirm-email\":\"rclarkson@vmware.com\",\"gender\":\"M\",\"birthdate\":{\"month\":7,\"day\":8,\"year\":1976},\"password\":\"letmein\"}";

	// ==================== Successful Signup Tests ====================

	@Test
	public void testSignupFromApi_ShouldReturnCreated_WhenValidSignupDataProvided() throws Exception {
		// Given
		AccountRepository accountRepository = mock(AccountRepository.class);
		SignedUpGateway gateway = mock(SignedUpGateway.class);
		SignupController signupController = new SignupController(accountRepository, gateway);
		MockMvc mockMvc = standaloneSetup(signupController).build();

		// When
		mockMvc.perform(post("/signup").contentType(APPLICATION_JSON).body(VALID_SIGNUP_JSON.getBytes()))

				// Then
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.equalTo("Account created")));
	}

	// ==================== Error Handling Tests ====================

	@Test
	public void testSignupFromApi_ShouldReturnBadRequest_WhenDuplicateEmailProvided() throws Exception {
		// Given
		AccountRepository accountRepository = mock(AccountRepository.class);
		when(accountRepository.createAccount(any(Person.class)))
				.thenThrow(new EmailAlreadyOnFileException("roy@clarkson.com"));
		SignedUpGateway gateway = mock(SignedUpGateway.class);
		SignupController signupController = new SignupController(accountRepository, gateway);
		MockMvc mockMvc = standaloneSetup(signupController).build();

		// When
		mockMvc.perform(post("/signup").contentType(APPLICATION_JSON).body(VALID_SIGNUP_JSON.getBytes()))

				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.equalTo("Account creation error")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].field", Matchers.equalTo("email")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].code", Matchers.equalTo("account.duplicateEmail")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].message", Matchers.equalTo("already on file")));
	}

	@Test
	public void testSignupFromApi_ShouldReturnBadRequest_WhenValidationErrorsOccur() throws Exception {
		// Given
		AccountRepository accountRepository = mock(AccountRepository.class);
		when(accountRepository.createAccount(any(Person.class)))
				.thenThrow(new EmailAlreadyOnFileException("roy@clarkson.com"));
		SignedUpGateway gateway = mock(SignedUpGateway.class);
		SignupController signupController = new SignupController(accountRepository, gateway);
		MockMvc mockMvc = standaloneSetup(signupController).build();

		// When
		mockMvc.perform(post("/signup").contentType(APPLICATION_JSON).body(INVALID_FIRST_NAME_JSON.getBytes()))

				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.equalTo("Validation error")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].field", Matchers.equalTo("firstName")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].code", Matchers.equalTo("NotEmpty")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].message", Matchers.equalTo("may not be empty")));
	}

	@Test
	public void testSignupFromApi_ShouldReturnBadRequest_WhenEmailsDoNotMatch() throws Exception {
		// Given
		AccountRepository accountRepository = mock(AccountRepository.class);
		when(accountRepository.createAccount(any(Person.class)))
				.thenThrow(new EmailAlreadyOnFileException("roy@clarkson.com"));
		SignedUpGateway gateway = mock(SignedUpGateway.class);
		SignupController signupController = new SignupController(accountRepository, gateway);
		MockMvc mockMvc = standaloneSetup(signupController).build();

		// When
		mockMvc.perform(post("/signup").contentType(APPLICATION_JSON).body(MISMATCHED_EMAIL_JSON.getBytes()))

				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("message", Matchers.equalTo("Validation error")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].field", Matchers.equalTo("email")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].code", Matchers.equalTo("Confirm")))
				.andExpect(MockMvcResultMatchers.jsonPath("errors[0].message",
						Matchers.equalTo("does not match confirmation email")));
	}
}
