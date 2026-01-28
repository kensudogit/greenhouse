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
package com.springsource.greenhouse.develop.oauth;

import java.util.Collections;

import javax.inject.Inject;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.develop.App;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;
import com.springsource.greenhouse.develop.AppConfigRepository;

/**
 * AppClientDetailsServiceクラスは、AppRepositoryから返されるAppレコードをSpring Security
 * OAuth2のClientDetailsに適応させます。
 * これにより、AppRepositoryがSpring Security
 * OAuth2プロバイダーに知られているOAuth2クライアントのソースとして機能できるようになります。
 * 
 * @author Craig Walls
 */
@Service("clientDetails")
public class AppClientDetailsService implements ClientDetailsService {

	private final AppRepository appRepository;
	private final AppConfigRepository configRepository;

	@Inject
	public AppClientDetailsService(AppRepository appRepository, AppConfigRepository configRepository) {
		this.appRepository = appRepository;
		this.configRepository = configRepository;
	}

	@Override
	public ClientDetails loadClientByClientId(String appId) throws OAuth2Exception {
		try {
			return clientDetailsFor(appRepository.findAppByApiKey(appId));
		} catch (InvalidApiKeyException e) {
			throw new OAuth2Exception("Invalid OAuth App ID " + appId, e);
		}
	}

	private ClientDetails clientDetailsFor(App app) {
		return new AppClientDetails(app, configRepository);
	}

	@SuppressWarnings("serial")
	private static class AppClientDetails extends BaseClientDetails {

		public AppClientDetails(App app, AppConfigRepository configRepository) {
			super();
			setClientId(app.getApiKey());
			setClientSecret(app.getSecret());
			setResourceIds(configRepository.getResourceIds());
			setScope(configRepository.getScopes());
			setAuthorizedGrantTypes(configRepository.getAuthorizedGrantTypes());
			setAuthorities(configRepository.getAuthorities());
			setRegisteredRedirectUri(Collections.singleton(app.getCallbackUrl()));
		}

	}

}