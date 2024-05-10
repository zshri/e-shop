package com.example.eshop.controller;

import com.example.eshop.model.Role;
import com.example.eshop.model.User;
import com.example.eshop.model.exception.UserAlreadyExistsException;
import com.example.eshop.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User newUser;
    private User saveUser;

    String username = "user1";
    String email = "mail@example.com";
    String password = "123456";

    @BeforeEach
    public void setup() {
        newUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        saveUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();
    }

    @Test
    public void testRegistrationUser_withValidUser_redirectsToRoot() throws Exception {
        when(userService.registrationUser(any(User.class))).thenReturn(saveUser);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .with(request -> {
                            request.setAttribute("newUser", newUser);
                            return request;
                }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testRegistrationUser_withValidExceptionName() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/registration")
                .param("username", "usr")
                .param("email", newUser.getEmail())
                .param("password", "12345")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Username Valid Error")));
    }
    @Test
    public void testRegistrationUser_withValidExceptionEmail() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/registration")
                .param("username", newUser.getUsername())
                .param("email", "email.com")
                .param("password", "12345")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Email Valid Error")));
    }
    @SneakyThrows
    @Test
    public void testRegistrationUser_withValidExceptionPassword()  {
        MockHttpServletRequestBuilder requestBuilder = post("/registration")
                .param("username", newUser.getUsername())
                .param("email", newUser.getEmail())
                .param("password", "22")
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Password Valid Error")));
    }

    @SneakyThrows
    @Test
    public void testRegistrationUser_withUserAlreadyExistsException() {
        doThrow(new UserAlreadyExistsException("")).when(userService).registrationUser(any(User.class));

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .with(request -> {
                            request.setAttribute("newUser", newUser);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User Exist Error")));
    }
    @SneakyThrows
    @Test
    public void testRegistrationUser_withException() {
        doThrow(new Exception()).when(userService).registrationUser(any(User.class));

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .with(request -> {
                            request.setAttribute("newUser", newUser);
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Error")));
    }
}