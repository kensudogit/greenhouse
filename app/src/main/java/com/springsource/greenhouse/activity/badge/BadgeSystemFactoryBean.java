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

import org.springframework.beans.factory.FactoryBean;

/**
 * Factory that configures a badge system and returns it as a Bean called by the integration-activity.xml pipeline.
 * Registers various badge awarding rules.
 * 
 * @author Keith Donald
 */
public class BadgeSystemFactoryBean implements FactoryBean<BadgeSystem> {

	private final BadgeRepository badgeRepository;

	@Inject
	public BadgeSystemFactoryBean(BadgeRepository badgeRepository) {
		this.badgeRepository = badgeRepository;
	}

	@Override
	public Class<?> getObjectType() {
		return BadgeSystem.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public BadgeSystem getObject() throws Exception {
		StandardBadgeSystem badgeSystem = new StandardBadgeSystem();
		badgeSystem.add(new SimpleBadgeAwarder("Newbie", badgeRepository), "SignedUp");
		return badgeSystem;
	}

}