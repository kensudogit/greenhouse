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
 * 標準的なBadgeSystemの実装。
 * 
 * @author Keith Donald
 */
class StandardBadgeSystem implements BadgeSystem {

	private final Map<Class<? extends Action>, BadgeAwarder<? extends Action>> badgeAwarders = new HashMap<Class<? extends Action>, BadgeAwarder<? extends Action>>();

	private final Map<String, SimpleBadgeAwarder> simpleActionBadgeAwarders = new HashMap<String, SimpleBadgeAwarder>();

	/**
	 * BadgeAwarderを追加します。
	 * 
	 * @param badgeAwarder バッジを授与するためのBadgeAwarder
	 */
	public void add(BadgeAwarder<? extends Action> badgeAwarder) {
		Class<? extends Action> actionType = (Class<? extends Action>) GenericTypeResolver
				.resolveTypeArgument(badgeAwarder.getClass(), BadgeAwarder.class);
		badgeAwarders.put(actionType, badgeAwarder);
	}

	/**
	 * SimpleBadgeAwarderを追加します。
	 * 
	 * @param badgeAwarder     バッジを授与するためのSimpleBadgeAwarder
	 * @param simpleActionType シンプルアクションのタイプ
	 */
	public void add(SimpleBadgeAwarder badgeAwarder, String simpleActionType) {
		simpleActionBadgeAwarders.put(simpleActionType, badgeAwarder);
	}

	/**
	 * BadgeAwarderとMatcherを追加します。
	 * 
	 * @param badgeAwarder バッジを授与するためのBadgeAwarder
	 * @param matcher      アクションをマッチさせるためのMatcher
	 */
	public void add(BadgeAwarder<? extends Action> badgeAwarder, BadgeAwarderMatcher matcher) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * アクションに対してバッジを授与します。
	 * 
	 * @param action バッジを授与するアクション
	 * @return 授与されたバッジ
	 */
	public AwardedBadge awardBadgeForAction(Action action) {
		if (action instanceof SimpleAction) {
			String simpleActionType = ((SimpleAction) action).getType();
			return invokeBadgeAwarder(simpleActionBadgeAwarders.get(simpleActionType), action);
		} else {
			return invokeBadgeAwarder(badgeAwarders.get(action.getClass()), action);
		}
	}

	/**
	 * BadgeAwarderを呼び出します。
	 * 
	 * @param awarder バッジを授与するためのBadgeAwarder
	 * @param action  バッジを授与するアクション
	 * @return 授与されたバッジ
	 */
	private AwardedBadge invokeBadgeAwarder(BadgeAwarder awarder, Action action) {
		if (awarder == null) {
			return null;
		}
		return awarder.awardBadgeForAction(action);
	}

	/**
	 * アクションが一致するかどうかを判定します。
	 */
	public interface BadgeAwarderMatcher {
		boolean matches(Action action);
	}

}