/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.test.transaction;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

// トランザクション管理を行うJUnitのテストルールを提供するクラス
// このクラスは、テストがトランザクション内で実行されることを保証し、
// テストが成功した場合はコミットし、失敗した場合はロールバックします。
public class Transactional implements TestRule {

	private EmbeddedDatabase database;

	// コンストラクタ: 埋め込みデータベースを受け取り、
	// トランザクション管理に使用します。
	public Transactional(EmbeddedDatabase database) {
		this.database = database;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return new TransactionalStatement(base);
	}

	// テストのステートメントをトランザクション内で実行するためのメソッド
	private class TransactionalStatement extends Statement {

		private final Statement next;

		public TransactionalStatement(Statement next) {
			this.next = next;
		}

		@Override
		public void evaluate() throws Throwable {
			PlatformTransactionManager tm = new DataSourceTransactionManager(database);
			TransactionStatus txStatus = tm.getTransaction(new DefaultTransactionDefinition());
			try {
				// ステートメントをトランザクション内で評価し、
				// 成功時にコミット、失敗時にロールバックします。
				try {
					next.evaluate();
				} catch (Throwable e) {
					tm.rollback(txStatus);
					throw e;
				}
				tm.commit(txStatus);
			} finally {
				// データベースをシャットダウンします。
				database.shutdown();
			}
		}

	}

}