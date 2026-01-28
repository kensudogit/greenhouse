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
package com.springsource.greenhouse.signin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.springsource.greenhouse.account.Account;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVCインターセプター。このクラスは、現在サインインしているメンバーを識別するアカウントをリクエスト属性として公開します。
 * コントローラーやビューから現在のメンバーアカウントへの参照を取得することをサポートします。
 * 
 * @author Keith Donald
 */
public class AccountExposingHandlerInterceptor implements HandlerInterceptor {

	// preHandleメソッドは、リクエスト処理の前に呼び出されます。
	// 認証情報が存在し、かつそのプリンシパルがAccountのインスタンスである場合、
	// リクエスト属性として"account"を設定します。
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof Account) {
			request.setAttribute("account", auth.getPrincipal());
		}
		return true;
	}

	// postHandleメソッドは、リクエスト処理の後に呼び出されますが、
	// ここでは特に後処理は必要ないため、空のままにしています。
	// (このメソッドは将来の拡張のために用意されています)
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	// afterCompletionメソッドは、リクエストの完了後に呼び出されますが、
	// ここでは特に後処理は必要ないため、空のままにしています。
	// (このメソッドは将来の拡張のために用意されています)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
