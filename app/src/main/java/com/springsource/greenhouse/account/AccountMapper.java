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

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

/**
 * RowMapper that maps ResultSet rows to Account object instances.
 * Designed to be reused wherever Account SQL mapping is needed.
 * Encapsulates the most difficult aspects of Account mapping:
 * picture and profile URL field mapping.
 * Also supports mapping AccountReference objects for generating links to public user profiles
 * without exposing private user data.
 * 
 * @author Keith Donald
 */
@Component
public class AccountMapper implements RowMapper<Account> {

	/**
	 * SELECT clause for Account fields.
	 */
	public static final String SELECT_ACCOUNT = "select id, firstName, lastName, email, username, gender, pictureSet from Member";

	/**
	 * SELECT clause for AccountReference fields.
	 */
	public static final String SELECT_ACCOUNT_REFERENCE = "select id, username, firstName, lastName, gender, pictureSet from Member";

	private final PictureUrlMapper pictureUrlMapper;

	private final UriTemplate profileUrlTemplate;

	/**
	 * Constructs an AccountMapper.
	 * 
	 * @param pictureStorage     The FileStorage for profile pictures
	 * @param profileUrlTemplate The profile URL template for generating public user
	 *                           profile links
	 */
	@Inject
	public AccountMapper(FileStorage pictureStorage,
			@Value("#{environment['application.url']}/members/{profileKey}") String profileUrlTemplate) {
		this(new PictureUrlMapper(new PictureUrlFactory(pictureStorage), PictureSize.SMALL), profileUrlTemplate);
	}

	/**
	 * Maps a ResultSet row to an Account object.
	 * 
	 * @param rs  SQL query result set
	 * @param row current row number
	 * @return Account object
	 */
	@Override
	public Account mapRow(ResultSet rs, int row) throws SQLException {
		String genderStr = rs.getString("gender");
		Gender gender = genderStr != null && !genderStr.isEmpty() ? Gender.valueOf(genderStr.charAt(0)) : null;
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
				rs.getString("username"), pictureUrlMapper.mapRow(rs, row), profileUrlTemplate, gender);
	}

	/**
	 * ProfileReferenceオブジェクトをマッピングするRowMapper。
	 * ユーザープロフィールへのリンクを生成する際に使用されるように設計されています。
	 */
	public RowMapper<ProfileReference> getReferenceMapper() {
		return referenceMapper;
	}

	/**
	 * Creates a new Account from a Person model.
	 * The Account's username is initially null and may be changed later.
	 * The Account's profile picture is initially a default image based on the Person's gender (may also be changed later).
	 * 
	 * @param accountId assigned internal account identifier
	 * @param person    Person model
	 * @return new Account object
	 */
	public Account newAccount(Long accountId, Person person) {
		String pictureUrl = pictureUrlMapper.defaultPictureUrl(person.getGender());
		return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail(), null, pictureUrl,
				profileUrlTemplate, person.getGender());
	}

	// internal helpers

	/**
	 * Internal RowMapper for mapping ProfileReference objects.
	 */
	private final RowMapper<ProfileReference> referenceMapper = new RowMapper<ProfileReference>() {
		@Override
		public ProfileReference mapRow(ResultSet rs, int row) throws SQLException {
			String id = getId(rs);
			String label = rs.getString("firstName") + " " + rs.getString("lastName");
			return new ProfileReference(id, label, pictureUrlMapper.mapRow(rs, row));
		}

		/**
		 * Helper method to get ID from ResultSet.
		 * 
		 * @param rs SQL query result set
		 * @return ID string
		 */
		private String getId(ResultSet rs) throws SQLException {
			String username = rs.getString("username");
			return username != null ? username : rs.getString("id");
		}
	};

}