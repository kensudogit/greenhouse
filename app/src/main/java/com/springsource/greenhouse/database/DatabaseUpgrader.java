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
package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.versioned.DatabaseChange;
import org.springframework.jdbc.versioned.DatabaseChangeSet;
import org.springframework.jdbc.versioned.DatabaseVersion;
import org.springframework.jdbc.versioned.GenericDatabaseUpgrader;
import org.springframework.jdbc.versioned.SqlDatabaseChange;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import com.springsource.greenhouse.database.upgrade.v3.UpdateEncryptionMethod;

/**
 * Performs migrations against the Greenhouse database.
 * As part of integrating software changes that affect the schema,
 * database migrations can be automated.
 * If the database is at version zero(),
 * the current version of the database is installed.
 * If the database is at a version lower than the current version,
 * it is upgraded from that version to the current version.
 * If the database is already at the current version,
 * no migration is performed.
 * This migration model was adapted from the
 * <a href="http://www.liquibase.org/tutorial-using-oracle">LiquiBase Oracle
 * tutorial</a>.
 *
 * @author Keith Donald
 */
public class DatabaseUpgrader {

	private final Environment environment;

	private final TextEncryptor textEncryptor;

	private final org.springframework.jdbc.versioned.DatabaseUpgrader upgrader;

	/**
	 * Constructs a DatabaseUpgrader.
	 * 
	 * @param dataSource    data source
	 * @param environment   environment
	 * @param textEncryptor text encryptor
	 */
	public DatabaseUpgrader(DataSource dataSource, Environment environment, TextEncryptor textEncryptor) {
		this.environment = environment;
		this.textEncryptor = textEncryptor;
		this.upgrader = createUpgrader(dataSource);
	}

	/**
	 * Runs the database migration.
	 */
	public void run() {
		upgrader.run();
	}

	// subclassing hooks

	protected void addInstallChanges(DatabaseChangeSet changeSet) {
		// This method is intentionally left empty for subclasses to override if needed.
	}

	// internal helpers

	private org.springframework.jdbc.versioned.DatabaseUpgrader createUpgrader(DataSource dataSource) {
		GenericDatabaseUpgrader localUpgrader = new GenericDatabaseUpgrader(dataSource);
		if (localUpgrader.getCurrentDatabaseVersion().equals(DatabaseVersion.zero())) {
			addInstallChangeSet(localUpgrader);
		} else {
			addUpgradeChangeSets(localUpgrader);
		}
		return localUpgrader;
	}

	/**
	 * Called for completely fresh DB only.
	 * 
	 * @param upgrader database upgrader
	 */
	private void addInstallChangeSet(GenericDatabaseUpgrader upgrader) {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("4"));
		changeSet.add(installScript("Member.sql"));
		changeSet.add(installScript("Group.sql"));
		changeSet.add(installScript("Activity.sql"));
		changeSet.add(installScript("ConnectedApp.sql"));
		changeSet.add(installScript("Reset.sql"));
		changeSet.add(installScript("Invite.sql"));
		changeSet.add(installScript("Venue.sql"));
		changeSet.add(installScript("Event.sql"));
		changeSet.add(SqlDatabaseChange.inResource(
				new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class)));
		addInstallChanges(changeSet);
		upgrader.addChangeSet(changeSet);
	}

	/**
	 * Creates a database change from an install script resource.
	 * 
	 * @param resource script resource name
	 * @return database change
	 */
	private DatabaseChange installScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("install/" + resource, DatabaseUpgrader.class));
	}

	/**
	 * Called to upgrade existing DB.
	 * 
	 * @param upgrader database upgrader
	 */
	private void addUpgradeChangeSets(GenericDatabaseUpgrader upgrader) {
		upgrader.addChangeSet(version2ChangeSet());
		upgrader.addChangeSet(version3ChangeSet());
		upgrader.addChangeSet(version4ChangeSet());
	}

	/**
	 * Creates change set for version 2 upgrade.
	 * 
	 * @return change set
	 */
	private DatabaseChangeSet version2ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("2"));
		changeSet.add(upgradeScript("v2/AlterServiceProviderTable.sql"));
		return changeSet;
	}

	/**
	 * Creates change set for version 3 upgrade.
	 * 
	 * @return change set
	 */
	private DatabaseChangeSet version3ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("3"));
		changeSet.add(new UpdateEncryptionMethod(environment, textEncryptor));
		changeSet.add(upgradeScript("v3/CreateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/PopulateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/DropAccountConnectionTables.sql"));
		return changeSet;
	}

	/**
	 * Creates change set for version 4 upgrade.
	 * 
	 * @return change set
	 */
	private DatabaseChangeSet version4ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("4"));
		changeSet.add(upgradeScript("v4/CreateTimeSlotTable.sql"));
		return changeSet;
	}

	/**
	 * Creates a database change from an upgrade script resource.
	 * 
	 * @param resource script resource path
	 * @return database change
	 */
	private DatabaseChange upgradeScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("upgrade/" + resource, DatabaseUpgrader.class));
	}

}