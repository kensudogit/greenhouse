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
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Greenhouseのキー・バリュー・ストアの設定。
 * キー・バリュー・ストアは、Greenhouseアプリケーション内のデータを効率的にキャッシュします。
 * Redisキー・バリュー・ストアを使用します。
 * Jedisクライアントと共に{@link StringRedisTemplate}を使用してキャッシュされたデータにアクセスします。
 * "standard"モードでのみ有効です。組み込みシナリオでは、キャッシュしません。
 * 
 * @author Keith Donald
 */
@Configuration
@Profile("standard")
public class KeyValueConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(Environment environment) {
		// Redisのホスト名を設定します
		JedisConnectionFactory redis = new JedisConnectionFactory();
		redis.setHostName(environment.getProperty("redis.hostName"));
		// Redisのポートを設定します
		redis.setPort(environment.getProperty("redis.port", Integer.class));
		// Redisのパスワードを設定します
		redis.setPassword(environment.getProperty("redis.password"));
		return redis;
	}

	@Bean
	public StringRedisTemplate redisTemplate(Environment environment) {
		// RedisConnectionFactoryを使用してStringRedisTemplateを返します
		return new StringRedisTemplate(redisConnectionFactory(environment));
	}

}
