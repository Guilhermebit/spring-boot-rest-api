package com.personal.project.api.entity.product;

import com.personal.project.api.controller.dto.product.RequestProductDTO;
import com.personal.project.api.entity.product.Product;
import com.personal.project.api.controller.dto.product.ResponseProductDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product mapToProduct(RequestProductDTO requestProductDTO) {
        if(requestProductDTO == null)
           return null;

        return new Product(
                requestProductDTO.name(),
                requestProductDTO.price_in_cents()
        );
    }

    public ResponseProductDTO mapToResponseProductDTO(Product product) {
        if(product == null)
           return null;

        return new ResponseProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice_in_cents()
        );
    }

    public List<ResponseProductDTO> toResponseProductDTOList(List<Product> entities) {
        return entities.stream()
                .map(this::mapToResponseProductDTO)
                .collect(Collectors.toList());
    }
}