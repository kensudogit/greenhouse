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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.hibernate.validator.util.LazyValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestAttributes;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.signup.SignupHelper.SignupCallback;
import com.springsource.greenhouse.utils.Message;
import com.springsource.greenhouse.utils.MessageType;

/**
 * 新しいメンバーのサインアップ用UIコントローラー。
 * このコントローラーは、ユーザーがサインアップするためのフォームを提供し、
 * サインアップの処理を行います。
 * サインアップが成功した場合、ユーザーをホームページにリダイレクトします。
 * 
 * @author Keith Donald
 */
@Controller
public class SignupController {

	// サインアップヘルパーのインスタンスを保持します。
	private final SignupHelper signupHelper;

	// メッセージ属性の定数を定義します。
	private static final String MESSAGE_ATTRIBUTE = "message";

	@Inject
	public SignupController(AccountRepository accountRepository, SignedUpGateway gateway) {
		// サインアップヘルパーを初期化します。
		this.signupHelper = new SignupHelper(accountRepository, gateway);
	}

	/**
	 * サインアップフォームをHTMLとしてレンダリングし、ユーザーのウェブブラウザに表示します。
	 * プロバイダーのアカウントが関連付けられていない場合、
	 * ユーザーにサインアップを促すメッセージを表示します。
	 */
	@GetMapping("/signup")
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if (connection != null) {
			// プロバイダーのアカウントが関連付けられていない場合のメッセージを設定
			request.setAttribute(MESSAGE_ATTRIBUTE,
					new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId())
							+ " account is not associated with a Greenhouse account. If you're new, please sign up."),
					RequestAttributes.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	/**
	 * サインアップフォームの送信を処理します。
	 * 実際のサインイン処理は {@link SignupHelper} に委任されます。
	 * サインインが成功した場合、新しいメンバーをアプリケーションのホームページにリダイレクトします。
	 */
	@PostMapping("/signup")
	public String signup(@Valid SignupForm form, BindingResult formBinding, final WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupCallback() {
			public void postSignup(Account account) {
				// サインアップ後の処理を行います。
				ProviderSignInUtils.handlePostSignUp(account.getId().toString(), request);
			}
		});
		return result ? "redirect:/" : null;
	}

	/**
	 * APIからのサインアップフォームの送信を処理します。
	 * バリデーションエラーがある場合、エラーメッセージを返します。
	 * サインアップが成功した場合、アカウント作成のメッセージを返します。
	 */
	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signupFromApi(@RequestBody SignupForm form) {

		// SPR-9826が修正されるまでの一時的な手動バリデーション。
		BindingResult formBinding = validate(form);

		if (formBinding.hasErrors()) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put(MESSAGE_ATTRIBUTE, "Validation error");
			errorResponse.put("errors", getErrorsMap(formBinding));
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		boolean result = signupHelper.signup(form, formBinding);

		if (result) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put(MESSAGE_ATTRIBUTE, "Account created");
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.CREATED);
		} else {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put(MESSAGE_ATTRIBUTE, "Account creation error");
			errorResponse.put("errors", getErrorsMap(formBinding));
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * サインアップフォームのバリデーションを行います。
	 * バリデーションエラーがある場合、エラーを返します。
	 */
	private BindException validate(SignupForm form) {
		BindException errors;
		errors = new BindException(form, "signupForm");
		LazyValidatorFactory lvf = new LazyValidatorFactory();
		Validator validator = new SpringValidatorAdapter(lvf.getValidator());
		ValidationUtils.invokeValidator(validator, form, errors);
		return errors;
	}

	/**
	 * バリデーションエラーをマップ形式で取得します。
	 * 各フィールドのエラー情報を含みます。
	 */
	private List<Map<String, String>> getErrorsMap(BindingResult formBinding) {
		List<FieldError> fieldErrors = formBinding.getFieldErrors();
		List<Map<String, String>> errors = new ArrayList<Map<String, String>>(fieldErrors.size());
		for (FieldError fieldError : fieldErrors) {
			Map<String, String> fieldErrorMap = new HashMap<String, String>();
			fieldErrorMap.put("field", fieldError.getField());
			fieldErrorMap.put("code", fieldError.getCode());
			fieldErrorMap.put("message", fieldError.getDefaultMessage());
			errors.add(fieldErrorMap);
		}
		return errors;
	}

}