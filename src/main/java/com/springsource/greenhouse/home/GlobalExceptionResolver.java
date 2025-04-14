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
package com.springsource.greenhouse.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * アプリケーションコントローラーレベルで処理されない例外を処理します。
 * アプリケーションのグローバル例外処理ポリシーを適用します。
 * AccountNotConnectedException は 412 を引き起こします。
 * EmptyResultDataAccessException は 404 を引き起こします。
 * 最初のフォールバックとして {@link ResponseStatusExceptionResolver} に委譲します。
 * 最終フォールバックとして {@link DefaultHandlerExceptionResolver} に委譲します。
 *
 * @author Keith Donald
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	private final ResponseStatusExceptionResolver annotatedExceptionHandler = new ResponseStatusExceptionResolver();

	private final DefaultHandlerExceptionResolver defaultExceptionHandler = new DefaultHandlerExceptionResolver();

	/**
	 * 例外を解決します。
	 *
	 * @param request  HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param handler  ハンドラーオブジェクト
	 * @param ex       発生した例外
	 * @return モデルとビュー
	 */
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
			if (ex instanceof EmptyResultDataAccessException) {
				return handleEmptyResultDataAccessException((EmptyResultDataAccessException) ex, request, response,
						handler);
			}
		} catch (Exception e) {
			logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", ex);
			return null;
		}
		ModelAndView result = annotatedExceptionHandler.resolveException(request, response, handler, ex);
		if (result != null) {
			return result;
		}
		return defaultExceptionHandler.resolveException(request, response, handler, ex);
	}

	// internal helpers

	/**
	 * EmptyResultDataAccessException を処理します。
	 * 
	 * @param ex       発生した例外
	 * @param request  HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param handler  ハンドラーオブジェクト
	 * @return モデルとビュー
	 * @throws Exception 処理中に発生した例外
	 */
	private ModelAndView handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return new ModelAndView();
	}

}