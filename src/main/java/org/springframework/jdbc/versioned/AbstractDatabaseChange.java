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
import java.sql.SQLException;
import java.sql.Statement;

// クラスの説明: データベースの変更を適用するための抽象クラス
public abstract class AbstractDatabaseChange implements DatabaseChange {

	/**
	 * メソッドの説明: この変更を適用します。
	 */
	public void apply(Connection connection) {
		Statement statement = getStatement(connection);
		try {
			// 特定のロジックの説明: ステートメントを実行します
			doExecuteStatement(statement);
		} catch (SQLException e) {
			// 特定のロジックの説明: 例外が発生した場合、データベース変更例外をスローします
			throw new DatabaseChangeException("Failed to apply database change", e);
		} finally {
			try {
				// 特定のロジックの説明: ステートメントを閉じます
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	// メソッドの説明: ステートメントを取得します
	private Statement getStatement(Connection connection) {
		try {
			// 特定のロジックの説明: ステートメントを作成します
			return doCreateStatement(connection);
		} catch (SQLException e) {
			// 特定のロジックの説明: ステートメントの作成に失敗した場合、例外をスローします
			throw new IllegalStateException("Could not create statement", e);
		}
	}

	protected abstract Statement doCreateStatement(Connection connection) throws SQLException;

	protected abstract void doExecuteStatement(Statement statement) throws SQLException;
}
