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
 * アプリケーションシステムでメンバーが実行したアクションをモデル化します。
 * メンバーの活動を記録するために使用されます。
 * アクションはバッジシステムへの入力としても機能します（アクションを実行するとバッジが授与されることがあります）。
 * アクションは明示的に実行される場合もあれば、メンバーの代わりに暗黙的にトリガーされる場合もあります。
 * 
 * @author Keith Donald
 */
public abstract class Action {

	private final Long id;

	private final DateTime time;

	private final Account account;

	private final Location location;

	protected Action(Long id, DateTime time, Account account, Location location) {
		this.id = id;
		this.time = time;
		this.account = account;
		this.location = location;
	}

	/**
	 * このアクションの内部識別子。
	 */
	public Long getId() {
		return id;
	}

	/**
	 * アクションが実行された日時。
	 */
	public DateTime getTime() {
		return time;
	}

	/**
	 * アクションを実行したメンバーアカウント。
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * アクションが実行された場所。
	 * ジオタグが無効になっている場合はnullになることがあります。
	 */
	public Location getLocation() {
		return location;
	}

}