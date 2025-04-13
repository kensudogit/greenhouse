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

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 非同期タスク実行の設定。
 * コンパイル時のAspectJアドバイスを適用して、{@link Async} メソッドを別スレッドで実行できるようにします。
 * 非同期タスク実行を行うExecutorは、{@link ThreadPoolTaskExecutor}です。
 * これは、{@link Async} メソッドの実行や、Spring Integrationによって開始される非同期作業の処理に使用されます。
 * 
 * @author Keith Donald
 */
@Configuration
@EnableAsync(mode = AdviceMode.ASPECTJ)
public class TaskConfig implements AsyncConfigurer {

	// implementing AsyncConfigurer
	public Executor getAsyncExecutor() {
		return taskExecutor();
	}

	/**
	 * The asynchronous task executor used by the Greenhouse application.
	 */
	@Bean
	public Executor taskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
