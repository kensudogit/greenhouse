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
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleAction クラスの定義。
 * 単一の String 'type' フィールドを持ち、実行されたアクションの種類をキャプチャします。
 * 例: signedUp。
 * 
 * @author Keith Donald
 */
public final class SimpleAction extends Action {

	private final String type;
	private static final Map<String, String> actionTextMap = new HashMap<>();

	static {
		actionTextMap.put("SignedUp", "signed up");
		// Add more action types and their corresponding text here
	}

	public SimpleAction(String type, Long id, DateTime time, Account account, Location location) {
		super(id, time, account, location);
		this.type = type;
	}

	/**
	 * 実行されたアクションの種類を返します。
	 */
	public String getType() {
		return type;
	}

	/**
	 * アクションの種類に基づいて文字列を返します。
	 */
	public String toString() {
		String actionText = actionTextMap.getOrDefault(getType(), getType());
		return getAccount().getFullName() + " " + actionText;
	}

}