package com.personal.project.api.unit;

import static org.assertj.core.api.Assertions.assertThat;
import com.personal.project.api.entity.product.Product;
import com.personal.project.api.repository.ProductRepository;
import com.personal.project.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;


/**
 * This is a sample class to test the UserRepository and ProductRepository.
 * Only additional methods to the interface should be tested.
 */

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    /**
     * Method under test: {@link UserRepository#findByLogin(String)}
     */

    @Test
    @DisplayName("Should return a user login")
    void testFindByLogin() {
        UserDetails user = userRepository.findByLogin(UTestData.LOGIN_ADMIN);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(UTestData.LOGIN_ADMIN);
    }

    /**
     * Method under test: {@link ProductRepository#findByRangeOfPrices(Integer, Integer, String)}
     */

    @Test
    @DisplayName("Should return a product through a range of prices")
    void testFindByRangeOfPrices() {
        List<Product> productList = productRepository.findByRangeOfPrices(1, 17000, UTestData.USER_ID);
        assertThat(productList.size()).isEqualTo(1);
        assertThat(productList.get(0).getName()).isEqualTo(UTestData.PRODUCT_NAME);
    }

    /**
     * Method under test: {@link ProductRepository#findProductByUserId(String, String)}
     */

    @Test
    @DisplayName("Should return a product by id")
    void testFindProductByUserId() {
        Optional<Product> product = productRepository.findProductByUserId(UTestData.PRODUCT_ID, UTestData.USER_ID);
        assertThat(product).isNotNull();
        assertThat(product.get().getName()).isEqualTo(UTestData.PRODUCT_NAME);
    }

    /**
     * Method under test: {@link ProductRepository#findAllByUserId(String)}
     */
    
    @Test
    @DisplayName("Should find all active products by user")
    void testFindAllByUserId() {
        List<Product> productList = productRepository.findAllByUserId(UTestData.USER_ID);
        assertThat(productList.size()).isEqualTo(3);
    }
}
