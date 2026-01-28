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
package com.springsource.greenhouse.invite.mail;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Message;

/**
 * メール招待を送信するためのUIコントローラー。
 * 
 * @author Keith Donald
 */
@Controller
public class MailInviteController {

	private final StringTemplateFactory inviteTemplateFactory = new ResourceStringTemplateFactory(
			new ClassPathResource("invite-text-body.st", getClass()));

	private final MailInviteService inviteService;

	@Inject
	public MailInviteController(MailInviteService inviteService) {
		this.inviteService = inviteService;
	}

	/**
	 * メール招待フォームをHTMLとしてウェブブラウザにレンダリングします。
	 * 招待テキストは、送信者がカスタマイズできる標準テキストで事前に入力されています。
	 */
	@GetMapping("/invite/mail")
	public MailInviteForm invitePage(Account account) {
		MailInviteForm form = new MailInviteForm();
		form.setInvitationText(renderStandardInvitationText(account));
		return form;
	}

	/**
	 * 招待フォームの送信を処理し、招待を送信します。
	 * 成功した場合、招待フォームにリダイレクトし、成功メッセージを表示します。
	 */
	@PostMapping("/invite/mail")
	public String sendInvites(@Valid MailInviteForm form, BindingResult result, Account account,
			RedirectAttributes redirectAttrs) {
		if (result.hasErrors()) {
			return null;
		}
		inviteService.sendInvite(account, form.getInvitees(), form.getInvitationText());
		redirectAttrs.addFlashAttribute(Message.success("Your invitations have been sent"));
		return "redirect:/invite/mail";
	}

	// internal helpers

	/**
	 * 標準の招待テキストをレンダリングします。
	 */
	private String renderStandardInvitationText(Account account) {
		StringTemplate template = inviteTemplateFactory.getStringTemplate();
		template.put("account", account);
		return template.render();
	}

}