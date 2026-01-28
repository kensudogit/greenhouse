/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSet;
import org.springframework.jdbc.versioned.SqlDatabaseChange;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.database.DatabaseUpgrader;

/**
 * Greenhouse RDBMSアクセス設定。
 * RDBMSは、Greenhouseアプリケーションのトランザクションデータの記録システムを提供します。
 * JdbcTemplateを使用してそのデータにアクセスします。
 * Transactionalメソッドの周りにコンパイル時に織り込まれたAspectJアドバイスを使用してトランザクション管理を適用します。
 * "embedded mode"では、開発者のテスト環境のセットアップを容易にするために埋め込みデータベースを使用します。
 * "standard mode"では、接続プールを介してファイルベースのH2データベースに接続します。
 * 
 * @author Keith Donald
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class DataConfig {

	private final DataSource dataSource;

	@Inject
	public DataConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * リポジトリがJDBC APIを使用してRDBMSデータにアクセスできるようにします。
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * JDBC APIを使用してRDBMSに対するトランザクションを管理できるようにします。
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * 埋め込みデータ構成。
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		private final Environment environment;
		private final TextEncryptor textEncryptor;

		@Inject
		public Embedded(Environment environment, TextEncryptor textEncryptor) {
			this.environment = environment;
			this.textEncryptor = textEncryptor;
		}

		/**
		 * データソースを作成し、埋め込みデータベースを設定します。
		 * データベース名を"greenhouse"に設定し、データベースタイプをH2に設定します。
		 * 
		 * @return 設定されたデータベースを返します。
		 */
		@Bean(destroyMethod = "shutdown")
		public DataSource dataSource() {
			EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
			factory.setDatabaseName("greenhouse");
			factory.setDatabaseType(EmbeddedDatabaseType.H2);
			return populateDatabase(factory.getDatabase());
		}

		/**
		 * データベースを初期化し、必要な変更セットを適用します。
		 * 
		 * @param database 初期化する埋め込みデータベース
		 * @return 初期化されたデータベースを返します。
		 */
		private EmbeddedDatabase populateDatabase(EmbeddedDatabase database) {
			new DatabaseUpgrader(database, environment, textEncryptor) {
				protected void addInstallChanges(DatabaseChangeSet changeSet) {
					changeSet.add(SqlDatabaseChange.inResource(new ClassPathResource("test-data.sql", getClass())));
				}
			}.run();
			return database;
		}

	}

	/**
	 * 標準データ構成。
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		private final Environment environment;
		private final TextEncryptor textEncryptor;

		@Inject
		public Standard(Environment environment, TextEncryptor textEncryptor) {
			this.environment = environment;
			this.textEncryptor = textEncryptor;
		}

		/**
		 * データソースを作成し、標準モードのデータベース接続を設定します。
		 * 環境プロパティからデータベースのURL、ユーザー名、パスワードを取得します。
		 * 
		 * @return 設定されたデータベース接続を返します。
		 */
		@Bean(destroyMethod = "dispose")
		public DataSource dataSource() {
			JdbcConnectionPool dataSource = JdbcConnectionPool.create(environment.getProperty("database.url"),
					environment.getProperty("database.username"), environment.getProperty("database.password"));
			new DatabaseUpgrader(dataSource, environment, textEncryptor).run();
			return dataSource;
		}

	}
}
