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
package com.springsource.greenhouse.members;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import com.springsource.greenhouse.account.Account;

/**
 * クラスの説明: メンバーの公開プロフィールを管理するためのUIコントローラー
 * 
 * @author Keith Donald
 * @author Craig Walls
 */
@RestController
@RequestMapping("/members")
public class MembersController {

	// プロフィールリポジトリのインスタンスを保持
	private final ProfileRepository profileRepository;

	@Inject
	public MembersController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	/**
	 * メソッドの説明: 現在サインインしているメンバーのプロフィールをJSON形式でレスポンスに書き込む
	 */
	@GetMapping(value = "/@self", produces = "application/json")
	public Profile profile(Account account) {
		return profileRepository.findByAccountId(account.getId());
	}

	/**
	 * メソッドの説明: リクエストされたメンバーのプロフィールをHTMLとしてユーザーのウェブブラウザにレンダリングする
	 * プロフィールページは一般公開されており、閲覧にサインインは不要
	 */
	@GetMapping("/{profileKey}")
	public String profileView(@PathVariable String profileKey, Model model) {
		Profile profile = profileRepository.findById(profileKey);
		model.addAttribute(profile);
		model.addAttribute("connectedProfiles", profileRepository.findConnectedProfiles(profile.getAccountId()));
		model.addAttribute("metadata", buildFacebookOpenGraphMetadata(profile));
		model.addAttribute("facebookAppId", facebookAppId);
		return "members/view";
	}

	// 特定のロジックの説明: Facebookの「いいね」ウィジェットに必要なメタデータを生成
	private Map<String, String> buildFacebookOpenGraphMetadata(Profile profile) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", profile.getDisplayName());
		metadata.put("og:type", "public_figure");
		metadata.put("og:site_name", siteName);
		metadata.put("fb:app_id", facebookAppId);
		return metadata;
	}

	@Value("#{environment['facebook.appId']}")
	private String facebookAppId;

	@Value("${site.name}")
	private String siteName;

}
