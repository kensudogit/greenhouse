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
package com.springsource.greenhouse.settings;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.springsource.greenhouse.account.Account;

/**
 * メンバーアカウント設定を変更するためのUIコントローラー。
 * 
 * @author Keith Donald
 */
@Controller
// TODO move the jdbc logic here into AppRepository
public class SettingsController {

	private JdbcTemplate jdbcTemplate;

	private TokenStore tokenStore;

	@Inject
	public SettingsController(TokenStore tokenStore, JdbcTemplate jdbcTemplate) {
		this.tokenStore = tokenStore;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * メンバーのWebブラウザに設定ページをHTMLとしてレンダリングします。
	 * メンバーがアカウントと接続したアプリケーションのリストをモデルに追加します
	 * （必要に応じてそれらのアプリを切断するため）。
	 */
	@GetMapping("/settings")
	public void settingsPage(Account account, Model model) {
		// TODO: このクエリを修正する
		List<Map<String, Object>> apps = jdbcTemplate.queryForList(
				"select a.name as name, c.token_id as accessToken from App a, oauth_access_token c where c.client_id = a.apiKey and c.user_name = ?",
				account.getId());
		model.addAttribute("apps", apps);
	}

	/**
	 * クライアントアプリケーションに以前付与されたアカウントアクセスを取り消します。
	 * この操作を実行すると、クライアントアプリケーションはメンバーのデータにアクセスしたり更新したりできなくなります。
	 * メンバーがクライアントアプリケーションを使用したくない場合や、クライアントアプリケーションが何らかの形で危険にさらされた場合に使用します
	 * （例えば、アプリがインストールされたユーザーの携帯電話が盗まれた場合など）。
	 * 
	 * @param accessToken クライアントアプリケーションとメンバーアカウント間の接続を識別するアクセストークン
	 * @param account     アクセス許可の終了を要求するメンバー
	 * @return 設定ページへのリダイレクト。
	 */
	@DeleteMapping("/settings/apps/{accessToken}")
	public String disconnectApp(@PathVariable String accessToken, Account account) {
		tokenStore.removeAccessToken(new DefaultOAuth2AccessToken(accessToken));
		return "redirect:/settings";
	}

}
