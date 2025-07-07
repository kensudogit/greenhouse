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
package com.springsource.greenhouse.signup;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;

/**
 * Test class for {@link AccountService}.
 * 
 * @author Test Author
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    // Test data constants
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final Gender TEST_GENDER = Gender.MALE;
    private static final LocalDate TEST_BIRTH_DATE = new LocalDate(1990, 6, 15);

    @Before
    public void setUp() {
        accountService = new AccountService(accountRepository);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateAccountService() {
        // When
        AccountService service = new AccountService(accountRepository);

        // Then
        assertNotNull("AccountService should not be null", service);
    }

    @Test
    public void testConstructor_ShouldAcceptNullRepository() {
        // When
        AccountService service = new AccountService(null);

        // Then
        assertNotNull("AccountService should not be null", service);
    }

    // ==================== Create Account Tests ====================

    @Test
    public void testCreateAccount_ShouldCreateAccountSuccessfully() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        verify(accountRepository, times(1)).createAccount(person);
    }

    @Test
    public void testCreateAccount_ShouldReturnCorrectAccount() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertEquals("Account ID should match", expectedAccount.getId(), result.getId());
        assertEquals("Account person should match", expectedAccount.getPerson(), result.getPerson());
        assertEquals("Account person first name should match", TEST_FIRST_NAME, result.getPerson().getFirstName());
        assertEquals("Account person last name should match", TEST_LAST_NAME, result.getPerson().getLastName());
        assertEquals("Account person email should match", TEST_EMAIL, result.getPerson().getEmail());
        assertEquals("Account person password should match", TEST_PASSWORD, result.getPerson().getPassword());
        assertEquals("Account person gender should match", TEST_GENDER, result.getPerson().getGender());
        assertEquals("Account person birth date should match", TEST_BIRTH_DATE, result.getPerson().getBirthDate());
    }

    @Test(expected = EmailAlreadyOnFileException.class)
    public void testCreateAccount_ShouldThrowException_WhenEmailAlreadyExists() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);

        when(accountRepository.createAccount(person)).thenThrow(new EmailAlreadyOnFileException(TEST_EMAIL));

        // When
        accountService.createAccount(person);

        // Then - Exception should be thrown
    }

    @Test
    public void testCreateAccount_ShouldHandleNullPerson() throws EmailAlreadyOnFileException {
        // Given
        when(accountRepository.createAccount(null)).thenReturn(null);

        // When
        Account result = accountService.createAccount(null);

        // Then
        assertNull("Account should be null when person is null", result);
        verify(accountRepository, times(1)).createAccount(null);
    }

    @Test
    public void testCreateAccount_ShouldHandlePersonWithNullValues() throws EmailAlreadyOnFileException {
        // Given
        Person personWithNulls = new Person(null, null, null, null, null, null);
        Account expectedAccount = new Account(1L, personWithNulls);

        when(accountRepository.createAccount(personWithNulls)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(personWithNulls);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        verify(accountRepository, times(1)).createAccount(personWithNulls);
    }

    @Test
    public void testCreateAccount_ShouldHandlePersonWithEmptyValues() throws EmailAlreadyOnFileException {
        // Given
        Person personWithEmpties = new Person("", "", "", "", TEST_GENDER, TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, personWithEmpties);

        when(accountRepository.createAccount(personWithEmpties)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(personWithEmpties);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        verify(accountRepository, times(1)).createAccount(personWithEmpties);
    }

    // ==================== Multiple Accounts Tests ====================

    @Test
    public void testCreateAccount_ShouldHandleMultipleAccounts() throws EmailAlreadyOnFileException {
        // Given
        Person person1 = new Person("John", "Doe", "john@example.com", "password1", Gender.MALE, TEST_BIRTH_DATE);
        Person person2 = new Person("Jane", "Smith", "jane@example.com", "password2", Gender.FEMALE, TEST_BIRTH_DATE);

        Account account1 = new Account(1L, person1);
        Account account2 = new Account(2L, person2);

        when(accountRepository.createAccount(person1)).thenReturn(account1);
        when(accountRepository.createAccount(person2)).thenReturn(account2);

        // When
        Account result1 = accountService.createAccount(person1);
        Account result2 = accountService.createAccount(person2);

        // Then
        assertNotNull("First account should not be null", result1);
        assertNotNull("Second account should not be null", result2);
        assertEquals("First account should match", account1, result1);
        assertEquals("Second account should match", account2, result2);
        assertNotEquals("Accounts should be different", result1.getId(), result2.getId());
        verify(accountRepository, times(1)).createAccount(person1);
        verify(accountRepository, times(1)).createAccount(person2);
    }

    // ==================== Repository Interaction Tests ====================

    @Test
    public void testCreateAccount_ShouldCallRepositoryOnce() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        accountService.createAccount(person);

        // Then
        verify(accountRepository, times(1)).createAccount(person);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void testCreateAccount_ShouldPassCorrectPersonToRepository() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(any(Person.class))).thenReturn(expectedAccount);

        // When
        accountService.createAccount(person);

        // Then
        verify(accountRepository, times(1)).createAccount(person);
    }

    // ==================== Exception Handling Tests ====================

    @Test
    public void testCreateAccount_ShouldPropagateRepositoryException() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        RuntimeException repositoryException = new RuntimeException("Repository error");

        when(accountRepository.createAccount(person)).thenThrow(repositoryException);

        // When & Then
        try {
            accountService.createAccount(person);
            fail("Should throw RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Exception should match repository exception", repositoryException, e);
        }
    }

    @Test
    public void testCreateAccount_ShouldHandleRepositoryReturningNull() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);

        when(accountRepository.createAccount(person)).thenReturn(null);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertNull("Account should be null when repository returns null", result);
        verify(accountRepository, times(1)).createAccount(person);
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testCreateAccount_ShouldHandlePersonWithSpecialCharacters() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person("José María", "O'Connor-Smith", "test+tag@example.co.uk", "P@ssw0rd!123",
                TEST_GENDER, TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        assertEquals("Person first name should handle special characters", "José María",
                result.getPerson().getFirstName());
        assertEquals("Person last name should handle special characters", "O'Connor-Smith",
                result.getPerson().getLastName());
        assertEquals("Person email should handle special characters", "test+tag@example.co.uk",
                result.getPerson().getEmail());
        assertEquals("Person password should handle special characters", "P@ssw0rd!123",
                result.getPerson().getPassword());
    }

    @Test
    public void testCreateAccount_ShouldHandlePersonWithUnicodeCharacters() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person("こんにちは", "世界", "test@example.com", "password123", TEST_GENDER, TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        assertEquals("Person first name should handle unicode characters", "こんにちは", result.getPerson().getFirstName());
        assertEquals("Person last name should handle unicode characters", "世界", result.getPerson().getLastName());
    }

    @Test
    public void testCreateAccount_ShouldHandlePersonWithVeryLongValues() throws EmailAlreadyOnFileException {
        // Given
        String longName = "A".repeat(1000);
        String longEmail = "very.long.email.address.that.exceeds.normal.length.but.should.still.be.valid@example.com";
        String longPassword = "P".repeat(1000);

        Person person = new Person(longName, longName, longEmail, longPassword, TEST_GENDER, TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When
        Account result = accountService.createAccount(person);

        // Then
        assertNotNull("Account should not be null", result);
        assertEquals("Account should match expected account", expectedAccount, result);
        assertEquals("Person first name should handle very long values", longName, result.getPerson().getFirstName());
        assertEquals("Person last name should handle very long values", longName, result.getPerson().getLastName());
        assertEquals("Person email should handle very long values", longEmail, result.getPerson().getEmail());
        assertEquals("Person password should handle very long values", longPassword, result.getPerson().getPassword());
    }

    // ==================== Service Lifecycle Tests ====================

    @Test
    public void testAccountService_ShouldBeReusable() throws EmailAlreadyOnFileException {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_GENDER,
                TEST_BIRTH_DATE);
        Account expectedAccount = new Account(1L, person);

        when(accountRepository.createAccount(person)).thenReturn(expectedAccount);

        // When - Call multiple times
        Account result1 = accountService.createAccount(person);
        Account result2 = accountService.createAccount(person);
        Account result3 = accountService.createAccount(person);

        // Then
        assertNotNull("First result should not be null", result1);
        assertNotNull("Second result should not be null", result2);
        assertNotNull("Third result should not be null", result3);
        assertEquals("All results should be equal", result1, result2);
        assertEquals("All results should be equal", result2, result3);
        verify(accountRepository, times(3)).createAccount(person);
    }

    // ==================== Annotation Tests ====================

    @Test
    public void testAccountService_ShouldHaveServiceAnnotation() {
        // When
        AccountService service = new AccountService(accountRepository);

        // Then
        assertTrue("AccountService should have @Service annotation",
                service.getClass().isAnnotationPresent(org.springframework.stereotype.Service.class));
    }

    @Test
    public void testCreateAccountMethod_ShouldHaveTransactionalAnnotation() throws Exception {
        // Given
        AccountService service = new AccountService(accountRepository);

        // When
        java.lang.reflect.Method createAccountMethod = service.getClass().getMethod("createAccount", Person.class);

        // Then
        assertTrue("createAccount method should have @Transactional annotation",
                createAccountMethod
                        .isAnnotationPresent(org.springframework.transaction.annotation.Transactional.class));
    }
}