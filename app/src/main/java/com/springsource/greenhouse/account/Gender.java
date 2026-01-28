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

/**
 * Male or Female gender.
 * Used to customize account messages (e.g., use of "he" or "she") and select default profile pictures.
 * 
 * @author Keith Donald
 */
public enum Gender {

	MALE('M'), FEMALE('F');

	private char code;

	private Gender(char code) {
		this.code = code;
	}

	/**
	 * Returns the gender code.
	 * 
	 * @return gender code ('M' or 'F')
	 */
	public char code() {
		return code;
	}

	/**
	 * Returns the corresponding Gender enum from a character.
	 * 
	 * @param charAt character representing gender ('M' or 'F')
	 * @return corresponding Gender enum
	 */
	public static Gender valueOf(char charAt) {
		if (charAt == 'M') {
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}
	}
}
