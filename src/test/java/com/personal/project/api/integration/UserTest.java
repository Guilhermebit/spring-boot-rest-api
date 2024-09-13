package com.personal.project.api.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.controller.dto.user.RequestUserRegisterDTO;
import com.personal.project.api.entity.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc//(addFilters = false)
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoad() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
    }

    @Test
    @DisplayName("Register User")
    void testRegisterUser() throws Exception {
        RequestUserRegisterDTO userRegister = new RequestUserRegisterDTO(ITestData.LOGIN_USER, ITestData.PASSWORD, UserRole.ADMIN);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ITestData.ROUTE_USER_AUTH)
                        .content(objectMapper.writeValueAsString(userRegister))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(ITestData.ENCODING))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Login User")
    void testLoginUser() throws Exception {
        RequestUserLoginDTO userLogin = new RequestUserLoginDTO(ITestData.LOGIN_ADMIN, ITestData.PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(ITestData.ROUTE_USER_LOGIN)
                        .content(objectMapper.writeValueAsString(userLogin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(ITestData.ENCODING))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


}
