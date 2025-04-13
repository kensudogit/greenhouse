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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.ProfileReference;

/**
 * Twitter招待ページのUIコントローラー。
 * <p>
 * このコントローラーは、ユーザーがTwitterを通じて友達を招待するためのページを提供します。
 * Twitter APIを使用して、ユーザーのフォロワーを検索し、
 * その中からコミュニティのメンバーを見つけ出す機能を持っています。
 * 
 * @author Keith Donald
 */
@Controller
public class TwitterInviteController {

	private final Twitter twitter;

	private final UsersConnectionRepository connectionRepository;

	private final AccountRepository accountRepository;

	@Inject
	public TwitterInviteController(Twitter twitter, UsersConnectionRepository connectionRepository,
			AccountRepository accountRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
		this.accountRepository = accountRepository;
	}

	/**
	 * メンバーにTwitter招待ページを表示します。
	 * <p>
	 * ユーザーがTwitterに認証されている場合、
	 * モデルにユーザーのTwitterスクリーンネームを追加し、
	 * 友達検索フォームを事前に入力します。
	 */
	@GetMapping("/invite/twitter")
	public void friendFinder(Model model) {
		if (twitter.isAuthorized()) {
			model.addAttribute("username", twitter.userOperations().getScreenName());
		}
	}

	/**
	 * コミュニティのメンバーであるユーザーのTwitterフォロワーのリストを表示します。
	 * <p>
	 * 通常、JavaScriptを介してAjaxリクエストで呼び出されます。
	 * そうでない場合、結果を含む招待ページ全体を再レンダリングします。
	 */
	@GetMapping("/invite/twitter")
	public String findFriends(@RequestParam String username, Model model) {
		if (StringUtils.hasText(username)) {
			List<ProfileReference> profileReferences = accountRepository
					.findProfileReferencesByIds(friendAccountIds(username));
			model.addAttribute("friends", profileReferences);
		}
		return "invite/twitterFriends";
	}

	private List<Long> friendAccountIds(String username) {
		List<Long> friendIds = twitter.friendOperations().getFriendIds(username);
		Set<String> providerUserIds = new HashSet<String>(friendIds.size());
		for (Object friendId : friendIds) {
			providerUserIds.add(friendId.toString());
		}
		Set<String> userIds = connectionRepository.findUserIdsConnectedTo("twitter", providerUserIds);
		List<Long> friendAccountIds = new ArrayList<Long>(userIds.size());
		for (String localUserId : userIds) {
			friendAccountIds.add(Long.valueOf(localUserId));
		}
		return friendAccountIds;
	}

}