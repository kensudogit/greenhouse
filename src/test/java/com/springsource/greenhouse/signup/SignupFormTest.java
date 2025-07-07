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

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.social.connect.UserProfile;

import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;

/**
 * Test class for {@link SignupForm}.
 * 
 * @author Test Author
 */
public class SignupFormTest {

    // Test data constants
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_CONFIRM_EMAIL = "john.doe@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final Gender TEST_GENDER = Gender.MALE;
    private static final Integer TEST_MONTH = 6;
    private static final Integer TEST_DAY = 15;
    private static final Integer TEST_YEAR = 1990;

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreateSignupForm() {
        // When
        SignupForm form = new SignupForm();

        // Then
        assertNotNull("SignupForm should not be null", form);
    }

    // ==================== Getter and Setter Tests ====================

    @Test
    public void testGetSetFirstName_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setFirstName(TEST_FIRST_NAME);
        String result = form.getFirstName();

        // Then
        assertEquals("First name should match", TEST_FIRST_NAME, result);
    }

    @Test
    public void testGetSetLastName_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setLastName(TEST_LAST_NAME);
        String result = form.getLastName();

        // Then
        assertEquals("Last name should match", TEST_LAST_NAME, result);
    }

    @Test
    public void testGetSetEmail_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setEmail(TEST_EMAIL);
        String result = form.getEmail();

        // Then
        assertEquals("Email should match", TEST_EMAIL, result);
    }

    @Test
    public void testGetSetConfirmEmail_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setConfirmEmail(TEST_CONFIRM_EMAIL);
        String result = form.getConfirmEmail();

        // Then
        assertEquals("Confirm email should match", TEST_CONFIRM_EMAIL, result);
    }

    @Test
    public void testGetSetPassword_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setPassword(TEST_PASSWORD);
        String result = form.getPassword();

        // Then
        assertEquals("Password should match", TEST_PASSWORD, result);
    }

    @Test
    public void testGetSetGender_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setGender(TEST_GENDER);
        Gender result = form.getGender();

        // Then
        assertEquals("Gender should match", TEST_GENDER, result);
    }

    @Test
    public void testGetSetMonth_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setMonth(TEST_MONTH);
        Integer result = form.getMonth();

        // Then
        assertEquals("Month should match", TEST_MONTH, result);
    }

    @Test
    public void testGetSetDay_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setDay(TEST_DAY);
        Integer result = form.getDay();

        // Then
        assertEquals("Day should match", TEST_DAY, result);
    }

    @Test
    public void testGetSetYear_ShouldWorkCorrectly() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setYear(TEST_YEAR);
        Integer result = form.getYear();

        // Then
        assertEquals("Year should match", TEST_YEAR, result);
    }

    // ==================== Null Value Tests ====================

    @Test
    public void testGetSetFirstName_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setFirstName(null);
        String result = form.getFirstName();

        // Then
        assertNull("First name should be null", result);
    }

    @Test
    public void testGetSetLastName_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setLastName(null);
        String result = form.getLastName();

        // Then
        assertNull("Last name should be null", result);
    }

    @Test
    public void testGetSetEmail_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setEmail(null);
        String result = form.getEmail();

        // Then
        assertNull("Email should be null", result);
    }

    @Test
    public void testGetSetConfirmEmail_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setConfirmEmail(null);
        String result = form.getConfirmEmail();

        // Then
        assertNull("Confirm email should be null", result);
    }

    @Test
    public void testGetSetPassword_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setPassword(null);
        String result = form.getPassword();

        // Then
        assertNull("Password should be null", result);
    }

    @Test
    public void testGetSetGender_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setGender(null);
        Gender result = form.getGender();

        // Then
        assertNull("Gender should be null", result);
    }

    @Test
    public void testGetSetMonth_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setMonth(null);
        Integer result = form.getMonth();

        // Then
        assertNull("Month should be null", result);
    }

    @Test
    public void testGetSetDay_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setDay(null);
        Integer result = form.getDay();

        // Then
        assertNull("Day should be null", result);
    }

    @Test
    public void testGetSetYear_ShouldHandleNullValue() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setYear(null);
        Integer result = form.getYear();

        // Then
        assertNull("Year should be null", result);
    }

    // ==================== Empty String Tests ====================

    @Test
    public void testGetSetFirstName_ShouldHandleEmptyString() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setFirstName("");
        String result = form.getFirstName();

        // Then
        assertEquals("First name should be empty string", "", result);
    }

    @Test
    public void testGetSetLastName_ShouldHandleEmptyString() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setLastName("");
        String result = form.getLastName();

        // Then
        assertEquals("Last name should be empty string", "", result);
    }

    @Test
    public void testGetSetEmail_ShouldHandleEmptyString() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setEmail("");
        String result = form.getEmail();

        // Then
        assertEquals("Email should be empty string", "", result);
    }

    @Test
    public void testGetSetConfirmEmail_ShouldHandleEmptyString() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setConfirmEmail("");
        String result = form.getConfirmEmail();

        // Then
        assertEquals("Confirm email should be empty string", "", result);
    }

    @Test
    public void testGetSetPassword_ShouldHandleEmptyString() {
        // Given
        SignupForm form = new SignupForm();

        // When
        form.setPassword("");
        String result = form.getPassword();

        // Then
        assertEquals("Password should be empty string", "", result);
    }

    // ==================== Set Birthdate Tests ====================

    @Test
    public void testSetBirthdate_ShouldSetCorrectValues() {
        // Given
        SignupForm form = new SignupForm();
        List<Integer> birthdateFields = Arrays.asList(TEST_MONTH, TEST_DAY, TEST_YEAR);

        // When
        form.setBirthdate(birthdateFields);

        // Then
        assertEquals("Month should be set correctly", TEST_MONTH, form.getMonth());
        assertEquals("Day should be set correctly", TEST_DAY, form.getDay());
        assertEquals("Year should be set correctly", TEST_YEAR, form.getYear());
    }

    @Test
    public void testSetBirthdate_ShouldHandleNullList() {
        // Given
        SignupForm form = new SignupForm();

        // When & Then - Should throw NullPointerException
        try {
            form.setBirthdate(null);
            fail("Should throw NullPointerException when birthdate list is null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSetBirthdate_ShouldHandleEmptyList() {
        // Given
        SignupForm form = new SignupForm();
        List<Integer> birthdateFields = Arrays.asList();

        // When & Then - Should throw IndexOutOfBoundsException
        try {
            form.setBirthdate(birthdateFields);
            fail("Should throw IndexOutOfBoundsException when birthdate list is empty");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetBirthdate_ShouldHandleIncompleteList() {
        // Given
        SignupForm form = new SignupForm();
        List<Integer> birthdateFields = Arrays.asList(TEST_MONTH, TEST_DAY); // Missing year

        // When & Then - Should throw IndexOutOfBoundsException
        try {
            form.setBirthdate(birthdateFields);
            fail("Should throw IndexOutOfBoundsException when birthdate list is incomplete");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // ==================== Create Person Tests ====================

    @Test
    public void testCreatePerson_ShouldCreatePersonWithCorrectValues() {
        // Given
        SignupForm form = new SignupForm();
        form.setFirstName(TEST_FIRST_NAME);
        form.setLastName(TEST_LAST_NAME);
        form.setEmail(TEST_EMAIL);
        form.setPassword(TEST_PASSWORD);
        form.setGender(TEST_GENDER);
        form.setMonth(TEST_MONTH);
        form.setDay(TEST_DAY);
        form.setYear(TEST_YEAR);

        // When
        Person person = form.createPerson();

        // Then
        assertNotNull("Person should not be null", person);
        assertEquals("Person first name should match", TEST_FIRST_NAME, person.getFirstName());
        assertEquals("Person last name should match", TEST_LAST_NAME, person.getLastName());
        assertEquals("Person email should match", TEST_EMAIL, person.getEmail());
        assertEquals("Person password should match", TEST_PASSWORD, person.getPassword());
        assertEquals("Person gender should match", TEST_GENDER, person.getGender());
        assertEquals("Person birth date should match", new LocalDate(TEST_YEAR, TEST_MONTH, TEST_DAY),
                person.getBirthDate());
    }

    @Test
    public void testCreatePerson_ShouldHandleNullValues() {
        // Given
        SignupForm form = new SignupForm();
        form.setFirstName(null);
        form.setLastName(null);
        form.setEmail(null);
        form.setPassword(null);
        form.setGender(null);
        form.setMonth(null);
        form.setDay(null);
        form.setYear(null);

        // When & Then - Should throw NullPointerException
        try {
            form.createPerson();
            fail("Should throw NullPointerException when required fields are null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // ==================== From Provider User Tests ====================

    @Test
    public void testFromProviderUser_ShouldCreateFormWithCorrectValues() {
        // Given
        UserProfile providerUser = new UserProfile("provider-id", "John", "Doe", "john.doe@example.com", "John Doe");

        // When
        SignupForm form = SignupForm.fromProviderUser(providerUser);

        // Then
        assertNotNull("SignupForm should not be null", form);
        assertEquals("First name should match", "John", form.getFirstName());
        assertEquals("Last name should match", "Doe", form.getLastName());
        assertEquals("Email should match", "john.doe@example.com", form.getEmail());
        assertEquals("Confirm email should match", "john.doe@example.com", form.getConfirmEmail());
    }

    @Test
    public void testFromProviderUser_ShouldHandleNullProviderUser() {
        // When & Then - Should throw NullPointerException
        try {
            SignupForm.fromProviderUser(null);
            fail("Should throw NullPointerException when provider user is null");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFromProviderUser_ShouldHandleProviderUserWithNullValues() {
        // Given
        UserProfile providerUser = new UserProfile("provider-id", null, null, null, null);

        // When
        SignupForm form = SignupForm.fromProviderUser(providerUser);

        // Then
        assertNotNull("SignupForm should not be null", form);
        assertNull("First name should be null", form.getFirstName());
        assertNull("Last name should be null", form.getLastName());
        assertNull("Email should be null", form.getEmail());
        assertNull("Confirm email should be null", form.getConfirmEmail());
    }

    @Test
    public void testFromProviderUser_ShouldHandleProviderUserWithEmptyValues() {
        // Given
        UserProfile providerUser = new UserProfile("provider-id", "", "", "", "");

        // When
        SignupForm form = SignupForm.fromProviderUser(providerUser);

        // Then
        assertNotNull("SignupForm should not be null", form);
        assertEquals("First name should be empty", "", form.getFirstName());
        assertEquals("Last name should be empty", "", form.getLastName());
        assertEquals("Email should be empty", "", form.getEmail());
        assertEquals("Confirm email should be empty", "", form.getConfirmEmail());
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testGetSetFirstName_ShouldHandleSpecialCharacters() {
        // Given
        SignupForm form = new SignupForm();
        String specialName = "José María O'Connor-Smith";

        // When
        form.setFirstName(specialName);
        String result = form.getFirstName();

        // Then
        assertEquals("First name should handle special characters", specialName, result);
    }

    @Test
    public void testGetSetEmail_ShouldHandleSpecialEmailFormats() {
        // Given
        SignupForm form = new SignupForm();
        String specialEmail = "test+tag@example.co.uk";

        // When
        form.setEmail(specialEmail);
        String result = form.getEmail();

        // Then
        assertEquals("Email should handle special formats", specialEmail, result);
    }

    @Test
    public void testGetSetPassword_ShouldHandleSpecialCharacters() {
        // Given
        SignupForm form = new SignupForm();
        String specialPassword = "P@ssw0rd!123";

        // When
        form.setPassword(specialPassword);
        String result = form.getPassword();

        // Then
        assertEquals("Password should handle special characters", specialPassword, result);
    }

    @Test
    public void testGetSetMonth_ShouldHandleBoundaryValues() {
        // Given
        SignupForm form = new SignupForm();

        // Test minimum value
        form.setMonth(1);
        assertEquals("Month should handle minimum value", Integer.valueOf(1), form.getMonth());

        // Test maximum value
        form.setMonth(12);
        assertEquals("Month should handle maximum value", Integer.valueOf(12), form.getMonth());

        // Test zero value
        form.setMonth(0);
        assertEquals("Month should handle zero value", Integer.valueOf(0), form.getMonth());

        // Test negative value
        form.setMonth(-1);
        assertEquals("Month should handle negative value", Integer.valueOf(-1), form.getMonth());
    }

    @Test
    public void testGetSetDay_ShouldHandleBoundaryValues() {
        // Given
        SignupForm form = new SignupForm();

        // Test minimum value
        form.setDay(1);
        assertEquals("Day should handle minimum value", Integer.valueOf(1), form.getDay());

        // Test maximum value
        form.setDay(31);
        assertEquals("Day should handle maximum value", Integer.valueOf(31), form.getDay());

        // Test zero value
        form.setDay(0);
        assertEquals("Day should handle zero value", Integer.valueOf(0), form.getDay());

        // Test negative value
        form.setDay(-1);
        assertEquals("Day should handle negative value", Integer.valueOf(-1), form.getDay());
    }

    @Test
    public void testGetSetYear_ShouldHandleBoundaryValues() {
        // Given
        SignupForm form = new SignupForm();

        // Test minimum value
        form.setYear(1900);
        assertEquals("Year should handle minimum value", Integer.valueOf(1900), form.getYear());

        // Test maximum value
        form.setYear(2100);
        assertEquals("Year should handle maximum value", Integer.valueOf(2100), form.getYear());

        // Test zero value
        form.setYear(0);
        assertEquals("Year should handle zero value", Integer.valueOf(0), form.getYear());

        // Test negative value
        form.setYear(-1);
        assertEquals("Year should handle negative value", Integer.valueOf(-1), form.getYear());
    }

    // ==================== Multiple Instances Tests ====================

    @Test
    public void testMultipleInstances_ShouldBeIndependent() {
        // Given
        SignupForm form1 = new SignupForm();
        SignupForm form2 = new SignupForm();

        // When
        form1.setFirstName("John");
        form1.setLastName("Doe");
        form1.setEmail("john@example.com");
        form2.setFirstName("Jane");
        form2.setLastName("Smith");
        form2.setEmail("jane@example.com");

        // Then
        assertEquals("Form1 first name should be John", "John", form1.getFirstName());
        assertEquals("Form1 last name should be Doe", "Doe", form1.getLastName());
        assertEquals("Form1 email should be john@example.com", "john@example.com", form1.getEmail());
        assertEquals("Form2 first name should be Jane", "Jane", form2.getFirstName());
        assertEquals("Form2 last name should be Smith", "Smith", form2.getLastName());
        assertEquals("Form2 email should be jane@example.com", "jane@example.com", form2.getEmail());
    }
}