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
package com.springsource.greenhouse.events;

/**
 * A person who leads or delivers a session at an event.
 * Can also be thought of as the speaker.
 * 
 * @author Keith Donald
 */
public class EventSessionLeader {

	private String name;

	public EventSessionLeader(String name) {
		this.name = name;
	}

	/**
	 * The full name of the leader.
	 */
	public String getName() {
		return name;
	}

	// These methods are retained for compatibility with the iPhone client,
	// which expects separate first and last name fields.

	/**
	 * Returns the first name of the leader.
	 * Assumes the first part of the name is the first name.
	 */
	public String getFirstName() {
		String[] parts = name.split(" ");
		return parts.length > 0 ? parts[0] : "";
	}

	/**
	 * Returns the last name of the leader.
	 * Assumes the last part of the name is the last name.
	 */
	public String getLastName() {
		String[] parts = name.split(" ");
		return parts.length > 1 ? parts[parts.length - 1] : "";
	}

}
