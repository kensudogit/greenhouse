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
package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * JoinRowMapper クラスは、複数の行を単一のオブジェクトにマッピングするための抽象テンプレートです。
 * これは、1対多の関係を持つ結合で、ルート (または集約) ごとに複数の行が返される場合に便利です。
 * 例えば、「プレゼンテーションには複数のスピーカーがいる」という関係を考えてみましょう。
 * スピーカーテーブルと結合してプレゼンテーションオブジェクトを構築する際、
 * プレゼンテーションに複数のスピーカーがいる場合、複数の行が返されます。
 * このテンプレートはそのような場合に役立ちます。
 * This class has been submitted for contribution to Spring JDBC; see SPR-7698.
 * 
 * @author Keith Donald
 * @param <R> the root, or aggregate, entity type
 * @param <I> the root's id property type
 */
public abstract class JoinRowMapper<R, I> {

	/**
	 * single メソッドは、1対多の関係で複数の R 行がある場合に、
	 * 正確に 1 つのルートオブジェクト R をマッピングする RowMapper を返します。
	 */
	public RowMapper<R> single() {
		return singleMapper;
	}

	/**
	 * list メソッドは、1..n のルートオブジェクト R をリストにマッピングする ResultSetExtractor を返します。
	 * ここでは、結合された子ごとに複数の R 行が存在する可能性があります。
	 */
	public ResultSetExtractor<List<R>> list() {
		return listMapper;
	}

	// subclassing hooks

	/**
	 * mapId メソッドは、ResultSet の現在の行からルートエンティティの ID プロパティ I をマッピングします。
	 */
	protected abstract I mapId(ResultSet rs) throws SQLException;

	/**
	 * mapRoot メソッドは、ResultSet の現在の行からルートオブジェクト R をマッピングします。
	 * 直接のプロパティを含み、子の関連プロパティを除外します。
	 */
	protected abstract R mapRoot(I id, ResultSet rs) throws SQLException;

	/**
	 * addChild メソッドは、次の子オブジェクトをマッピングし、ルートオブジェクト R に追加します。
	 */
	protected abstract void addChild(R root, ResultSet rs) throws SQLException;

	// internal helpers

	private final RowMapper<R> singleMapper = new RowMapper<R>() {
		public R mapRow(ResultSet rs, int rowNum) throws SQLException {
			I id = mapId(rs);
			R root = mapRoot(id, rs);
			addChild(root, rs);
			while (rs.next() && mapId(rs).equals(id)) {
				addChild(root, rs);
			}
			return root;
		}
	};

	private final ResultSetExtractor<List<R>> listMapper = new ResultSetExtractor<List<R>>() {
		public List<R> extractData(ResultSet rs) throws SQLException, DataAccessException {
			return mapInto(new ArrayList<R>(), rs);
		}

		private <C extends Collection<R>> C mapInto(C collection, ResultSet rs) throws SQLException {
			R root = null;
			I previousId = null;
			while (rs.next()) {
				I id = mapId(rs);
				if (!id.equals(previousId)) {
					root = mapRoot(id, rs);
					collection.add(root);
				}
				addChild(root, rs);
				previousId = id;
			}
			return collection;
		}
	};

}