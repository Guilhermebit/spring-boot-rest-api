package com.personal.project.api.controller.dto.product;

public record ResponseProductDTO (
        String id,
        String name,
        Integer price_in_cents) {
}



