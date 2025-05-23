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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.ProfileReference;
import com.springsource.greenhouse.activity.action.ActionFactory;
import com.springsource.greenhouse.activity.action.ActionRepository;
import com.springsource.greenhouse.utils.Location;

/**
 * InviteRepositoryの実装で、JDBC APIを使用してリレーショナルデータベースに招待を保存します。
 * 
 * @author Keith Donald
 */
@Repository
public class JdbcInviteRepository implements InviteRepository {

	private final JdbcTemplate jdbcTemplate;

	private final ActionRepository actionRepository;

	@Inject
	public JdbcInviteRepository(JdbcTemplate jdbcTemplate, ActionRepository actionRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.actionRepository = actionRepository;
	}

	/**
	 * すでに招待されているかどうかを確認します。
	 * 
	 * @param email 招待を確認するメールアドレス
	 * @return 招待されている場合はtrue、そうでない場合はfalse
	 */
	public boolean alreadyInvited(String email) {
		return jdbcTemplate.queryForObject("select exists(select 1 from Invite where email = ?)", Boolean.class, email)
				||
				jdbcTemplate.queryForObject("select exists(select 1 from Member where email = ?)", Boolean.class,
						email);
	}

	/**
	 * 招待をデータベースに保存します。
	 * 
	 * @param token   招待トークン
	 * @param invitee 招待される人の情報
	 * @param text    招待メッセージ
	 * @param sentBy  招待を送信したユーザーのID
	 */
	public void saveInvite(String token, Invitee invitee, String text, Long sentBy) {
		jdbcTemplate.update(INSERT_INVITE, token, invitee.getEmail(), invitee.getFirstName(), invitee.getLastName(),
				text, sentBy);
	}

	/**
	 * 招待が受け入れられたことをマークします。
	 * 
	 * @param token   招待トークン
	 * @param account 招待を受け入れたアカウント
	 */
	public void markInviteAccepted(final String token, Account account) {
		actionRepository.saveAction(InviteAcceptAction.class, account, new ActionFactory<InviteAcceptAction>() {
			public InviteAcceptAction createAction(Long id, DateTime time, Account account, Location location) {
				jdbcTemplate.update("insert into InviteAcceptAction (invite, memberAction) values (?, ?)", token, id);
				Map<String, Object> invite = jdbcTemplate
						.queryForMap("select sentBy, sentTime from Invite where token = ?", token);
				Long sentBy = (Long) invite.get("sentBy");
				DateTime sentTime = new DateTime(invite.get("sentTime"), DateTimeZone.UTC);
				return new InviteAcceptAction(id, time, account, location, sentBy, sentTime);
			}
		});
	}

	/**
	 * 招待をトークンで検索します。
	 * 
	 * @param token 招待トークン
	 * @return 招待オブジェクト
	 * @throws NoSuchInviteException          招待が見つからない場合
	 * @throws InviteAlreadyAcceptedException 招待がすでに受け入れられている場合
	 */
	public Invite findInvite(String token) throws NoSuchInviteException, InviteAlreadyAcceptedException {
		Invite invite = queryForInvite(token);
		if (invite.isAccepted()) {
			throw new InviteAlreadyAcceptedException(token);
		}
		return invite;
	}

	// internal helpers

	/**
	 * 招待をトークンで検索し、見つからない場合は例外をスローします。
	 * 
	 * @param token 招待トークン
	 * @return 招待オブジェクト
	 * @throws NoSuchInviteException 招待が見つからない場合
	 */
	private Invite queryForInvite(String token) throws NoSuchInviteException {
		try {
			return jdbcTemplate.queryForObject(SELECT_INVITE, new RowMapper<Invite>() {
				public Invite mapRow(ResultSet rs, int rowNum) throws SQLException {
					Invitee invitee = new Invitee(rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("email"));
					ProfileReference sentBy = ProfileReference.textOnly(rs.getLong("sentById"),
							rs.getString("sentByUsername"), rs.getString("sentByFirstName"),
							rs.getString("sentByLastName"));
					return new Invite(invitee, sentBy, rs.getBoolean("accepted"));
				}
			}, token, token);
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchInviteException(token);
		}
	}

	private static final String INSERT_INVITE = "insert into Invite (token, email, firstName, lastName, text, sentBy) values (?, ?, ?, ?, ?, ?)";

	private static final String SELECT_INVITE = "select i.email, i.firstName, i.lastName, m.id as sentById, m.username as sentByUsername, m.firstName as sentByFirstName, m.lastName as sentByLastName, exists(select 1 from InviteAcceptAction where invite = ?) as accepted from Invite i inner join Member m on i.sentBy = m.id where i.token = ?";

}