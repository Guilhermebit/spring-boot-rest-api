package com.personal.project.api.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.api.controller.ProductController;
import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.controller.dto.product.ResponseProductDTO;
import com.personal.project.api.service.ProductService;
import com.personal.project.api.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Autowired
    ObjectMapper objectMapper;


    /**
     * Method under test: {@link ProductController#getProductBetweenPrice(Integer, Integer)}
     */

    @Test
    @DisplayName("Should return a product through a range of prices")
    void testFindProductBetweenPrice() throws Exception {
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.findProductBetweenPrice(anyInt(), anyInt())).thenReturn(List.of(product));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(UTestData.ROUTE_PRODUCT_VALUES, 10000, 17000);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(product.id())))
                .andExpect(jsonPath("$.data[0].name", is(product.name())))
                .andExpect(jsonPath("$.data[0].price_in_cents", is(product.price_in_cents())));

    }

    /**
     * Method under test: {@link ProductController#getProductBetweenPrice(Integer, Integer)}
     */

    @Test
    @DisplayName("Should throw exception when values are not valid - findProductBetweenPrice")
    void testFindProductBetweenPriceInvalid() throws Exception {

        // both values null
       // AssertionError assertionError = assertThrows(AssertionError.class, () -> {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(UTestData.ROUTE_PRODUCT_VALUES, null, null);
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
       // });

        //String assertionMessage = assertionError.getMessage();

        //assertThat(assertionMessage).contains("ConstraintViolationException");

        // both values negative
        ServletException servletException = assertThrows(ServletException.class, () -> {
            MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders.get(UTestData.ROUTE_PRODUCT_VALUES, -100, -1000);
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(reqBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
        });

        String servletMessage = servletException.getMessage();

        assertThat(servletMessage).contains("ConstraintViolationException");

    }

    /**
     * Method under test: {@link ProductController#getProductById(String)}
     */

    @Test
    @DisplayName("Should return a product by id")
    void testFindById() throws Exception {
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.findUniqueProduct(anyString())).thenReturn(product);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(UTestData.ROUTE_PRODUCT_ID, UTestData.PRODUCT_ID);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(product.id())))
                .andExpect(jsonPath("$.data.name", is(product.name())))
                .andExpect(jsonPath("$.data.price_in_cents", is(product.price_in_cents())));
        
    }

    /**
     * Method under test: {@link ProductController#getProductById(String)}
     */

    @Test
    @DisplayName("Should return a 404 status code when product is not found")
    void testFindByIdNotFound() throws Exception {

        when(productService.findUniqueProduct(anyString())).thenThrow(new ObjectNotFoundException("Product not found!"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(UTestData.ROUTE_PRODUCT_ID, "0123456789");
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    /**
     * Method under test: {@link ProductController#getAllProducts()}
     */

    @Test
    @DisplayName("Should return a list of products in JSON format")
    void testFindAll() throws Exception  {
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.findAllProducts()).thenReturn(List.of(product));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(UTestData.ROUTE_PRODUCT);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(product.id())))
                .andExpect(jsonPath("$.data[0].name", is(product.name())))
                .andExpect(jsonPath("$.data[0].price_in_cents", is(product.price_in_cents())));

    }

    /**
     * Method under test {@link ProductController#saveProduct(RequestProductDTO)}
     */

    @Test
    @DisplayName("Should create a product")
    void testCreateProduct() throws Exception {
        RequestProductDTO productDTO = UTestData.createValidProductRequest();
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.create(productDTO)).thenReturn(product);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(UTestData.ROUTE_PRODUCT)
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(product.id())))
                .andExpect(jsonPath("$.data.name", is(product.name())))
                .andExpect(jsonPath("$.data.price_in_cents", is(product.price_in_cents())));

    }

    /**
     * Method under test {@link ProductController#saveProduct(RequestProductDTO)}
     */

    @Test
    @DisplayName("Should return bad request when creating an invalid course")
    void testCreateInvalid() throws Exception {
        List<RequestProductDTO> productList = UTestData.createInvalidProductRequest();

        for(RequestProductDTO productDTO : productList) {

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(UTestData.ROUTE_PRODUCT)
                    .content(objectMapper.writeValueAsString(productDTO))
                    .contentType(MediaType.APPLICATION_JSON);
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

    }

    /**
     * Method under test: {@link ProductController#updateProduct(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should update a product")
    void testUpdateProduct() throws Exception {
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.update(anyString(), any())).thenReturn(product);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(UTestData.ROUTE_PRODUCT_ID, UTestData.PRODUCT_ID)
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(product.id())))
                .andExpect(jsonPath("$.data.name", is(product.name())))
                .andExpect(jsonPath("$.data.price_in_cents", is(product.price_in_cents())));
        
    }

    /**
     * Method under test: {@link ProductController#updateProduct(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should throw an exception when updating an invalid product ID")
    void testUpdateNotFound() throws Exception {
        ResponseProductDTO product = UTestData.createValidProductResponse();

        when(productService.update(anyString(), any())).thenThrow(new ObjectNotFoundException("Product not found!"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(UTestData.ROUTE_PRODUCT_ID, "0123456789")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    /**
     * Method under test: {@link ProductController#updateProduct(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should throw exception when id is not valid - update")
    void testUpdateInvalid() throws Exception {
        RequestProductDTO product = UTestData.createValidProductRequest();

        // invalid id and valid product
        MockHttpServletRequestBuilder reqBuilder = MockMvcRequestBuilders
                .put(UTestData.ROUTE_PRODUCT_ID, "0123456789")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // valid id and invalid product
        List<RequestProductDTO> productList = UTestData.createInvalidProductRequest();
        for(RequestProductDTO productDTO : productList) {

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .put(UTestData.ROUTE_PRODUCT_ID, UTestData.PRODUCT_ID)
                    .content(objectMapper.writeValueAsString(productDTO))
                    .contentType(MediaType.APPLICATION_JSON);
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

        // invalid id and invalid course
        for(RequestProductDTO productDTO : productList) {

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .put(UTestData.ROUTE_PRODUCT_ID, "0123456789")
                    .content(objectMapper.writeValueAsString(productDTO))
                    .contentType(MediaType.APPLICATION_JSON);
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

    }

    /**
     * Method under test: {@link ProductController#deleteProduct(String)}
     */

    @Test
    @DisplayName("Should soft delete a product")
    void testDeleteProduct() throws Exception {

        doNothing().when(productService).delete(anyString());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(UTestData.ROUTE_PRODUCT_ID, UTestData.PRODUCT_ID);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    /**
     * Method under test: {@link ProductController#deleteProduct(String)}
     */

    @Test
    @DisplayName("Should return empty when product not found - delete")
    void testDeleteNotFound() throws Exception {

        doThrow(new ObjectNotFoundException("Product not found!")).doNothing().when(productService).delete(anyString());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(UTestData.ROUTE_PRODUCT_ID, "0123456789");
            MockMvcBuilders.standaloneSetup(productController)
                    .build()
                    .perform(requestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

}
