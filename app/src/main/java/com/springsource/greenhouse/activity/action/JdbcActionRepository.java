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

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * JdbcActionRepository records action information in a relational database using JDBC API.
 * 
 * @author Keith Donald
 */
@Repository
public class JdbcActionRepository implements ActionRepository {

	private final ActionGateway actionGateway;

	private final ActionService actionService;

	@Inject
	public JdbcActionRepository(ActionGateway actionGateway, ActionService actionService) {
		this.actionGateway = actionGateway;
		this.actionService = actionService;
	}

	/**
	 * Saves a simple action and notifies that the action was performed.
	 * 
	 * @param type    simple action type
	 * @param account account that performed the action
	 * @return saved SimpleAction
	 */
	@Override
	public SimpleAction saveSimpleAction(final String type, Account account) {
		SimpleAction action = actionService.doSaveAction(SimpleAction.class, account,
				new ActionFactory<SimpleAction>() {
					@Override
					public SimpleAction createAction(Long id, DateTime time, Account account, Location location) {
						return new SimpleAction(type, id, time, account, location);
					}
				}, type, Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	/**
	 * Saves an instance of the specified action class and notifies that the action was performed.
	 * 
	 * @param actionClass   action class
	 * @param account      account that performed the action
	 * @param actionFactory factory for creating action instances
	 * @return saved Action
	 */
	@Override
	public <A extends Action> A saveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory) {
		A action = actionService.doSaveAction(actionClass, account, actionFactory, actionType(actionClass),
				Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	// internal helpers

	/**
	 * Gets the short name of the action class and returns the action type.
	 * 
	 * @param actionClass action class
	 * @return action type
	 */
	private String actionType(Class<? extends Action> actionClass) {
		String shortName = ClassUtils.getShortName(actionClass);
		int actionPart = shortName.lastIndexOf("Action");
		return actionPart == -1 ? shortName : shortName.substring(0, actionPart);
	}

	/**
	 * ActionService interface defines methods for saving actions.
	 */
	interface ActionService {
		<A extends Action> A doSaveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory,
				String actionType, Location location, DateTime time);
	}

}

/**
 * ActionService implementation that saves actions to the database.
 */
@Repository
class ActionServiceImpl implements ActionService {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public ActionServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Saves an action to the database and returns the action instance.
	 * 
	 * @param actionClass   action class
	 * @param account       account that performed the action
	 * @param actionFactory factory for creating action instances
	 * @param actionType    action type
	 * @param location      location where action was performed
	 * @param time          time when action was performed
	 * @return saved Action
	 */
	@Transactional
	@Override
	public <A extends Action> A doSaveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory,
			String actionType, Location location, DateTime time) {
		Long id = insertAction(actionType, time, account, location);
		return actionFactory.createAction(id, time, account, location);
	}

	/**
	 * Inserts an action into the database and returns the generated ID.
	 * 
	 * @param type        action type
	 * @param performTime time when action was performed
	 * @param account     account that performed the action
	 * @param location    location where action was performed
	 * @return generated action ID
	 */
	private Long insertAction(String type, DateTime performTime, Account account, Location location) {
		Double latitude = location != null ? location.getLatitude() : null;
		Double longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update(
				"insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?, ?)",
				type, performTime.toDate(), latitude, longitude, account.getId());
		return jdbcTemplate.queryForObject("call identity()", Long.class);
	}
}