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

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.springframework.social.connect.UserProfile;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;
import com.springsource.greenhouse.validation.Confirm;

/**
 * 新しいメンバーのサインアップフォームのモデル。
 * 
 * @author Keith Donald
 */
@Confirm(field = "email", message = "does not match confirmation email")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupForm {

	@NotEmpty
	@JsonProperty("first-name")
	private String firstName;

	@NotEmpty
	@JsonProperty("last-name")
	private String lastName;

	@NotEmpty
	@Email
	@JsonProperty("email")
	private String email;

	@JsonProperty("confirm-email")
	private String confirmEmail;

	@JsonProperty("gender")
	@JsonDeserialize(using = SignupFormDeserializers.GenderDeserializer.class)
	private Gender gender;

	@NotNull
	private Integer month;

	@NotNull
	private Integer day;

	@NotNull
	private Integer year;

	@Size(min = 6, message = "must be at least 6 characters")
	@JsonProperty("password")
	private String password;

	/**
	 * 人の名前を取得します。
	 */
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 人の名前を設定します。
	 */
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 人のメールアドレスを取得します。ユニークである必要があります。
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 人のメールアドレスを設定します。
	 */
	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	/**
	 * メンバーのパスワードを取得します。
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * メンバーの性別を取得します。
	 */
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * メンバーが生まれた月を取得します。
	 */
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * メンバーが生まれた日を取得します。
	 */
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * メンバーが生まれた年を取得します。
	 */
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@JsonProperty("birthdate")
	@JsonDeserialize(using = SignupFormDeserializers.BirthdateDeserializer.class)
	protected void setBirthdate(List<Integer> birthdateFields) {
		this.month = birthdateFields.get(0);
		this.day = birthdateFields.get(1);
		this.year = birthdateFields.get(2);
	}

	/**
	 * このSignupFormからPersonレコードを作成します。
	 * Personは新しいメンバーアカウントを作成するためにAccountRepositoryに入力されます。
	 */
	public Person createPerson() {
		return new Person(firstName, lastName, email, password, gender, new LocalDate(year, month, day));
	}

	/**
	 * プロバイダユーザーのサインイン試行から取得したServiceProviderUserモデルから事前に入力されたSignupFormを作成するファクトリーメソッド。
	 * 
	 * @param providerUser プロバイダユーザーモデル
	 */
	public static SignupForm fromProviderUser(UserProfile providerUser) {
		SignupForm form = new SignupForm();
		form.setFirstName(providerUser.getFirstName());
		form.setLastName(providerUser.getLastName());
		form.setEmail(providerUser.getEmail());
		form.setConfirmEmail(providerUser.getEmail());
		return form;
	}
}