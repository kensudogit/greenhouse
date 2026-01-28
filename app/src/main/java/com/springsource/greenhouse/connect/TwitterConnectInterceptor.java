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
package com.springsource.greenhouse.connect;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.springsource.greenhouse.account.AccountUtils;

/**
 * Supports posting a tweet on behalf of the user after connecting to Twitter.
 * 
 * @author Keith Donald
 */
public class TwitterConnectInterceptor implements ConnectInterceptor<Twitter> {

	/**
	 * Performs actions before connecting to Twitter.
	 * Checks if user wants to post a tweet and stores the preference in session.
	 */
	@Override
	public void preConnect(ConnectionFactory<Twitter> connectionFactory, MultiValueMap<String, String> params,
			WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TWEET_PARAMETER))) {
			request.setAttribute(POST_TWEET_ATTRIBUTE, Boolean.TRUE, RequestAttributes.SCOPE_SESSION);
		}
	}

	/**
	 * Performs actions after connecting to Twitter.
	 * Posts a tweet if the user requested it during pre-connect.
	 */
	@Override
	public void postConnect(Connection<Twitter> connection, WebRequest request) {
		if (request.getAttribute(POST_TWEET_ATTRIBUTE, RequestAttributes.SCOPE_SESSION) != null) {
			connection.getApi().timelineOperations()
					.updateStatus("Join me at the Greenhouse! " + AccountUtils.getCurrentAccount().getProfileUrl());
			request.removeAttribute(POST_TWEET_ATTRIBUTE, RequestAttributes.SCOPE_SESSION);
		}
	}

	private static final String POST_TWEET_PARAMETER = "postTweet";

	private static final String POST_TWEET_ATTRIBUTE = "twitterConnect." + POST_TWEET_PARAMETER;

}