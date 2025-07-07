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

import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Test class for {@link Person}.
 * 
 * @author Test Author
 */
public class PersonTest {

    // Test data constants
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final Gender TEST_GENDER = Gender.MALE;
    private static final LocalDate TEST_BIRTHDATE = new LocalDate(1990, 1, 15);

    // ==================== Constructor Tests ====================

    @Test
    public void testConstructor_ShouldCreatePerson_WhenValidParametersProvided() {
        // When
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, TEST_BIRTHDATE);

        // Then
        assertNotNull("Person should not be null", person);
        assertEquals("First name should match", TEST_FIRST_NAME, person.getFirstName());
        assertEquals("Last name should match", TEST_LAST_NAME, person.getLastName());
        assertEquals("Email should match", TEST_EMAIL, person.getEmail());
        assertEquals("Password should match", TEST_PASSWORD, person.getPassword());
        assertEquals("Gender should match", TEST_GENDER, person.getGender());
        assertEquals("Birthdate should match", TEST_BIRTHDATE, person.getBirthdate());
    }

    @Test
    public void testConstructor_ShouldCreatePerson_WhenNullValuesProvided() {
        // When
        Person person = new Person(null, null, null, null, null, null);

        // Then
        assertNotNull("Person should not be null", person);
        assertNull("First name should be null", person.getFirstName());
        assertNull("Last name should be null", person.getLastName());
        assertNull("Email should be null", person.getEmail());
        assertNull("Password should be null", person.getPassword());
        assertNull("Gender should be null", person.getGender());
        assertNull("Birthdate should be null", person.getBirthdate());
    }

    @Test
    public void testConstructor_ShouldCreatePerson_WhenEmptyStringsProvided() {
        // When
        Person person = new Person("", "", "", "", TEST_GENDER, TEST_BIRTHDATE);

        // Then
        assertNotNull("Person should not be null", person);
        assertEquals("First name should be empty string", "", person.getFirstName());
        assertEquals("Last name should be empty string", "", person.getLastName());
        assertEquals("Email should be empty string", "", person.getEmail());
        assertEquals("Password should be empty string", "", person.getPassword());
        assertEquals("Gender should match", TEST_GENDER, person.getGender());
        assertEquals("Birthdate should match", TEST_BIRTHDATE, person.getBirthdate());
    }

    // ==================== Getter Tests ====================

    @Test
    public void testGetFirstName_ShouldReturnCorrectFirstName() {
        // Given
        Person person = createTestPerson();

        // When
        String firstName = person.getFirstName();

        // Then
        assertEquals("First name should match", TEST_FIRST_NAME, firstName);
    }

    @Test
    public void testGetLastName_ShouldReturnCorrectLastName() {
        // Given
        Person person = createTestPerson();

        // When
        String lastName = person.getLastName();

        // Then
        assertEquals("Last name should match", TEST_LAST_NAME, lastName);
    }

    @Test
    public void testGetEmail_ShouldReturnCorrectEmail() {
        // Given
        Person person = createTestPerson();

        // When
        String email = person.getEmail();

        // Then
        assertEquals("Email should match", TEST_EMAIL, email);
    }

    @Test
    public void testGetPassword_ShouldReturnCorrectPassword() {
        // Given
        Person person = createTestPerson();

        // When
        String password = person.getPassword();

        // Then
        assertEquals("Password should match", TEST_PASSWORD, password);
    }

    @Test
    public void testGetGender_ShouldReturnCorrectGender() {
        // Given
        Person person = createTestPerson();

        // When
        Gender gender = person.getGender();

        // Then
        assertEquals("Gender should match", TEST_GENDER, gender);
    }

    @Test
    public void testGetBirthdate_ShouldReturnCorrectBirthdate() {
        // Given
        Person person = createTestPerson();

        // When
        LocalDate birthdate = person.getBirthdate();

        // Then
        assertEquals("Birthdate should match", TEST_BIRTHDATE, birthdate);
    }

    // ==================== Gender Tests ====================

    @Test
    public void testGetGender_ShouldReturnFemale_WhenFemaleGenderProvided() {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, Gender.FEMALE, TEST_BIRTHDATE);

        // When
        Gender gender = person.getGender();

        // Then
        assertEquals("Gender should be FEMALE", Gender.FEMALE, gender);
    }

    @Test
    public void testGetGender_ShouldReturnMale_WhenMaleGenderProvided() {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, Gender.MALE, TEST_BIRTHDATE);

        // When
        Gender gender = person.getGender();

        // Then
        assertEquals("Gender should be MALE", Gender.MALE, gender);
    }

    // ==================== Birthdate Tests ====================

    @Test
    public void testGetBirthdate_ShouldReturnCorrectDate_WhenDifferentDateProvided() {
        // Given
        LocalDate differentBirthdate = new LocalDate(1985, 6, 20);
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, differentBirthdate);

        // When
        LocalDate birthdate = person.getBirthdate();

        // Then
        assertEquals("Birthdate should match", differentBirthdate, birthdate);
        assertEquals("Year should be 1985", 1985, birthdate.getYear());
        assertEquals("Month should be 6", 6, birthdate.getMonthOfYear());
        assertEquals("Day should be 20", 20, birthdate.getDayOfMonth());
    }

    @Test
    public void testGetBirthdate_ShouldReturnNull_WhenNullBirthdateProvided() {
        // Given
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, null);

        // When
        LocalDate birthdate = person.getBirthdate();

        // Then
        assertNull("Birthdate should be null", birthdate);
    }

    // ==================== String Content Tests ====================

    @Test
    public void testGetFirstName_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialFirstName = "José María";
        Person person = new Person(specialFirstName, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, TEST_BIRTHDATE);

        // When
        String firstName = person.getFirstName();

        // Then
        assertEquals("First name should match exactly", specialFirstName, firstName);
    }

    @Test
    public void testGetLastName_ShouldReturnExactString_WhenSpecialCharactersProvided() {
        // Given
        String specialLastName = "O'Connor-Smith";
        Person person = new Person(TEST_FIRST_NAME, specialLastName, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, TEST_BIRTHDATE);

        // When
        String lastName = person.getLastName();

        // Then
        assertEquals("Last name should match exactly", specialLastName, lastName);
    }

    @Test
    public void testGetEmail_ShouldReturnExactString_WhenSpecialEmailProvided() {
        // Given
        String specialEmail = "john.doe+test@example.co.uk";
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, specialEmail,
                TEST_PASSWORD, TEST_GENDER, TEST_BIRTHDATE);

        // When
        String email = person.getEmail();

        // Then
        assertEquals("Email should match exactly", specialEmail, email);
    }

    @Test
    public void testGetPassword_ShouldReturnExactString_WhenSpecialPasswordProvided() {
        // Given
        String specialPassword = "P@ssw0rd!123";
        Person person = new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                specialPassword, TEST_GENDER, TEST_BIRTHDATE);

        // When
        String password = person.getPassword();

        // Then
        assertEquals("Password should match exactly", specialPassword, password);
    }

    // ==================== Immutability Tests ====================

    @Test
    public void testPersonShouldBeImmutable_WhenCreatedWithValidData() {
        // Given
        Person person = createTestPerson();
        String originalFirstName = person.getFirstName();
        String originalLastName = person.getLastName();
        String originalEmail = person.getEmail();
        String originalPassword = person.getPassword();
        Gender originalGender = person.getGender();
        LocalDate originalBirthdate = person.getBirthdate();

        // When - Try to create another person with different data
        Person anotherPerson = new Person("Jane", "Smith", "jane@example.com",
                "differentPassword", Gender.FEMALE, new LocalDate(1995, 3, 10));

        // Then - Original person should remain unchanged
        assertEquals("Original first name should remain unchanged", originalFirstName, person.getFirstName());
        assertEquals("Original last name should remain unchanged", originalLastName, person.getLastName());
        assertEquals("Original email should remain unchanged", originalEmail, person.getEmail());
        assertEquals("Original password should remain unchanged", originalPassword, person.getPassword());
        assertEquals("Original gender should remain unchanged", originalGender, person.getGender());
        assertEquals("Original birthdate should remain unchanged", originalBirthdate, person.getBirthdate());

        // And the new person should have different data
        assertNotEquals("New person should have different first name", originalFirstName, anotherPerson.getFirstName());
        assertNotEquals("New person should have different last name", originalLastName, anotherPerson.getLastName());
        assertNotEquals("New person should have different email", originalEmail, anotherPerson.getEmail());
        assertNotEquals("New person should have different password", originalPassword, anotherPerson.getPassword());
        assertNotEquals("New person should have different gender", originalGender, anotherPerson.getGender());
        assertNotEquals("New person should have different birthdate", originalBirthdate, anotherPerson.getBirthdate());
    }

    // ==================== Helper Methods ====================

    private Person createTestPerson() {
        return new Person(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_EMAIL,
                TEST_PASSWORD, TEST_GENDER, TEST_BIRTHDATE);
    }
}