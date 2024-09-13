package com.personal.project.api.repository;

import com.personal.project.api.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "SELECT * FROM sch_application.product WHERE price_in_cents BETWEEN ?1 AND ?2 AND active = true AND user_id = ?3", nativeQuery = true)
    List<Product> findByRangeOfPrices(Integer price1, Integer price2, String userId);

    @Query(value = "SELECT * FROM sch_application.product WHERE active = true AND user_id = ?1", nativeQuery = true)
    List<Product> findAllByUserId(String userId);

    @Query(value = "SELECT * FROM sch_application.product WHERE active = true AND id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Product> findProductByUserId(String productId, String userId);

}
