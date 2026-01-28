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
package com.springsource.greenhouse.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * このクラスは@Confirmフィールドを検証します。
 *
 * @author Keith Donald
 */
public final class ConfirmValidator implements ConstraintValidator<Confirm, Object> {

	private String field;

	private String matches;

	private String message;

	/**
	 * Confirmアノテーションを初期化します。
	 *
	 * @param constraintAnnotation Confirmアノテーション
	 */
	public void initialize(Confirm constraintAnnotation) {
		field = constraintAnnotation.field();
		matches = constraintAnnotation.matches();
		if (matches == null || matches.isEmpty()) {
			// matchesがnullまたは空の場合、フィールド名を元に"confirm"を付けた名前を設定します。
			matches = "confirm" + StringUtils.capitalize(field);
		}
		message = constraintAnnotation.message();
	}

	/**
	 * フィールドの値が一致するかどうかを検証します。
	 *
	 * @param value   検証対象のオブジェクト
	 * @param context 検証コンテキスト
	 * @return フィールドの値が一致する場合はtrue、それ以外はfalse
	 */
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object fieldValue = beanWrapper.getPropertyValue(field);
		Object matchesValue = beanWrapper.getPropertyValue(matches);
		boolean matched = ObjectUtils.nullSafeEquals(fieldValue, matchesValue);
		if (matched) {
			// フィールドの値が一致する場合
			return true;
		} else {
			// フィールドの値が一致しない場合、デフォルトの制約違反メッセージを無効にし、カスタムメッセージを設定します。
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addNode(field).addConstraintViolation();
			return false;
		}
	}

}
