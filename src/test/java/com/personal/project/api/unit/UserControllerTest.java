package com.personal.project.api.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.api.controller.UserController;
import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.controller.dto.user.RequestUserRegisterDTO;
import com.personal.project.api.controller.dto.user.ResponseUserLoginDTO;
import com.personal.project.api.controller.dto.user.ResponseUserRegisterDTO;
import com.personal.project.api.service.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.List;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Method under test: {@link UserController#registerUser(RequestUserRegisterDTO)}
     */

    @Test
    @DisplayName("Should register a user")
    void testRegisterUser() throws Exception {
        RequestUserRegisterDTO userDTO = UTestData.createValidUserRegisterReq();
        ResponseUserRegisterDTO user = UTestData.createValidUserRegisterResp();

        when(userService.registerUser(userDTO)).thenReturn(user);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(UTestData.ROUTE_USER_AUTH)
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Your registration was successful")));
                //.andDo(MockMvcResultHandlers.print());
    }

    /**
     * Method under test: {@link UserController#registerUser(RequestUserRegisterDTO)}
     */

    @Test
    @DisplayName("Should return bad request when creating an invalid user")
    void testRegisterInvalid() throws Exception {
        List<RequestUserRegisterDTO> userDTOList = UTestData.createInvalidUserRegisterReq();

        for (RequestUserRegisterDTO userDTO : userDTOList) {

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(UTestData.ROUTE_USER_AUTH)
                    .content(objectMapper.writeValueAsString(userDTO))
                    .contentType(MediaType.APPLICATION_JSON);
            MockMvcBuilders.standaloneSetup(userController)
                    .build()
                    .perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

    }

    /**
     * Method under test: {@link UserService#loginUser(RequestUserLoginDTO)}
     */

    @Test
    @DisplayName("Should login a user")
    void testLoginUser() throws Exception {
        RequestUserLoginDTO userLogin = UTestData.createValidUserLoginReq();

        when(userService.loginUser(userLogin)).thenReturn(new ResponseUserLoginDTO("token"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(UTestData.ROUTE_USER_LOGIN)
                .content(objectMapper.writeValueAsString(userLogin))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    /**
     * Method under test: {@link UserService#loginUser(RequestUserLoginDTO)}
     */

    @Test
    @DisplayName("Should throw an exception when login is no valid")
    void testLoginInvalidLogin() throws Exception {
        RequestUserLoginDTO invalidLogin = UTestData.createInvalidUserLoginReq();

        when(userService.loginUser(any())).thenThrow(new InternalAuthenticationServiceException(null));

        ServletException servletException = assertThrows(ServletException.class, () -> {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(UTestData.ROUTE_USER_LOGIN)
                    .content(objectMapper.writeValueAsString(invalidLogin))
                    .contentType(MediaType.APPLICATION_JSON);
            ResultActions resultActions = MockMvcBuilders.standaloneSetup(userController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andDo(MockMvcResultHandlers.print());
            resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        });

        String message = servletException.getMessage();

        assertThat(message).contains("InternalAuthenticationServiceException");

    }

    /**
     * Method under test: {@link UserService#loginUser(RequestUserLoginDTO)}
     */

    @Test
    @DisplayName("Should throw an exception when password is no valid")
    void testLoginInvalidPassword() throws Exception {
        RequestUserLoginDTO invalidPassword = UTestData.createInvalidUserPasswordReq();

        when(userService.loginUser(any())).thenThrow(new BadCredentialsException(null));

        ServletException servletException = assertThrows(ServletException.class, () -> {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(UTestData.ROUTE_USER_LOGIN)
                    .content(objectMapper.writeValueAsString(invalidPassword))
                    .contentType(MediaType.APPLICATION_JSON);
            ResultActions resultActions = MockMvcBuilders.standaloneSetup(userController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andDo(MockMvcResultHandlers.print());
            resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
        });

        String message = servletException.getMessage();

        assertThat(message).contains("BadCredentialsException");

    }


}
