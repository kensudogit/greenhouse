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
 * Callback interface for constructing Action instances.
 * 
 * @author Keith Donald
 * @param <A> Action type
 */
public interface ActionFactory<A extends Action> {

	/**
	 * Creates an Action instance of type A.
	 * This callback allows the caller to specify the Action implementation and set its specific fields.
	 * It also allows persisting additional properties of this Action specialization along with the system record.
	 * 
	 * @param id       assigned internal action ID
	 * @param time     time when action was performed, current time
	 * @param account  member that performed the action
	 * @param location resolved member location where action was performed
	 * @return Action instance
	 */
	A createAction(Long id, DateTime time, Account account, Location location);
}