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
package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;

/**
 * ProfileRepositoryの実装クラスで、JDBC APIを使用してリレーショナルデータベースから
 * プロファイルデータをロードします。
 * 
 * @author Keith Donald
 */
@Service
public class JdbcProfileRepository implements ProfileRepository {

	private final JdbcTemplate jdbcTemplate;

	private final PictureUrlFactory pictureUrlFactory;

	private final RowMapper<Profile> profileMapper;

	/**
	 * 新しいJdbcProfileRepositoryを作成します。
	 * 
	 * @param jdbcTemplate   リレーショナルデータアクセスのテンプレート
	 * @param pictureStorage プロフィール画像が保存されている場所。メンバーのプロフィール画像が設定されている場合に
	 *                       プロフィール画像のURLを取得するために使用されます。
	 */
	@Inject
	public JdbcProfileRepository(JdbcTemplate jdbcTemplate, FileStorage pictureStorage) {
		this.jdbcTemplate = jdbcTemplate;
		this.pictureUrlFactory = new PictureUrlFactory(pictureStorage);
		profileMapper = new ProfileMapper();
	}

	/**
	 * プロファイルキーでプロファイルを検索します。
	 * 
	 * @param profileKey プロファイルキー
	 * @return プロファイルオブジェクト
	 */
	public Profile findById(String profileKey) {
		Long accountId = getAccountId(profileKey);
		return accountId != null ? findByAccountId(accountId) : findByUsername(profileKey);
	}

	/**
	 * アカウントIDでプロファイルを検索します。
	 * 
	 * @param accountId アカウントID
	 * @return プロファイルオブジェクト
	 */
	public Profile findByAccountId(Long accountId) {
		return jdbcTemplate.queryForObject(SELECT_PROFILE + " where id = ?", profileMapper, accountId);
	}

	/**
	 * アカウントIDで接続されたプロファイルを検索します。
	 * 
	 * @param accountId アカウントID
	 * @return 接続されたプロファイルのリスト
	 */
	public List<ConnectedProfile> findConnectedProfiles(Long accountId) {
		return jdbcTemplate.query(SELECT_CONNECTED_PROFILES, new RowMapper<ConnectedProfile>() {
			public ConnectedProfile mapRow(ResultSet rs, int row) throws SQLException {
				String displayName = displayNameFor(rs.getString("providerId"));
				return new ConnectedProfile(displayName, rs.getString("profileUrl"));
			}
		}, accountId.toString());
	}

	// internal helpers

	/**
	 * ユーザー名でプロファイルを検索します。
	 * 
	 * @param username ユーザー名
	 * @return プロファイルオブジェクト
	 */
	private Profile findByUsername(String username) {
		return jdbcTemplate.queryForObject(SELECT_PROFILE + " where username = ?", profileMapper, username);
	}

	/**
	 * プロバイダーIDに基づいて表示名を取得します。
	 * 
	 * @param providerId プロバイダーID
	 * @return 表示名
	 */
	private String displayNameFor(String providerId) {
		return providerId;
	}

	/**
	 * ProfileのRowMapper実装クラス。
	 */
	private class ProfileMapper implements RowMapper<Profile> {

		private PictureUrlMapper profilePictureUrlMapper = new PictureUrlMapper(pictureUrlFactory, PictureSize.LARGE);

		public Profile mapRow(ResultSet rs, int row) throws SQLException {
			return new Profile(rs.getLong("id"), rs.getString("displayName"), profilePictureUrlMapper.mapRow(rs, row));
		}
	};

	/**
	 * アカウントIDを取得します。
	 * 
	 * @param id ID
	 * @return アカウントID、またはnull
	 */
	private Long getAccountId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static final String SELECT_PROFILE = "select id, (firstName || ' ' || lastName) as displayName, gender, pictureSet from Member";

	private static final String SELECT_CONNECTED_PROFILES = "select providerId, profileUrl from UserConnection where userId = ? order by providerId";

}