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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * クラスレベルの制約で、フィールドの値が他のフィールドの値と一致することを確認します。
 * 
 * @author Keith Donald
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ConfirmValidator.class)
@Documented
public @interface Confirm {

	/**
	 * 制約違反時のデフォルトメッセージ。
	 */
	String message() default "{com.springsource.greenhouse.validation.constraints.Confirm.message}";

	/**
	 * 制約のグループを指定します。
	 */
	Class<?>[] groups() default {};

	/**
	 * 制約のペイロードを指定します。
	 */
	Class<? extends Payload>[] payload() default {};

	/**
	 * 確認するフィールドの名前。例: password
	 */
	String field();

	/**
	 * {@link #field()} の値と一致する必要があるフィールドの名前。
	 * オプション。指定しない場合、デフォルトで "confirm${field}" になります。例: confirmPassword。
	 */
	String matches() default "";

	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		/**
		 * クラスごとに複数の確認フィールドを指定するために使用されます。
		 */
		Confirm[] value();

	}
}
