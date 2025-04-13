/*
 * 著作権 2010-2011 オリジナルの著者または著作権者。
 *
 * Apache License, Version 2.0 ("ライセンス") に基づいてライセンスされています。
 * このファイルを使用するには、ライセンスに従う必要があります。
 * ライセンスのコピーは以下で入手できます。
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * 適用される法律または書面で合意されている場合を除き、
 * このソフトウェアは「現状のまま」提供され、
 * 明示または黙示を問わず、いかなる保証もありません。
 * ライセンスに基づく権限と制限を参照してください。
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
 * Greenhouse データベースに対してマイグレーションを実行します。
 * スキーマに影響を与えるソフトウェア変更を統合する一環として、
 * データベースのマイグレーションを自動化することができます。
 * データベースがバージョン zero() の場合、
 * データベースの現在のバージョンがインストールされます。
 * データベースが現在のバージョンよりも低いバージョンの場合、
 * そのバージョンから現在のバージョンにアップグレードされます。
 * データベースがすでに現在のバージョンの場合、
 * マイグレーションは実行されません。
 * このマイグレーションモデルは、
 * <a href="http://www.liquibase.org/tutorial-using-oracle">LiquiBase Oracle
 * チュートリアル</a>から
 * 適応されました。
 * 
 * @author Keith Donald
 */
public class DatabaseUpgrader {

	private final Environment environment;

	private final TextEncryptor textEncryptor;

	private final org.springframework.jdbc.versioned.DatabaseUpgrader upgrader;

	public DatabaseUpgrader(DataSource dataSource, Environment environment, TextEncryptor textEncryptor) {
		this.environment = environment;
		this.textEncryptor = textEncryptor;
		this.upgrader = createUpgrader(dataSource);
	}

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

	// Called for completely fresh DB only
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

	private DatabaseChange installScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("install/" + resource, DatabaseUpgrader.class));
	}

	// Called to upgrade existing DB
	private void addUpgradeChangeSets(GenericDatabaseUpgrader upgrader) {
		upgrader.addChangeSet(version2ChangeSet());
		upgrader.addChangeSet(version3ChangeSet());
		upgrader.addChangeSet(version4ChangeSet());
	}

	private DatabaseChangeSet version2ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("2"));
		changeSet.add(upgradeScript("v2/AlterServiceProviderTable.sql"));
		return changeSet;
	}

	private DatabaseChangeSet version3ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("3"));
		changeSet.add(new UpdateEncryptionMethod(environment, textEncryptor));
		changeSet.add(upgradeScript("v3/CreateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/PopulateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/DropAccountConnectionTables.sql"));
		return changeSet;
	}

	private DatabaseChangeSet version4ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("4"));
		changeSet.add(upgradeScript("v4/CreateTimeSlotTable.sql"));
		return changeSet;
	}

	private DatabaseChange upgradeScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("upgrade/" + resource, DatabaseUpgrader.class));
	}

}