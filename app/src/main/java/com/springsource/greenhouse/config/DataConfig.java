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
 * Greenhouse RDBMS access configuration.
 * RDBMS provides the transactional data recording system for the Greenhouse application.
 * Access to that data is via JdbcTemplate.
 * Transaction management is applied using compile-time woven AspectJ advice around @Transactional methods.
 * In "embedded mode", an embedded database is used to ease developer test environment setup.
 * In "standard mode", connects to a file-based H2 database via a connection pool.
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
	 * Enables repositories to access RDBMS data using JDBC API.
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * Enables transaction management for RDBMS using JDBC API.
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * Embedded data configuration.
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
		 * Creates a data source and configures an embedded database.
		 * Sets database name to "greenhouse" and database type to H2.
		 * 
		 * @return configured database
		 */
		@Bean(destroyMethod = "shutdown")
		public DataSource dataSource() {
			EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
			factory.setDatabaseName("greenhouse");
			factory.setDatabaseType(EmbeddedDatabaseType.H2);
			return populateDatabase(factory.getDatabase());
		}

		/**
		 * Initializes the database and applies necessary change sets.
		 * 
		 * @param database embedded database to initialize
		 * @return initialized database
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
	 * Standard data configuration.
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
		 * Creates a data source and configures database connection for standard mode.
		 * Retrieves database URL, username, and password from environment properties.
		 * 
		 * @return configured database connection
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
