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

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.GenericTypeResolver;

import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.activity.action.SimpleAction;

/**
 * Standard BadgeSystem implementation.
 * 
 * @author Keith Donald
 */
class StandardBadgeSystem implements BadgeSystem {

	private final Map<Class<? extends Action>, BadgeAwarder<? extends Action>> badgeAwarders = new HashMap<Class<? extends Action>, BadgeAwarder<? extends Action>>();

	private final Map<String, SimpleBadgeAwarder> simpleActionBadgeAwarders = new HashMap<String, SimpleBadgeAwarder>();

	/**
	 * Adds a BadgeAwarder.
	 * 
	 * @param badgeAwarder BadgeAwarder to award badges
	 */
	public void add(BadgeAwarder<? extends Action> badgeAwarder) {
		Class<? extends Action> actionType = (Class<? extends Action>) GenericTypeResolver
				.resolveTypeArgument(badgeAwarder.getClass(), BadgeAwarder.class);
		badgeAwarders.put(actionType, badgeAwarder);
	}

	/**
	 * Adds a SimpleBadgeAwarder.
	 * 
	 * @param badgeAwarder     SimpleBadgeAwarder to award badges
	 * @param simpleActionType simple action type
	 */
	public void add(SimpleBadgeAwarder badgeAwarder, String simpleActionType) {
		simpleActionBadgeAwarders.put(simpleActionType, badgeAwarder);
	}

	/**
	 * Adds a BadgeAwarder with a Matcher.
	 * 
	 * @param badgeAwarder BadgeAwarder to award badges
	 * @param matcher      Matcher to match actions
	 */
	public void add(BadgeAwarder<? extends Action> badgeAwarder, BadgeAwarderMatcher matcher) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Awards a badge for an action.
	 * 
	 * @param action action to award badge for
	 * @return awarded badge
	 */
	@Override
	public AwardedBadge awardBadgeForAction(Action action) {
		if (action instanceof SimpleAction) {
			String simpleActionType = ((SimpleAction) action).getType();
			return invokeBadgeAwarder(simpleActionBadgeAwarders.get(simpleActionType), action);
		} else {
			return invokeBadgeAwarder(badgeAwarders.get(action.getClass()), action);
		}
	}

	/**
	 * Invokes a BadgeAwarder.
	 * 
	 * @param awarder BadgeAwarder to award badges
	 * @param action  action to award badge for
	 * @return awarded badge
	 */
	private AwardedBadge invokeBadgeAwarder(BadgeAwarder awarder, Action action) {
		if (awarder == null) {
			return null;
		}
		return awarder.awardBadgeForAction(action);
	}

	/**
	 * Determines if an action matches certain criteria.
	 */
	public interface BadgeAwarderMatcher {
		/**
		 * Checks if an action matches.
		 * 
		 * @param action action to check
		 * @return true if action matches
		 */
		boolean matches(Action action);
	}

}