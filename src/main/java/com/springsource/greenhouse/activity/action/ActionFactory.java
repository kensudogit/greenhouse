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
package com.springsource.greenhouse.activity.action;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * コールバックインターフェースは、Actionインスタンスを構築するためのものです。
 * 
 * @author Keith Donald
 * @param <A> Actionの型
 */
public interface ActionFactory<A extends Action> {

	/**
	 * 型AのActionインスタンスを作成します。
	 * このコールバックにより、呼び出し元はActionの実装を指定し、その特定のフィールドを設定することができます。
	 * また、このActionの特化型の追加プロパティをシステムレコードと共に永続化することも可能です。
	 * 
	 * @param id       割り当てられた内部アクションID
	 * @param time     アクションが実行された時間、現在の時間
	 * @param account  アクションを実行したメンバー
	 * @param location アクションが実行された場所の解決済みメンバーの位置
	 * @return Actionインスタンス
	 */
	A createAction(Long id, DateTime time, Account account, Location location);
}