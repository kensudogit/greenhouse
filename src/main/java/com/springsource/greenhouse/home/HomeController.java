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

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the homepage of the application.
 *
 * @author Keith Donald
 */
@Controller
public class HomeController {

	/**
	 * Renders the home page as HTML in thw web browser.
	 * The home page is different based on whether the user is signed in or not.
	 */
	@GetMapping("/")
	public String home(Principal user) {
		return user != null ? "homeSignedIn" : "homeNotSignedIn";
	}
}
