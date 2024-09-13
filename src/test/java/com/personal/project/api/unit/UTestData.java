package com.personal.project.api.unit;

import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.controller.dto.product.ResponseProductDTO;
import com.personal.project.api.controller.dto.user.RequestUserLoginDTO;
import com.personal.project.api.controller.dto.user.RequestUserRegisterDTO;
import com.personal.project.api.controller.dto.user.ResponseUserRegisterDTO;
import com.personal.project.api.entity.user.enums.UserRole;
import com.personal.project.api.entity.product.Product;
import com.personal.project.api.entity.user.User;


import java.util.List;


public class UTestData {

    public static final String LOGIN_ADMIN = "User1"; // ADMIN

    public static final String LOGIN_USER = "User3"; // USER

    public static final String PASSWORD = "1234";

    private static final String INVALID_LOGIN_USER = "User100";

    private static final String INVALID_PASSWORD_USER = "0123456789";

    public static final String PRODUCT_ID = "49c74b02-3f7a-4af6-9b72-04622769adc1";

    public static final String USER_ID = "9d93b715-c6a5-4e48-9ad0-e0d536ab9b3e";

    public static final String PRODUCT_NAME = "Multilaser Wireless Gamer Headset";

    private static final Integer PRODUCT_PRICE = 17000;

    public static final String ROUTE_USER_AUTH = "/auth/register";

    public static final String ROUTE_USER_LOGIN = "/auth/login";

    public static final String ROUTE_PRODUCT = "/product";

    public static final String ROUTE_PRODUCT_ID = "/product/{id}";

    public static final String ROUTE_PRODUCT_VALUES = "/product/value/{price1}/{price2}";

    private UTestData(){

    }

    // USER

    public static User createValidUser() {
        User user = new User();
        //user.setId(USER_ID);
        user.setLogin(LOGIN_USER);
        user.setPassword(PASSWORD);
        user.setRole(UserRole.ADMIN);
        return user;
    }

    public static RequestUserRegisterDTO createValidUserRegisterReq() {
        return new RequestUserRegisterDTO(LOGIN_USER, PASSWORD, UserRole.ADMIN);
    }

    public static ResponseUserRegisterDTO createValidUserRegisterResp() {
        return new ResponseUserRegisterDTO(USER_ID, LOGIN_USER);
    }

    public static List<RequestUserRegisterDTO> createInvalidUserRegisterReq() {
        return List.of(new RequestUserRegisterDTO(null, null, null),
                       new RequestUserRegisterDTO("", "", null));
    }

    public static RequestUserLoginDTO createValidUserLoginReq() {
        return new RequestUserLoginDTO(LOGIN_ADMIN, PASSWORD);
    }

    public static RequestUserLoginDTO createInvalidUserLoginReq() {
        return new RequestUserLoginDTO(INVALID_LOGIN_USER, PASSWORD);
    }

    public static RequestUserLoginDTO createInvalidUserPasswordReq() {
        return new RequestUserLoginDTO(LOGIN_USER, INVALID_PASSWORD_USER);
    }

    // PRODUCT

    public static Product createValidProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice_in_cents(PRODUCT_PRICE);
        product.setActive(true);
        product.setUser(createValidUser());
        return product;
    }

    public static RequestProductDTO createValidProductRequest() {
        return new RequestProductDTO(PRODUCT_NAME, PRODUCT_PRICE);
    }

    public static ResponseProductDTO createValidProductResponse() {
        return new ResponseProductDTO(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE);
    }

    public static List<RequestProductDTO> createInvalidProductRequest() {
        return List.of(new RequestProductDTO(null, null),
                       new RequestProductDTO("", null));
    }

}
