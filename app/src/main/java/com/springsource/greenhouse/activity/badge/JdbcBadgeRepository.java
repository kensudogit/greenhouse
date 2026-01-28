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
package com.springsource.greenhouse.activity.badge;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import com.springsource.greenhouse.account.Account;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import com.springsource.greenhouse.activity.action.Action;

/**
 * BadgeRepository implementation that stores awarded badge records in a relational database using JDBC API.
 * 
 * @author Keith Donald
 */
public class JdbcBadgeRepository implements BadgeRepository {

	private final JdbcTemplate jdbcTemplate;

	@Value("${badge.image.url:http://images.greenhouse.springsource.org/activity/icon-default-badge.png}")
	private String imageUrl;

	/**
	 * Constructs a JdbcBadgeRepository.
	 * 
	 * @param jdbcTemplate JdbcTemplate used for database operations
	 */
	@Inject
	public JdbcBadgeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Creates a new awarded badge.
	 * 
	 * @param badge   badge name
	 * @param account account information
	 * @param action  action information
	 * @return created AwardedBadge object
	 */
	@Transactional
	@Override
	public AwardedBadge createAwardedBadge(String badge, Account account, Action action) {
		DateTime awardTime = new DateTime(DateTimeZone.UTC);
		jdbcTemplate.update("insert into AwardedBadge (badge, awardTime, member, memberAction) values (?, ?, ?, ?)",
				badge, awardTime.toDate(), account.getId(), action.getId());
		Long id = jdbcTemplate.queryForObject("call identity()", Long.class);
		return new AwardedBadge(id, badge, awardTime, imageUrl, account, action);
	}

}