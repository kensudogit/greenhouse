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
package com.springsource.greenhouse.invite;

/**
 * Base class for invite exceptions.
 * 
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public abstract class InviteException extends Exception {

	private final String token;

	protected InviteException(String token, String message) {
		super(message);
		this.token = token;
	}

	/**
	 * The token identifying the invite.
	 */
	public String getToken() {
		return token;
	}

}
