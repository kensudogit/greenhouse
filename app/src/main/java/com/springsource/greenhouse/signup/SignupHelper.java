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
package com.springsource.greenhouse.signup;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Person;
import com.springsource.greenhouse.invite.InviteController;

import javax.inject.Inject;

/**
 * 新しいユーザーをサインアップさせるためのヘルパークラス。
 * 共通のサインアップUI制御とビジネスロジックをカプセル化します。
 * サインアップは通常の{@link SignupController}フローまたは{@link InviteController}の招待受け入れフローによってトリガーされる可能性があります。
 * 
 * @author Keith Donald
 */
public class SignupHelper {

	private final AccountRepository accountRepository;

	private final SignedUpGateway gateway;

	private final AccountService accountService;

	@Inject
	public SignupHelper(AccountRepository accountRepository, SignedUpGateway gateway, AccountService accountService) {
		this.accountRepository = accountRepository;
		this.gateway = gateway;
		this.accountService = accountService;
	}

	/**
	 * サインアップフォームを完了した人をサインアップさせます。
	 * サインアップが成功した場合はtrueを返し、エラーがあった場合はfalseを返します。
	 * フォームのBindingResultコンテキストにエラーを記録し、ユーザーに表示します。
	 */
	public boolean signup(SignupForm form, BindingResult formBinding) {
		return signup(form, formBinding, null);
	}

	/**
	 * サインアップフォームを完了した人をサインアップさせます。
	 * サインアップが成功した場合はtrueを返し、エラーがあった場合はfalseを返します。
	 * フォームのBindingResultコンテキストにエラーを記録し、ユーザーに表示します。
	 * 成功したメンバーサインアップ後にカスタム処理を行うためのオプションのSignupCallbackを指定できます。
	 */
	public boolean signup(SignupForm form, BindingResult formBinding, SignupCallback callback) {
		try {
			Account account = accountService.createAccount(form.createPerson());
			gateway.signedUp(account);
			AccountUtils.signin(account);
			if (callback != null) {
				callback.postSignup(account);
			}
			return true;
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return false;
		}
	}

	// internal helpers

	public Account createAccount(Person person, SignupCallback callback) throws EmailAlreadyOnFileException {
		return accountRepository.createAccount(person);
	}

	/**
	 * 新しいメンバーアカウントのサインアップ後にカスタム後処理を行うために呼び出されます。
	 */
	public interface SignupCallback {

		void postSignup(Account account);

	}

}