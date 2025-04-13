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
package com.springsource.greenhouse.account;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * このクラスは、Spring SecurityのAuthenticationProviderインターフェースを実装します。
 * アカウントリポジトリを使用して認証を行います。
 * 認証されたアカウントは、{@link Authentication#getPrincipal() Authentication
 * Principal}として扱われます。
 * 
 * @author Keith Donald
 */
@Service
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	// アカウントリポジトリのインスタンスを保持します。
	private AccountRepository accountRepository;

	@Inject
	public UsernamePasswordAuthenticationProvider(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	// 認証を実行するメソッドです。
	// ユーザー名とパスワードを使用してアカウントを認証します。
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		try {
			Account account = accountRepository.authenticate(token.getName(), (String) token.getCredentials());
			return authenticatedToken(account, authentication);
		} catch (SignInNotFoundException e) {
			throw new org.springframework.security.core.userdetails.UsernameNotFoundException(token.getName(), e);
		} catch (InvalidPasswordException e) {
			throw new BadCredentialsException("Invalid password", e);
		}
	}

	// このプロバイダがサポートする認証トークンのクラスを確認します。
	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

	// 内部ヘルパーメソッド
	// 認証されたトークンを生成します。
	private Authentication authenticatedToken(Account account, Authentication original) {
		List<GrantedAuthority> authorities = null;
		UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(account, null,
				authorities);
		authenticated.setDetails(original.getDetails());
		return authenticated;
	}

}