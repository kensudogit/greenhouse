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
package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.SignInNotFoundException;

/**
 * このクラスは、JDBC APIを使用してリレーショナルデータベースにリセットパスワードリクエストを保存する
 * ResetPasswordServiceの実装です。メンバーのパスワードを実際に変更するために
 * AccountRepositoryに委譲します。リセットパスワードメールを送信するために
 * ResetPasswordMailerに委譲します。ユニークなリセットパスワードトークンを生成するために
 * SecureRandomStringKeyGeneratorに委譲します。
 * 
 * @author Keith Donald
 */
@Service
public class JdbcRestPasswordService implements ResetPasswordService {

	private final JdbcTemplate jdbcTemplate;

	private final AccountRepository accountRepository;

	private final ResetPasswordMailer mailer;

	private final StringKeyGenerator tokenGenerator = KeyGenerators.string();

	@Inject
	public JdbcRestPasswordService(JdbcTemplate jdbcTemplate, AccountRepository accountRepository,
			ResetPasswordMailer mailer) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
		this.mailer = mailer;
	}

	/**
	 * 指定されたユーザー名にリセットメールを送信します。
	 * 
	 * @param username ユーザー名
	 * @throws SignInNotFoundException ユーザーが見つからない場合にスローされます。
	 */
	@Transactional
	public void sendResetMail(String username) throws SignInNotFoundException {
		Account account = accountRepository.findBySignin(username);
		String token = tokenGenerator.generateKey();
		jdbcTemplate.update("insert into ResetPassword (token, member) values (?, ?)", token, account.getId());
		mailer.send(new ResetPasswordRequest(token, account));
	}

	/**
	 * リセットトークンが有効かどうかを確認します。
	 * 
	 * @param token リセットトークン
	 * @return トークンが有効な場合はtrue、無効な場合はfalse
	 */
	public boolean isValidResetToken(String token) {
		return jdbcTemplate.queryForInt("select count(*) from ResetPassword where token = ?", token) == 1;
	}

	/**
	 * トークンに基づいてパスワードを変更します。
	 * 
	 * @param token    リセットトークン
	 * @param password 新しいパスワード
	 * @throws InvalidResetTokenException トークンが無効な場合にスローされます。
	 */
	@Transactional
	public void changePassword(String token, String password) throws InvalidResetTokenException {
		Long accountId = findAccountIdByToken(token);
		accountRepository.changePassword(accountId, password);
		jdbcTemplate.update("delete from ResetPassword where token = ?", token);
	}

	// 内部ヘルパーメソッド

	/**
	 * トークンに基づいてアカウントIDを検索します。
	 * 
	 * @param token リセットトークン
	 * @return アカウントID
	 * @throws InvalidResetTokenException トークンが無効な場合にスローされます。
	 */
	private Long findAccountIdByToken(String token) throws InvalidResetTokenException {
		try {
			// データベースからアカウントIDを取得します。
			return jdbcTemplate.queryForLong("select member from ResetPassword where token = ?", token);
		} catch (EmptyResultDataAccessException e) {
			// トークンが見つからない場合の例外処理
			throw new InvalidResetTokenException(token);
		}
	}

}