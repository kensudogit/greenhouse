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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.ProfileReference;
import com.springsource.greenhouse.utils.Message;

/**
 * Facebookの友達をコミュニティに招待するためのUIコントローラー。
 * 
 * @author Keith Donald
 */
@Controller
public class FacebookInviteController {

	private final Facebook facebook;

	private final UsersConnectionRepository connectionRepository;

	private final AccountRepository accountRepository;

	/**
	 * FacebookInviteControllerを構築します。
	 * FacebookのREST APIへのクライアントサイドプロキシを取得するためにFacebook ServiceProviderが注入されます。
	 * また、メンバーのFacebookの友達のうち、すでにコミュニティに参加している人を調べるためにも使用されます。
	 */
	@Inject
	public FacebookInviteController(Facebook facebook, UsersConnectionRepository connectionRepository,
			AccountRepository accountRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
		this.accountRepository = accountRepository;
	}

	/**
	 * Facebookの友達検索ページをHTMLとしてブラウザにレンダリングします。
	 * メンバーのアカウントがFacebookに接続されている場合、すでにメンバーであるFacebookの友達をモデルに追加します。
	 * すでに参加しているFacebookの友達と、まだ参加していない友達を区別することをサポートします。
	 */
	@GetMapping("/invite/facebook")
	public void friendFinder(Model model, Account account) {
		if (facebook.isAuthorized()) {
			List<ProfileReference> profileReferences = accountRepository
					.findProfileReferencesByIds(friendAccountIds(facebook));
			model.addAttribute("friends", profileReferences);
		} else {
			model.addAttribute("friends", Collections.emptySet());
		}
		model.addAttribute("facebookAppId", facebookAppId);
	}

	/**
	 * Facebookのマルチフレンドセレクターフォームによって招待が送信された後に呼び出されます。
	 * ユーザーを招待ページにリダイレクトし、成功メッセージをレンダリングします。
	 *
	 * @param inviteIds Facebookによって割り当てられた招待ID、送信された各招待に対して1つ
	 */
	@RequestMapping(value = "/invite/facebook/request-form", method = RequestMethod.POST)
	public String invitationsSent(@RequestParam("ids[]") String[] inviteIds, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute(Message.success("Your invitations have been sent"));
		return "redirect:/invite";
	}

	/**
	 * Facebookのマルチフレンドセレクターフォームによって1つ以上の招待の送信をキャンセルすることを選択した後に呼び出されます。
	 * ユーザーをメインの招待ページにリダイレクトします。
	 */
	@GetMapping("/invite/facebook/request-form")
	public String invitationsCancelled() {
		return "redirect:/invite";
	}

	// internal helpers

	/**
	 * Facebookの友達のアカウントIDを取得します。
	 *
	 * @param facebook Facebookのインスタンス
	 * @return 友達のアカウントIDのリスト
	 */
	private List<Long> friendAccountIds(Facebook facebook) {
		List<Reference> friends = facebook.friendOperations().getFriends();
		if (friends.isEmpty()) {
			return Collections.emptyList();
		}
		Set<String> providerUserIds = new HashSet<String>(friends.size());
		for (Reference friend : friends) {
			providerUserIds.add(friend.getId());
		}
		Set<String> userIds = connectionRepository.findUserIdsConnectedTo("facebook", providerUserIds);
		List<Long> friendAccountIds = new ArrayList<Long>(userIds.size());
		for (String localUserId : userIds) {
			friendAccountIds.add(Long.valueOf(localUserId));
		}
		return friendAccountIds;
	}

	@Value("#{environment['facebook.appId']}")
	private String facebookAppId;

}