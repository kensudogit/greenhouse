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
package org.springframework.jdbc.versioned;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GenericDatabaseUpgraderクラスは、データベースのバージョン管理を行うためのクラスです。
 * 指定されたDataSourceを使用してデータベースに接続し、
 * DatabaseVersionテーブルを用いてバージョン情報を管理します。
 * 
 * @author Keith Donald
 */
public class GenericDatabaseUpgrader implements DatabaseUpgrader {

	private static final Logger logger = LoggerFactory.getLogger(GenericDatabaseUpgrader.class);

	private final DataSource dataSource;

	private final Set<DatabaseChangeSet> changeSets = new TreeSet<DatabaseChangeSet>();

	private DatabaseVersion currentVersion;

	/**
	 * Constructs a database upgrader that upgrades the Database accessed by the
	 * provided {@link DataSource}.
	 * 
	 * @param dataSource the factory for database connections
	 */
	public GenericDatabaseUpgrader(DataSource dataSource) {
		this.dataSource = dataSource;
		currentVersion = findCurrentVersion();
		if (logger.isInfoEnabled()) {
			logger.info("Database is at Version " + currentVersion);
		}
	}

	/**
	 * データベースをアップグレードするための変更セットをリストに追加します。
	 */
	public void addChangeSet(DatabaseChangeSet changeSet) {
		changeSets.add(changeSet);
	}

	// implementing DatabaseUpgrader

	/**
	 * データベースの現在のバージョンを取得します。
	 * 
	 * @return 現在のデータベースバージョン
	 */
	public DatabaseVersion getCurrentDatabaseVersion() {
		return currentVersion;
	}

	/**
	 * データベースをアップグレードするためのメインメソッドです。
	 * 追加された変更セットを順に適用し、データベースのバージョンを更新します。
	 */
	public void run() {
		for (DatabaseChangeSet changeSet : changeSets) {
			if (currentVersion.lessThan(changeSet.getVersion())) {
				if (logger.isInfoEnabled()) {
					logger.info("Upgrading Database to Version " + changeSet.getVersion());
				}
				currentVersion = changeSet.apply(dataSource);
				if (logger.isInfoEnabled()) {
					logger.info("Database is at Version " + currentVersion);
				}
			}
		}
	}

	// internal helpers

	/**
	 * 現在のデータベースバージョンをDatabaseVersionテーブルから取得します。
	 * テーブルが存在しない場合は新たに作成し、バージョン0を設定します。
	 * 
	 * @return 現在のデータベースバージョン
	 */
	private DatabaseVersion findCurrentVersion() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet result = metadata.getTables(null, null, "DATABASEVERSION", null);
			try {
				// テーブルが存在するかを確認し、存在しない場合は作成します。
				if (result.next()) {
					Statement stmt = connection.createStatement();
					try {
						stmt.execute("select value from DatabaseVersion");
						ResultSet queryResult = stmt.getResultSet();
						try {
							queryResult.next();
							String value = queryResult.getString("value");
							queryResult.close();
							return DatabaseVersion.valueOf(value);
						} finally {
							queryResult.close();
						}
					} finally {
						stmt.close();
					}
				} else {
					Statement stmt = connection.createStatement();
					try {
						stmt.execute("create table DatabaseVersion (value varchar not null)");
						stmt.execute("insert into DatabaseVersion (value) values ('0')");
					} finally {
						stmt.close();
					}
					return DatabaseVersion.zero();
				}
			} finally {
				result.close();
			}
		} catch (SQLException e) {
			throw new DatabaseChangeException("Unable to determine database version", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

}