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

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

// SqlDatabaseChangeクラスは、SQLスクリプトをデータベースに適用するためのクラスです。
// SQL文を保持し、データベース接続を通じて実行します。

public class SqlDatabaseChange extends AbstractDatabaseChange {

	private final String sql;

	// コンストラクタ: SQL文を受け取り、インスタンスを初期化します。
	// sql: 実行するSQL文
	public SqlDatabaseChange(String sql) {
		this.sql = sql;
	}

	// doCreateStatementメソッド: データベース接続から新しいStatementを作成します。
	// connection: データベース接続オブジェクト
	@Override
	protected Statement doCreateStatement(Connection connection) throws SQLException {
		return connection.createStatement();
	}

	// doExecuteStatementメソッド: SQL文を実行します。
	// statement: 実行するStatementオブジェクト
	@Override
	protected void doExecuteStatement(Statement statement) throws SQLException {
		statement.execute(sql);
	}

	// inResourceメソッド: リソースからSQLスクリプトを読み込み、SqlDatabaseChangeオブジェクトを生成します。
	// resource: SQLスクリプトを含むリソース
	public static DatabaseChange inResource(Resource resource) {
		try {
			return new SqlDatabaseChange(readScript(resource));
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read sql resource: " + resource, e);
		}
	}

	// readScriptメソッド: リソースからSQLスクリプトを読み込み、文字列として返します。
	// resource: SQLスクリプトを含むリソース
	private static String readScript(Resource resource) throws IOException {
		EncodedResource encoded = resource instanceof EncodedResource ? (EncodedResource) resource
				: new EncodedResource(resource);
		LineNumberReader lnr = new LineNumberReader(encoded.getReader());
		String currentStatement = lnr.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (StringUtils.hasText(currentStatement) && !currentStatement.startsWith(SQL_COMMENT_PREFIX)) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lnr.readLine();
		}
		return scriptBuilder.toString();
	}

	private static final String SQL_COMMENT_PREFIX = "--";
}
