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
package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.utils.EmailUtils;

/**
 * 招待を送るべき、または送られた人を表すクラス。
 * コミュニティに参加してほしい人を管理します。
 * 
 * @author Keith Donald
 */
public final class Invitee {

	private final String firstName;

	private final String lastName;

	private final String email;

	/**
	 * コンストラクタ。指定された名前とメールアドレスでInviteeを初期化します。
	 * 
	 * @param firstName 招待者の名前の最初の部分
	 * @param lastName  招待者の名前の最後の部分
	 * @param email     招待者のメールアドレス
	 * @throws IllegalArgumentException メールアドレスが無効な場合
	 */
	public Invitee(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		if (!EmailUtils.isEmail(email)) {
			// メールアドレスの形式が正しいかを確認します。
			throw new IllegalArgumentException("'" + email + "' is not a valid email address");
		}
		this.email = email;
	}

	/**
	 * 人の名前の最初の部分。
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 人の名前の最後の部分。
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 招待を送るべきメールアドレス。
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Inviteeインスタンスを文字列から解析します。
	 * 形式は<code>[firstName] [lastName] &lt;email&gt;</code>です。
	 * 例: Bob Sanders &lt;bob@sanders.com&gt;。
	 * 名前のフィールドはオプションです。
	 * 1つの名前フィールドのみが提供された場合、それはfirstNameとして解析されます。
	 * 
	 * @param inviteeString 解析する招待者の文字列
	 * @return 解析されたInvitee
	 * @throws IllegalArgumentException フォーマットエラーによる解析失敗時
	 */
	public static Invitee valueOf(String inviteeString) {
		if (inviteeString == null || inviteeString.isEmpty()) {
			// 招待者の文字列がnullまたは空の場合、例外をスローします。
			throw new IllegalArgumentException("The Invitee string to parse cannot be null or empty");
		}
		String[] pieces = inviteeString.trim().split("[<>]");
		// 文字列を<と>で分割し、名前とメールアドレスを抽出します。
		if (pieces.length == 1) {
			return new Invitee(pieces[0]);
		} else if (pieces.length == 2) {
			String[] name = pieces[0].trim().split(" ");
			String email = pieces[1];
			if (name.length == 1) {
				if (name[0].isEmpty()) {
					return new Invitee(email);
				} else {
					return new Invitee(name[0], email);
				}
			} else if (name.length == 2) {
				return new Invitee(name[0], name[1], pieces[1]);
			}
		}
		// 招待者の文字列が無効な場合、例外をスローします。
		throw new IllegalArgumentException("Unable to parse invalid invitee string '" + inviteeString + "'");
	}

	/**
	 * Inviteeオブジェクトを文字列に変換します。
	 * 
	 * @return Inviteeの文字列表現
	 */
	public String toString() {
		if (firstName == null && lastName == null) {
			return email;
		}
		StringBuilder builder = new StringBuilder();
		if (firstName != null) {
			builder.append(firstName);
			builder.append(' ');
		}
		if (lastName != null) {
			builder.append(lastName);
			builder.append(' ');
		}
		builder.append('<').append(email).append('>');
		return builder.toString();
	}

	// internal helpers

	private Invitee(String email) {
		this(null, null, email);
	}

	private Invitee(String firstName, String email) {
		this(firstName, null, email);
	}

}
