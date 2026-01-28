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
package com.springsource.greenhouse.develop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.SlugUtils;

/**
 * AppRepositoryの実装で、JDBC APIを使用してリレーショナルデータベースにAppデータを保存します。
 * ApiKeysやシークレット、accessTokensやシークレットは、{@link StringEncryptor}を使用して保存のために暗号化されます。
 * 
 * @author Keith Donald
 */
@Repository
public class JdbcAppRepository implements AppRepository {

	private JdbcTemplate jdbcTemplate;

	private TextEncryptor encryptor;

	private StringKeyGenerator keyGenerator = KeyGenerators.string();

	@Inject
	public JdbcAppRepository(JdbcTemplate jdbcTemplate, TextEncryptor encryptor) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
	}

	// 指定されたアカウントIDに関連するアプリの概要を取得します。
	public List<AppSummary> findAppSummaries(Long accountId) {
		return jdbcTemplate.query(SELECT_APPS, appSummaryMapper, accountId);
	}

	// 指定されたスラッグに基づいてアプリを検索します。
	public App findAppBySlug(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_APP_BY_SLUG, appMapper, accountId, slug);
	}

	// 指定されたAPIキーに基づいてアプリを検索します。
	public App findAppByApiKey(String apiKey) throws InvalidApiKeyException {
		try {
			return jdbcTemplate.queryForObject(SELECT_APP_BY_API_KEY, appMapper, encryptor.encrypt(apiKey));
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidApiKeyException(apiKey);
		}
	}

	// アプリ情報を更新し、新しいスラッグを返します。
	public String updateApp(Long accountId, String slug, AppForm form) {
		String newSlug = createSlug(form.getName());
		jdbcTemplate.update(UPDATE_APP_FORM, form.getName(), newSlug, form.getDescription(), form.getOrganization(),
				form.getWebsite(), form.getCallbackUrl(), accountId, slug);
		return newSlug;
	}

	// 指定されたアカウントIDとスラッグに基づいてアプリを削除します。
	public void deleteApp(Long accountId, String slug) {
		jdbcTemplate.update(DELETE_APP, accountId, slug);
	}

	// 新しいアプリフォームを取得します。
	public AppForm getNewAppForm() {
		return new AppForm();
	}

	// 指定されたアカウントIDとスラッグに基づいてアプリフォームを取得します。
	public AppForm getAppForm(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_APP_FORM, appFormMapper, accountId, slug);
	}

	@Transactional
	// 新しいアプリを作成し、スラッグを返します。
	public String createApp(Long accountId, AppForm form) {
		String slug = createSlug(form.getName());
		String encryptedApiKey = encryptor.encrypt(keyGenerator.generateKey());
		String encryptedSecret = encryptor.encrypt(keyGenerator.generateKey());
		jdbcTemplate.update(INSERT_APP, form.getName(), slug, form.getDescription(), form.getOrganization(),
				form.getWebsite(), encryptedApiKey, encryptedSecret, form.getCallbackUrl());
		Long appId = jdbcTemplate.queryForObject("call identity()", Long.class);
		jdbcTemplate.update(INSERT_APP_DEVELOPER, appId, accountId);
		return slug;
	}

	@Override
	public List<Map<String, Object>> findConnectedApps(Long accountId) {
		return jdbcTemplate.queryForList(
				"select a.name as name, c.token_id as accessToken from App a, oauth_access_token c where c.client_id = a.apiKey and c.user_name = ?",
				accountId);
	}

	// 内部ヘルパーメソッド

	// アプリ名からスラッグを作成します。
	private String createSlug(String appName) {
		return SlugUtils.toSlug(appName);
	}

	private static final String SELECT_APPS = "select a.name, a.slug, a.description from App a inner join AppDeveloper d on a.id = d.app where d.member = ?";

	private static final String SELECT_APP_BY_SLUG = "select a.name, a.slug, a.description, a.apiKey, a.secret, a.callbackUrl from App a inner join AppDeveloper d on a.id = d.app where d.member = ? and a.slug = ?";

	private static final String SELECT_APP_BY_API_KEY = "select a.name, a.slug, a.description, a.apiKey, a.secret, a.callbackUrl from App a where a.apiKey = ?";

	private static final String SELECT_APP_FORM = "select a.name, a.description, a.organization, a.website, a.callbackUrl from App a inner join AppDeveloper d on a.id = d.app where d.member = ? and a.slug = ?";

	private static final String UPDATE_APP_FORM = "update App set name = ?, slug = ?, description = ?, organization = ?, website = ?, callbackUrl = ? where exists(select 1 from AppDeveloper where member = ?) and slug = ?";

	private static final String DELETE_APP = "delete from App where exists(select 1 from AppDeveloper where member = ?) and slug = ?";

	private static final String INSERT_APP = "insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String INSERT_APP_DEVELOPER = "insert into AppDeveloper (app, member) values (?, ?)";

	private static final String DEFAULT_ICON_URL = "http://images.greenhouse.springsource.org/apps/icon-default-app.png";

	// アプリの概要をマッピングするRowMapper
	private RowMapper<AppSummary> appSummaryMapper = new RowMapper<AppSummary>() {
		public AppSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new AppSummary(rs.getString("name"), DEFAULT_ICON_URL, rs.getString("description"),
					rs.getString("slug"));
		}
	};

	// アプリをマッピングするRowMapper
	private RowMapper<App> appMapper = new RowMapper<App>() {
		public App mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new App(appSummaryMapper.mapRow(rs, rowNum), encryptor.decrypt(rs.getString("apiKey")),
					encryptor.decrypt(rs.getString("secret")), rs.getString("callbackUrl"));
		}
	};

	// アプリフォームをマッピングするRowMapper
	private RowMapper<AppForm> appFormMapper = new RowMapper<AppForm>() {
		public AppForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			AppForm form = new AppForm();
			form.setName(rs.getString("name"));
			form.setDescription(rs.getString("description"));
			form.setOrganization(rs.getString("organization"));
			form.setWebsite(rs.getString("website"));
			form.setCallbackUrl(rs.getString("callbackUrl"));
			return form;
		}
	};

}
