/*
 * Copyright 2012 the original author or authors.
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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.members.ProfilePictureService;
import com.springsource.greenhouse.utils.Message;

/**
 * ユーザーがFacebookに接続した後、ユーザーの代わりにウォールに投稿することをサポートします。
 * また、ユーザーのFacebookプロフィール画像をローカルのプロフィール画像として使用することもサポートします。
 * 
 * @author Keith Donald
 */
public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

	private final ProfilePictureService profilePictureService;

	@Inject
	public FacebookConnectInterceptor(ProfilePictureService profilePictureService) {
		this.profilePictureService = profilePictureService;
	}

	public void preConnect(ConnectionFactory<Facebook> provider, MultiValueMap<String, String> params,
			WebRequest request) {
		// Facebook接続前の処理を行います。
		// リクエストパラメータに基づいて、ウォールへの投稿やプロフィール画像の使用を設定します。
		if (StringUtils.hasText(request.getParameter(POST_TO_WALL_PARAMETER))) {
			request.setAttribute(POST_TO_WALL_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
		if (StringUtils.hasText(request.getParameter(USE_FACEBOOK_IMAGE_PARAMETER))) {
			request.setAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(Connection<Facebook> connection, WebRequest request) {
		// Facebook接続後の処理を行います。
		// ユーザーのウォールに投稿し、Facebookプロフィール画像を使用します。
		Account account = AccountUtils.getCurrentAccount();
		postToWall(connection, account, request);
		useFacebookProfileImage(connection, account, request);
	}

	// internal helpers

	private void postToWall(Connection<Facebook> connection, Account account, WebRequest request) {
		// ユーザーのウォールにリンクを投稿します。
		if (request.getAttribute(POST_TO_WALL_ATTRIBUTE, RequestAttributes.SCOPE_SESSION) != null) {
			connection.getApi().feedOperations().postLink("Join me at the Greenhouse!", new FacebookLink(
					account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.",
					"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
			request.removeAttribute(POST_TO_WALL_ATTRIBUTE, RequestAttributes.SCOPE_SESSION);
		}
	}

	private void useFacebookProfileImage(Connection<Facebook> connection, Account account, WebRequest request) {
		// Facebookプロフィール画像をローカルのプロフィール画像として保存します。
		if (request.getAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, RequestAttributes.SCOPE_SESSION) != null) {
			try {
				profilePictureService.saveProfilePicture(account.getId(),
						connection.getApi().userOperations().getUserProfileImage());
			} catch (IOException e) {
				NativeWebRequest nativeRequest = (NativeWebRequest) request;
				HttpServletRequest servletRequest = nativeRequest.getNativeRequest(HttpServletRequest.class);
				FlashMap flashMap = RequestContextUtils.getOutputFlashMap(servletRequest);
				flashMap.put("message", Message.warning("Greenhouse was unable to use your Facebook profile picture."));
			}
			request.removeAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, RequestAttributes.SCOPE_SESSION);
		}
	}

	private static final String POST_TO_WALL_PARAMETER = "postToWall";

	private static final String POST_TO_WALL_ATTRIBUTE = "facebookConnect." + POST_TO_WALL_PARAMETER;

	private static final String USE_FACEBOOK_IMAGE_PARAMETER = "useProfilePicture";

	private static final String USE_FACEBOOK_IMAGE_ATTRIBUTE = "facebookConnect." + USE_FACEBOOK_IMAGE_PARAMETER;

}
