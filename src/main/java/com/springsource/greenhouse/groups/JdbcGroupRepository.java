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
package com.springsource.greenhouse.groups;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * GroupRepositoryの実装クラスで、JDBC APIを使用してリレーショナルデータベースにグループ情報を保存します。
 * 
 * @author Keith Donald
 */
@Repository
public class JdbcGroupRepository implements GroupRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcGroupRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 指定されたスラッグに基づいてグループを検索します。
	 * 
	 * @param profileKey グループを識別するためのスラッグ
	 * @return 検索されたグループ
	 */
	public Group findGroupBySlug(String profileKey) {
		return jdbcTemplate.queryForObject(FIND_GROUP_QUERY, groupMapper, profileKey);
	}

	// 内部ヘルパー

	private RowMapper<Group> groupMapper = new RowMapper<Group>() {
		/**
		 * ResultSetの現在の行をGroupオブジェクトにマッピングします。
		 *
		 * @param rs  ResultSetオブジェクト
		 * @param row 現在の行番号
		 * @return マッピングされたGroupオブジェクト
		 * @throws SQLException SQLエラーが発生した場合
		 */
		public Group mapRow(ResultSet rs, int row) throws SQLException {
			Group group = new Group();
			group.setName(rs.getString("name")); // グループ名を設定
			group.setDescription(rs.getString("description")); // グループの説明を設定
			group.setHashtag(rs.getString("hashtag")); // グループのハッシュタグを設定
			return group;
		}
	};

	private static final String FIND_GROUP_QUERY = "select name, description, hashtag from MemberGroup where slug = ?"; // グループを検索するためのSQLクエリ
}
