/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springsource.greenhouse.account.GreenhousePasswordEncoder;

/**
 * Spring Security configuration.
 * Applies security policies to protect the Greenhouse web application.
 * <p>
 * In embedded mode, for developer convenience and ease of testing,
 * password encoding and data encryption are not performed.
 * Temporary OAuth sessions are stored in an in-memory ConcurrentHashMap.
 * </p>
 * <p>
 * In standard mode, standard password encoding (SHA-256 with 1024 iterations + random salt)
 * and encryption (password-based 256-bit AES + site-wide salt + secure random 16-byte IV processing) are applied.
 * Temporary OAuth sessions are stored in a Redis key-value store.
 * </p>
 * <p>
 * Since Spring Security is currently best configured using XML namespaces,
 * this class imports an XML file containing most of the configuration information.
 * PasswordEncoder, TextEncryptor, and OAuthSessionManager are configured in Java.
 * </p>
 * 
 * @author Keith Donald
 */
@Configuration
@ImportResource("classpath:com/springsource/greenhouse/config/security.xml")
public class SecurityConfig {

	// Private constructor to prevent instantiation
	private SecurityConfig() {
		// This constructor is intentionally empty. Nothing special is needed here.
	}

	/**
	 * Embedded security configuration (not secure).
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.noOpText();
		}

	}

	/**
	 * Standard security configuration (secure).
	 * 
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		private final Environment environment;

		public Standard(Environment environment) {
			this.environment = environment;
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new GreenhousePasswordEncoder(getEncryptPassword());
		}

		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.queryableText(getEncryptPassword(), environment.getProperty("security.encryptSalt"));
		}

		// helpers

		private String getEncryptPassword() {
			return environment.getProperty("security.encryptPassword");
		}

	}

}