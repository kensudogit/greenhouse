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
package com.springsource.greenhouse.signin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link SigninController}.
 * 
 * @author Test Author
 */
public class SigninControllerTest {

    private MockMvc mockMvc;
    private SigninController signinController;

    @Before
    public void setUp() {
        signinController = new SigninController();

        // Set up MockMvc with view resolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(signinController)
                .setViewResolvers(viewResolver)
                .build();
    }

    // ==================== Controller Instance Tests ====================

    @Test
    public void testSigninController_ShouldBeInstantiated() {
        // When
        SigninController controller = new SigninController();

        // Then
        assertNotNull("SigninController should not be null", controller);
    }

    @Test
    public void testSigninController_ShouldHaveCorrectAnnotation() {
        // When
        SigninController controller = new SigninController();

        // Then
        assertTrue("SigninController should have @Controller annotation",
                controller.getClass().isAnnotationPresent(org.springframework.stereotype.Controller.class));
    }

    // ==================== Signin Method Tests ====================

    @Test
    public void testSignin_ShouldBeAccessible() throws Exception {
        // When & Then
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignin_ShouldReturnCorrectViewName() throws Exception {
        // When & Then
        mockMvc.perform(get("/signin"))
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldHandleGetRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldNotHandlePostRequest() throws Exception {
        // When & Then - POST should not be handled by this controller
        mockMvc.perform(post("/signin"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testSignin_ShouldNotHandlePutRequest() throws Exception {
        // When & Then - PUT should not be handled by this controller
        mockMvc.perform(put("/signin"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testSignin_ShouldNotHandleDeleteRequest() throws Exception {
        // When & Then - DELETE should not be handled by this controller
        mockMvc.perform(delete("/signin"))
                .andExpect(status().isMethodNotAllowed());
    }

    // ==================== URL Mapping Tests ====================

    @Test
    public void testSignin_ShouldMapToCorrectUrl() throws Exception {
        // When & Then
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignin_ShouldNotMapToIncorrectUrl() throws Exception {
        // When & Then - Different URL should not be handled
        mockMvc.perform(get("/signin-form"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSignin_ShouldNotMapToRootUrl() throws Exception {
        // When & Then - Root URL should not be handled
        mockMvc.perform(get("/"))
                .andExpect(status().isNotFound());
    }

    // ==================== Method Behavior Tests ====================

    @Test
    public void testSignin_ShouldReturnVoid() throws Exception {
        // Given
        SigninController controller = new SigninController();

        // When
        Object result = controller.signin();

        // Then
        assertNull("Signin method should return void (null)", result);
    }

    @Test
    public void testSignin_ShouldNotThrowException() {
        // Given
        SigninController controller = new SigninController();

        // When & Then - Should not throw any exception
        try {
            controller.signin();
        } catch (Exception e) {
            fail("Signin method should not throw exception: " + e.getMessage());
        }
    }

    // ==================== Request Parameter Tests ====================

    @Test
    public void testSignin_ShouldHandleRequestWithoutParameters() throws Exception {
        // When & Then
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldHandleRequestWithParameters() throws Exception {
        // When & Then - Should handle request even with extra parameters
        mockMvc.perform(get("/signin")
                .param("redirect", "/dashboard")
                .param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    // ==================== HTTP Header Tests ====================

    @Test
    public void testSignin_ShouldHandleRequestWithHeaders() throws Exception {
        // When & Then - Should handle request with various headers
        mockMvc.perform(get("/signin")
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldHandleRequestWithContentType() throws Exception {
        // When & Then - Should handle request with content type header
        mockMvc.perform(get("/signin")
                .contentType("text/html"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    // ==================== Edge Cases Tests ====================

    @Test
    public void testSignin_ShouldHandleRequestWithSpecialCharacters() throws Exception {
        // When & Then - Should handle request with special characters in parameters
        mockMvc.perform(get("/signin")
                .param("redirect", "/dashboard?param=value&another=param"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldHandleRequestWithUnicodeCharacters() throws Exception {
        // When & Then - Should handle request with unicode characters
        mockMvc.perform(get("/signin")
                .param("message", "こんにちは世界"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    // ==================== Multiple Requests Tests ====================

    @Test
    public void testSignin_ShouldHandleMultipleRequests() throws Exception {
        // When & Then - Should handle multiple requests to the same endpoint
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));

        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));

        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    // ==================== Controller Lifecycle Tests ====================

    @Test
    public void testSigninController_ShouldBeReusable() throws Exception {
        // Given
        SigninController controller = new SigninController();

        // When & Then - Should be able to call signin method multiple times
        controller.signin();
        controller.signin();
        controller.signin();

        // Should not throw any exception
        assertNotNull("Controller should still be valid", controller);
    }

    // ==================== Annotation Tests ====================

    @Test
    public void testSigninMethod_ShouldHaveGetMappingAnnotation() throws Exception {
        // Given
        SigninController controller = new SigninController();

        // When
        java.lang.reflect.Method signinMethod = controller.getClass().getMethod("signin");

        // Then
        assertTrue("Signin method should have @GetMapping annotation",
                signinMethod.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));
    }

    @Test
    public void testGetMappingAnnotation_ShouldHaveCorrectValue() throws Exception {
        // Given
        SigninController controller = new SigninController();

        // When
        java.lang.reflect.Method signinMethod = controller.getClass().getMethod("signin");
        org.springframework.web.bind.annotation.GetMapping mapping = signinMethod
                .getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);

        // Then
        assertNotNull("GetMapping annotation should exist", mapping);
        assertArrayEquals("GetMapping should have correct value", new String[] { "/signin" }, mapping.value());
    }

    // ==================== Integration Tests ====================

    @Test
    public void testSignin_ShouldWorkWithSpringContext() throws Exception {
        // When & Then - Should work in a Spring context
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void testSignin_ShouldReturnConsistentResponse() throws Exception {
        // When & Then - Should return consistent response for same request
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));

        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }
}