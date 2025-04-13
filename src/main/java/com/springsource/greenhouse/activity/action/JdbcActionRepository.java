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
 * JdbcActionRepositoryクラスは、JDBC APIを使用してリレーショナルデータベースにアクション情報を記録するためのリポジトリです。
 * 
 * @author Keith Donald
 */
@Repository
public class JdbcActionRepository implements ActionRepository, ActionService {

	// アクションを実行するためのゲートウェイ
	private final ActionGateway actionGateway;

	private final ActionService actionService;

	@Inject
	public JdbcActionRepository(ActionGateway actionGateway, ActionService actionService) {
		this.actionGateway = actionGateway;
		this.actionService = actionService;
	}

	// saveSimpleActionメソッドは、シンプルなアクションを保存し、アクションが実行されたことを通知します。
	public SimpleAction saveSimpleAction(final String type, Account account) {
		SimpleAction action = actionService.doSaveAction(SimpleAction.class, account,
				new ActionFactory<SimpleAction>() {
					public SimpleAction createAction(Long id, DateTime time, Account account, Location location) {
						return new SimpleAction(type, id, time, account, location);
					}
				}, type, Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	// saveActionメソッドは、指定されたアクションクラスのインスタンスを保存し、アクションが実行されたことを通知します。
	public <A extends Action> A saveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory) {
		A action = actionService.doSaveAction(actionClass, account, actionFactory, actionType(actionClass),
				Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	// internal helpers

	// actionTypeメソッドは、アクションクラスの短縮名を取得し、アクションタイプを返します。
	private String actionType(Class<? extends Action> actionClass) {
		String shortName = ClassUtils.getShortName(actionClass);
		int actionPart = shortName.lastIndexOf("Action");
		return actionPart == -1 ? shortName : shortName.substring(0, actionPart);
	}

	// ActionServiceインターフェースは、アクションを保存するためのメソッドを定義します。
	public interface ActionService {
		<A extends Action> A doSaveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory,
				String actionType, Location location, DateTime time);
	}

}

@Repository
public class ActionServiceImpl implements ActionService {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public ActionServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	@Override
	// doSaveActionメソッドは、アクションをデータベースに保存し、アクションインスタンスを返します。
	public <A extends Action> A doSaveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory,
			String actionType, Location location, DateTime time) {
		Long id = insertAction(actionType, time, account, location);
		return actionFactory.createAction(id, time, account, location);
	}

	// insertActionメソッドは、アクションをデータベースに挿入し、生成されたIDを返します。
	private Long insertAction(String type, DateTime performTime, Account account, Location location) {
		Double latitude = location != null ? location.getLatitude() : null;
		Double longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update(
				"insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?, ?)",
				type, performTime.toDate(), latitude, longitude, account.getId());
		return jdbcTemplate.queryForLong("call identity()");
	}
}

// クラス定義の終わり