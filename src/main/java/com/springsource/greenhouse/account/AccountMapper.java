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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ResultSetの行をAccountオブジェクトインスタンスにマッピングするRowMapper。
 * Account SQLマッピングが必要な場所で再利用するように設計されています。
 * AccountMappingの最も難しい側面をカプセル化しています：
 * pictureとprofile URLフィールドのマッピング。
 * また、プライベートなユーザーデータを公開せずに、
 * 公開ユーザープロフィールへのリンクを生成するためのAccountReferenceオブジェクトのマッピングも可能です。
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
	@Autowired
	@Inject
	public AccountMapper(FileStorage pictureStorage,
			@Value("#{environment['application.url']}/members/{profileKey}") String profileUrlTemplate) {
		this(new PictureUrlMapper(new PictureUrlFactory(pictureStorage), PictureSize.SMALL), profileUrlTemplate);
	}

	// ResultSetの行をAccountオブジェクトにマッピングするメソッド
	// @param rs SQLクエリの結果セット
	// @param row 現在の行番号
	// @return Accountオブジェクト
	public Account mapRow(ResultSet rs, int row) throws SQLException {
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"),
				rs.getString("username"), pictureUrlMapper.mapRow(rs, row), profileUrlTemplate);
	}

	/**
	 * ProfileReferenceオブジェクトをマッピングするRowMapper。
	 * ユーザープロフィールへのリンクを生成する際に使用されるように設計されています。
	 */
	public RowMapper<ProfileReference> getReferenceMapper() {
		return referenceMapper;
	}

	// Personモデルから新しいAccountを作成します。
	// Accountのユーザー名は初期的にnullであり、後で変更される可能性があります。
	// Accountのプロフィール画像は、初期的にPersonの性別に基づくデフォルト画像です（後で変更される可能性もあります）。
	// @param accountId 割り当てられた内部アカウント識別子
	// @param person Personモデル
	// @return 新しいAccountオブジェクト
	public Account newAccount(Long accountId, Person person) {
		String pictureUrl = pictureUrlMapper.defaultPictureUrl(person.getGender());
		return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail(), null, pictureUrl,
				profileUrlTemplate);
	}

	// internal helpers

	// ProfileReferenceオブジェクトをマッピングする内部RowMapper。
	// @param rs SQLクエリの結果セット
	// @param row 現在の行番号
	// @return ProfileReferenceオブジェクト
	private final RowMapper<ProfileReference> referenceMapper = new RowMapper<ProfileReference>() {
		public ProfileReference mapRow(ResultSet rs, int row) throws SQLException {
			String id = getId(rs);
			String label = rs.getString("firstName") + " " + rs.getString("lastName");
			return new ProfileReference(id, label, pictureUrlMapper.mapRow(rs, row));
		}

		// ResultSetからIDを取得するヘルパーメソッド
		// @param rs SQLクエリの結果セット
		// @return ID文字列
		private String getId(ResultSet rs) throws SQLException {
			String username = rs.getString("username");
			return username != null ? username : rs.getString("id");
		}
	};

}