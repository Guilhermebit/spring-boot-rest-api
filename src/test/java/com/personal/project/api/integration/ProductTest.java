package com.personal.project.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.api.config.JWTUtil;
import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.entity.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.personal.project.api.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
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
class ProductTest {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private String token;

	@BeforeEach
	void setUp() {
		token = jwtUtil.generateToken(new User(ITestData.LOGIN_ADMIN, ITestData.PASSWORD, UserRole.ADMIN));
	}

	@Test
	@DisplayName("Find products by an range")
	void testFindProductBetweenPrice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get(ITestData.ROUTE_PRODUCT_VALUES, 10000,20000)
						.header("Authorization","Bearer " + token))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("Find a product by id")
	void findUniqueProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get(ITestData.ROUTE_PRODUCT_ID, ITestData.ID)
						.header("Authorization","Bearer " + token))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("List all products")
	void testAllProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get(ITestData.ROUTE_PRODUCT)
						.header("Authorization","Bearer " + token))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("Create product")
	void testCreateProduct() throws Exception {
		RequestProductDTO requestProduct = new RequestProductDTO(ITestData.PRODUCT_NAME_CREATE, 500000);
		mockMvc.perform(MockMvcRequestBuilders
						.post(ITestData.ROUTE_PRODUCT)
						.header("Authorization","Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.characterEncoding(ITestData.ENCODING)
						.content(objectMapper.writeValueAsString(requestProduct)))
			   .andExpect(MockMvcResultMatchers.status().isCreated())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("Update product")
	void testUpdateProduct() throws Exception {
		RequestProductDTO requestProduct = new RequestProductDTO(ITestData.PRODUCT_NAME_UPDATE, 50000);
		mockMvc.perform(MockMvcRequestBuilders
						.put(ITestData.ROUTE_PRODUCT_ID, ITestData.ID)
						.header("Authorization","Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.characterEncoding(ITestData.ENCODING)
						.content(objectMapper.writeValueAsString(requestProduct)))
			   .andExpect(MockMvcResultMatchers.status().isOk())
			   .andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("Delete product")
	void testDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.delete(ITestData.ROUTE_PRODUCT_ID, ITestData.ID)
						.header("Authorization","Bearer " + token))
			   .andExpect(MockMvcResultMatchers.status().isNoContent())
			   .andDo(MockMvcResultHandlers.print());
	}

}
