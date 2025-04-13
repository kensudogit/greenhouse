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
import javax.validation.Valid;

import org.springframework.model.FieldModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springsource.greenhouse.account.SignInNotFoundException;
import com.springsource.greenhouse.utils.Message;

/**
 * ユーザーのパスワードをリセットするためのUIコントローラー。
 * このコントローラーは一般公開されており、サインインは不要です。
 * ただし、セキュアなユーザー資格情報を受け取るため、SSL経由でアクセスする必要があります。
 * 
 * @author Keith Donald
 */
@Controller
public class ResetPasswordController {

	private static final String REDIRECT_RESET = "redirect:/reset";

	private final ResetPasswordService service;

	@Inject
	public ResetPasswordController(ResetPasswordService service) {
		this.service = service;
	}

	/**
	 * ユーザーのWebブラウザにリセットパスワードフォームをHTMLとしてレンダリングします。
	 */
	@GetMapping("/reset")
	public void resetPage(Model model) {
		model.addAttribute("username", new FieldModel<String>());
	}

	/**
	 * メンバーのパスワードをリセットするためのリクエストを処理します。
	 * {@link ResetPasswordService}を呼び出して、リセットパスワードメールを送信します。
	 * メールメッセージには、パスワードリセット操作を続行するために提示する必要があるリクエストトークンが含まれます。
	 */
	@PostMapping("/reset")
	public String sendResetMail(@RequestParam String signin, Model model, RedirectAttributes redirectAttrs) {
		try {
			service.sendResetMail(signin);
			redirectAttrs.addFlashAttribute(
					Message.info("メールが送信されました。パスワードをリセットするための指示に従ってください。"));
			return REDIRECT_RESET;
		} catch (SignInNotFoundException e) {
			model.addAttribute("signin", FieldModel.error("ファイルにありません", signin));
			return null;
		}
	}

	/**
	 * リセットトークンが検証された後、ユーザーにパスワード変更フォームをレンダリングします。
	 */
	@GetMapping("/reset")
	public String changePasswordForm(@RequestParam String token, Model model) {
		if (!service.isValidResetToken(token)) {
			return "reset/invalidToken";
		}
		model.addAttribute(new ChangePasswordForm());
		return "reset/changePassword";
	}

	/**
	 * パスワード変更の送信を処理し、ユーザーのパスワードをリセットします。
	 */
	@PostMapping("/reset")
	public String changePassword(@RequestParam String token,
			@Valid ChangePasswordForm form, BindingResult formBinding, Model model, RedirectAttributes redirectAttrs) {
		if (formBinding.hasErrors()) {
			model.addAttribute("token", token);
			return "reset/changePassword";
		}
		try {
			service.changePassword(token, form.getPassword());
			redirectAttrs.addFlashAttribute("パスワードがリセットされました");
			return REDIRECT_RESET;
		} catch (InvalidResetTokenException e) {
			redirectAttrs
					.addFlashAttribute(Message.error("リセットパスワードセッションの有効期限が切れました。もう一度お試しください。"));
			return REDIRECT_RESET;
		}
	}

}