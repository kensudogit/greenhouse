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
package com.springsource.greenhouse.activity.action;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * Models an action performed by a member in the application system.
 * Used to record member activity.
 * Actions also serve as input to the badge system (performing actions may result in badge awards).
 * Actions may be explicitly performed or implicitly triggered on behalf of members.
 * 
 * @author Keith Donald
 */
public abstract class Action {

	private final Long id;

	private final DateTime time;

	private final Account account;

	private final Location location;

	protected Action(Long id, DateTime time, Account account, Location location) {
		this.id = id;
		this.time = time;
		this.account = account;
		this.location = location;
	}

	/**
	 * Internal identifier for this action.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Date and time when the action was performed.
	 */
	public DateTime getTime() {
		return time;
	}

	/**
	 * Member account that performed the action.
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Location where the action was performed.
	 * May be null if geotagging is disabled.
	 */
	public Location getLocation() {
		return location;
	}

}