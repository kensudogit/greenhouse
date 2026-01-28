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

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springsource.greenhouse.utils.Location;

/**
 * クライアントから送信されたクエリパラメータから、ユーザーの地理的位置を解決します。
 * 
 * @author Keith Donald
 */
public class UserLocationHandlerInterceptor implements HandlerInterceptor {

	public static final String USER_LOCATION_ATTRIBUTE = "userLocation";

	/**
	 * リクエストから緯度と経度を取得し、ユーザーの位置情報を設定します。
	 *
	 * @param request  HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param handler  ハンドラーオブジェクト
	 * @return 常にtrueを返します。
	 * @throws Exception 例外が発生した場合
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Double latitude = ServletRequestUtils.getDoubleParameter(request, "latitude");
		Double longitude = ServletRequestUtils.getDoubleParameter(request, "longitude");
		// 緯度と経度が存在する場合、ユーザーの位置情報をリクエスト属性に設定します。
		if (latitude != null && longitude != null) {
			request.setAttribute(USER_LOCATION_ATTRIBUTE, new Location(latitude, longitude));
		}
		return true;
	}

	/**
	 * リクエスト処理後に呼び出されるメソッドです。
	 * 
	 * @param request      HTTPリクエスト
	 * @param response     HTTPレスポンス
	 * @param handler      ハンドラーオブジェクト
	 * @param modelAndView モデルとビュー
	 * @throws Exception 例外が発生した場合
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// このメソッドは現在未実装です。必要に応じてオーバーライドしてください。
		throw new UnsupportedOperationException("This method is not yet implemented.");
	}

	/**
	 * リクエスト処理が完了した後に呼び出されるメソッドです。
	 *
	 * @param request  HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param handler  ハンドラーオブジェクト
	 * @param ex       例外オブジェクト
	 * @throws Exception 例外が発生した場合
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// このメソッドは現在未実装です。必要に応じてオーバーライドしてください。
		throw new UnsupportedOperationException("This method is not yet implemented.");
	}

}
