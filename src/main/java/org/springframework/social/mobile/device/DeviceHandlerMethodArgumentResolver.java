/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.social.mobile.device;

import org.springframework.core.MethodParameter;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// クラスの説明: このクラスは、Spring MVCのHandlerMethodArgumentResolverインターフェースを実装し、デバイス情報を引数として解決します。
public class DeviceHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	// メソッドの説明: supportsParameterメソッドは、引数がDeviceクラスに割り当て可能かどうかを確認します。
	public boolean supportsParameter(MethodParameter parameter) {
		return Device.class.isAssignableFrom(parameter.getParameterType());
	}

	// メソッドの説明: resolveArgumentメソッドは、現在のリクエストからデバイス情報を取得して返します。
	// 特定のロジックの説明: DeviceUtils.getCurrentDeviceメソッドを使用して、リクエストからデバイス情報を取得します。
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		return DeviceUtils.getCurrentDevice(request);
	}

}