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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.web.FacebookCookieValue;
import org.springframework.social.facebook.web.FacebookWebArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.springsource.greenhouse.signup.AccountService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.signup.SignedUpGateway;
import com.springsource.greenhouse.signup.SignupForm;
import com.springsource.greenhouse.signup.SignupHelper;
import com.springsource.greenhouse.signup.SignupHelper.SignupCallback;

/**
 * メインの招待ページと招待受け入れフローのためのUIコントローラー。
 * 
 * @author Keith Donald
 */
@Controller
public class InviteController {

	private final InviteRepository inviteRepository;

	private final SignupHelper signupHelper;

	@Inject
	public InviteController(InviteRepository inviteRepository, AccountRepository accountRepository,
			SignedUpGateway signupGateway, AccountService accountService) {
		this.inviteRepository = inviteRepository;
		this.signupHelper = new SignupHelper(accountRepository, signupGateway, accountService);
	}

	/**
	 * メンバーにHTMLとしてルート招待ページを表示します。
	 * このページからユーザーはTwitterのフォロワー、Facebookの友達、
	 * およびメールの連絡先を招待することができます。
	 * Facebookのログインステータスを判断するために、
	 * {@link FacebookWebArgumentResolver}を介してクッキーから取得した
	 * FacebookUserIDがモデルにエクスポートされます。
	 */
	@GetMapping("/invite")
	public void invitePage(@FacebookCookieValue(value = "uid", required = false) String facebookUserId, Model model) {
		model.addAttribute("facebookUserId", facebookUserId);
		model.addAttribute("facebookAppId", facebookAppId);
	}

	/**
	 * 招待された人に招待を表示し、受け入れることを許可します。
	 */
	@GetMapping("/invite/accept")
	public String acceptInvitePage(@RequestParam String token, Model model, HttpServletResponse response)
			throws IOException {
		try {
			Invite invite = inviteRepository.findInvite(token);
			model.addAttribute(invite.getInvitee());
			model.addAttribute("sentBy", invite.getSentBy());
			model.addAttribute(invite.createSignupForm());
			model.addAttribute("token", token);
			return "invite/accept";
		} catch (InviteException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	/**
	 * 招待された人に代わって招待を受け入れます。
	 * 新しいメンバーアカウントが作成されます。
	 * すべてが成功した場合、招待された人は新しいアカウントでサインインされ、
	 * ホームページにリダイレクトされます。
	 */
	@PostMapping("/invite/accept")
	public String acceptInvite(final @RequestParam String token, @Valid SignupForm form, BindingResult formBinding,
			Model model) {
		if (formBinding.hasErrors()) {
			return form(token, model);
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupCallback() {
			public void postSignup(Account account) {
				inviteRepository.markInviteAccepted(token, account);
			}
		});
		return result ? "redirect:/" : form(token, model);
	}

	// internal helpers

	private String form(String token, Model model) {
		model.addAttribute("token", token);
		return null;
	}

	@Value("#{environment['facebook.appId']}")
	private String facebookAppId;

}