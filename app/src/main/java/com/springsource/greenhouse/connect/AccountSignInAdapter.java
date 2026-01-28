/*
 * Copyright 2011 the original author or authors.
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
package com.springsource.greenhouse.connect;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;

/**
 * Supports user sign-in with provider accounts.
 * This class finds accounts by user ID and handles sign-in.
 * 
 * @author Keith Donald
 */
public class AccountSignInAdapter implements SignInAdapter {

	private final AccountRepository accountRepository;

	private final RequestCache requestCache;

	@Inject
	public AccountSignInAdapter(AccountRepository accountRepository, RequestCache requestCache) {
		this.accountRepository = accountRepository;
		this.requestCache = requestCache;
	}

	/**
	 * Finds account by user ID and handles sign-in.
	 * 
	 * @param userId     user ID
	 * @param connection social connection
	 * @param request    native web request
	 * @return original URL
	 */
	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		Account account = accountRepository.findById(Long.valueOf(userId));
		AccountUtils.signin(account);
		return extractOriginalUrl(request);
	}

	// internal helpers

	/**
	 * Extracts the original URL from the request cache.
	 * 
	 * @param request native web request
	 * @return redirect URL
	 */
	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		requestCache.removeRequest(nativeReq, nativeRes);
		removeAuthenticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}

	/**
	 * Removes authentication attributes from session.
	 * 
	 * @param session HTTP session
	 */
	private void removeAuthenticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
