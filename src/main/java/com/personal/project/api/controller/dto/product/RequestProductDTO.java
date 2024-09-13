package com.personal.project.api.controller.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestProductDTO (
        @NotBlank(message = "Product name should not be empty or null.") String name,
        @NotNull(message = "Price should not be empty or null.") Integer price_in_cents) {
}
