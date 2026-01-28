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
package com.springsource.greenhouse.develop;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

/**
 * Repository for OAuth2 app configuration.
 * Provides default OAuth2 configuration values for apps.
 */
@Repository
public class AppConfigRepository {

	/**
	 * Returns the resource IDs that apps can access.
	 */
	public Collection<String> getResourceIds() {
		return Collections.singletonList("greenhouse");
	}

	/**
	 * Returns the OAuth2 scopes that apps can request.
	 */
	public Collection<String> getScopes() {
		return Arrays.asList("read", "write");
	}

	/**
	 * Returns the authorized grant types for apps.
	 */
	public Collection<String> getAuthorizedGrantTypes() {
		return Arrays.asList("authorization_code", "refresh_token");
	}

	/**
	 * Returns the authorities granted to apps.
	 */
	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
	}
}
