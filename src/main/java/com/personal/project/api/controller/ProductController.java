package com.personal.project.api.controller;

import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.utils.ResponseHandler;
import com.personal.project.api.controller.dto.product.ResponseProductDTO;
import com.personal.project.api.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/value/{price1}/{price2}")
    public ResponseEntity<Object> getProductBetweenPrice(@PathVariable @Positive @NotNull Integer price1, @PathVariable @Positive @NotNull Integer price2) {
            List<ResponseProductDTO> listOfProducts = productService.findProductBetweenPrice(price1, price2);
            if(listOfProducts.isEmpty())
                return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, "No products were found within the declared range.", listOfProducts);
            return ResponseHandler.responseBuilder(HttpStatus.OK, "", listOfProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable @NotBlank String id) {
            ResponseProductDTO product = productService.findUniqueProduct(id);
            if(product == null)
                return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, "Product not found.", null);
            return ResponseHandler.responseBuilder(HttpStatus.OK, "", product);
    }

    @GetMapping
    public ResponseEntity<Object> getAllProducts() {
            List<ResponseProductDTO> allProducts = productService.findAllProducts();
            if(allProducts.isEmpty())
               return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, "No products were found.", allProducts);
            return ResponseHandler.responseBuilder(HttpStatus.OK, "", allProducts);
    }

    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid RequestProductDTO requestProductDTO) {
            ResponseProductDTO productCreated = productService.create(requestProductDTO);
            if(productCreated == null)
                return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, "Unable to register product.", null);
            return ResponseHandler.responseBuilder(HttpStatus.CREATED, "", productCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable @NotBlank String id, @RequestBody @Valid RequestProductDTO requestProductDTO) {
            ResponseProductDTO productUpdated = productService.update(id, requestProductDTO);
            if(productUpdated == null)
                return ResponseHandler.responseBuilder(HttpStatus.NOT_FOUND, "Unable to update product.", null);
            return ResponseHandler.responseBuilder(HttpStatus.OK, "", productUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable @NotBlank String id) {
            productService.delete(id);
            return ResponseHandler.responseBuilder(HttpStatus.NO_CONTENT, "Product successfully deleted.", null);
    }

}
