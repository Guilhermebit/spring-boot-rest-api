package com.personal.project.api.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.controller.dto.product.ResponseProductDTO;
import com.personal.project.api.entity.product.ProductMapper;
import com.personal.project.api.entity.product.Product;
import com.personal.project.api.repository.ProductRepository;
import com.personal.project.api.service.exceptions.ObjectNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import com.personal.project.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        //Authenticate the user
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(UTestData.LOGIN_ADMIN, UTestData.PASSWORD);
        Authentication authentication = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Method under test: {@link ProductService#findProductBetweenPrice(Integer, Integer)}
     */

    @Test
    @DisplayName("Should return a product through a range of prices")
    void testFindProductBetweenPrice() {
        // Arrange
        Product product = UTestData.createValidProduct();

        // Act
        when(productRepository.findByRangeOfPrices(any(), any(), any())).thenReturn(List.of(product));
        List<ResponseProductDTO> responseProductDTOList = productService.findProductBetweenPrice(10000, 17000);

        // Assert
        assertEquals(productMapper.toResponseProductDTOList(List.of(product)), responseProductDTOList);
        verify(productRepository).findByRangeOfPrices(any(), any(), any());
    }

    /**
     * Method under test: {@link ProductService#findProductBetweenPrice(Integer, Integer)}
     */

    @Test
    @DisplayName("Should throw exception when values are not valid - findProductBetweenPrice")
    void testFindProductBetweenPriceInvalid() {
        assertThrows(ConstraintViolationException.class, () -> productService.findProductBetweenPrice(null, null));
        assertThrows(ConstraintViolationException.class, () -> productService.findProductBetweenPrice(-100, -1000));
    }

    /**
     * Method under test: {@link ProductService#findUniqueProduct(String)}
     */

    @Test
    @DisplayName("Should return a product by id")
    void testFindById() {
        // Arrange
        Product product = UTestData.createValidProduct();

        // Act
        when(productRepository.findProductByUserId(anyString(), anyString())).thenReturn(Optional.of(product));
        ResponseProductDTO responseProductDTO = productService.findUniqueProduct(UTestData.PRODUCT_ID);

        // Assert
        assertEquals(productMapper.mapToResponseProductDTO(product), responseProductDTO);
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#findUniqueProduct(String)}
     */

    @Test
    @DisplayName("Should throw NotFound exception when product not found")
    void testFindByIdNotFound() {
        when(productRepository.findProductByUserId(anyString(), anyString())).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> productService.findUniqueProduct("0123456789"));
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#findUniqueProduct(String)}
     */

    @Test
    @DisplayName("Should throw exception when id is not valid - findById")
    void testFindByIdInvalid() {
        assertThrows(ConstraintViolationException.class, () -> this.productService.findUniqueProduct(null));
    }

    /**
     * Method under test: {@link ProductService#findAllProducts()}
     */

    @Test
    @DisplayName("Should return a list of products")
    void testFindAll() {
        // Arrange
        List<Product> product = List.of(UTestData.createValidProduct());

        // Act
        when(productRepository.findAllByUserId(anyString())).thenReturn(product);
        List<ResponseProductDTO> productDTOList = productService.findAllProducts();

        // Assert
        assertThat(productDTOList).isNotEmpty();
        assertEquals(productMapper.toResponseProductDTOList(product), productDTOList);
        verify(productRepository).findAllByUserId(anyString());
    }

    /**
     * Method under test: {@link ProductService#create(RequestProductDTO)}
     */

    @Test
    @DisplayName("Should create a product")
    void testCreateProduct() {
         // Arrange
         Product product = UTestData.createValidProduct();
         RequestProductDTO requestProductDTO = UTestData.createValidProductRequest();

         // Act
         when(productRepository.save(any())).thenReturn(product);
         ResponseProductDTO responseProductDTO = productService.create(requestProductDTO);

         // Assert
         assertEquals(productMapper.mapToResponseProductDTO(product), responseProductDTO);
         verify(productRepository).save(any());
    }

    /**
     * Method under test: {@link ProductService#create(RequestProductDTO)}
     */

    @Test
    @DisplayName("Should throw an exception when creating an invalid product")
    void testCreateInvalid() {
        List<RequestProductDTO> requestProductDTO = UTestData.createInvalidProductRequest();

        for(RequestProductDTO productDTO : requestProductDTO)
            assertThrows(ConstraintViolationException.class, () -> productService.create(productDTO));

        then(productRepository).shouldHaveNoInteractions();
    }

    /**
     * Method under test: {@link ProductService#update(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should update a product")
    void testUpdateProduct() {
        // Arrange
        Product product = UTestData.createValidProduct();
        RequestProductDTO requestProductDTO = UTestData.createValidProductRequest();

        // Act
        when(productRepository.findProductByUserId(anyString(), anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        ResponseProductDTO responseProductDTO = productService.update(UTestData.PRODUCT_ID, requestProductDTO);

        // Assert
        assertEquals(productMapper.mapToResponseProductDTO(product), responseProductDTO);
        verify(productRepository).save(any());
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#update(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should throw an exception when updating an invalid product ID")
    void testUpdateNotFound() {
        // Arrange
        Product product = UTestData.createValidProduct();
        RequestProductDTO requestProductDTO = UTestData.createValidProductRequest();

        // Act
        when(productRepository.save(any())).thenThrow(new ObjectNotFoundException("Product not found!"));
        when(productRepository.findProductByUserId(anyString(), anyString())).thenReturn(Optional.of(product));

        // Assert
        assertThrows(ObjectNotFoundException.class, () -> productService.update("0123456789", requestProductDTO));
        verify(productRepository).save(any());
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#update(String, RequestProductDTO)}
     */

    @Test
    @DisplayName("Should throw exception when id is not valid - update")
    void testUpdateInvalid() {
        RequestProductDTO requestProductDTO = UTestData.createValidProductRequest();

        // invalid id and valid product
        assertThrows(ConstraintViolationException.class, () -> productService.update(null, requestProductDTO));

        // valid id and invalid product
        List<RequestProductDTO> productDTOList = UTestData.createInvalidProductRequest();
        for(RequestProductDTO productDTO : productDTOList)
            assertThrows(ConstraintViolationException.class, () -> productService.update("0123456789", productDTO));

        // invalid id and invalid course
        for(RequestProductDTO productDTO : productDTOList)
            assertThrows(ConstraintViolationException.class, () -> productService.update(null, productDTO));

        then(productRepository).shouldHaveNoInteractions();
    }

    /**
     * Method under test: {@link ProductService#delete(String)}
     */

    @Test
    @DisplayName("Should soft delete a product")
    void testDeleteProduct() {
        // Arrange
        Product product = UTestData.createValidProduct();

        // Act;
        when(productRepository.findProductByUserId(anyString(), anyString())).thenReturn(Optional.of(product));
        productService.delete(UTestData.PRODUCT_ID);

        // Assert
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#delete(String)}
     */

    @Test
    @DisplayName("Should return empty when product not found - delete")
    void testDeleteNotFound() {
        when(productRepository.findProductByUserId(anyString(), anyString())).thenThrow(new ObjectNotFoundException("Product not found!"));

        assertThrows(ObjectNotFoundException.class, () -> productService.delete("0123456789"));
        verify(productRepository).findProductByUserId(anyString(), anyString());
    }

    /**
     * Method under test: {@link ProductService#delete(String)}
     */

    @Test
    @DisplayName("Should throw exception when id is not valid - delete")
    void testDeleteInvalid() {
        assertThrows(ConstraintViolationException.class, () -> this.productService.delete(null));
    }

}
