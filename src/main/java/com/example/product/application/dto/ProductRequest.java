package com.example.product.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Description is required") String description,
        @PositiveOrZero(message = "Price must be non-negative") double price,
        @Min(value = 0, message = "Stock must be non-negative") int stock,
        @NotBlank(message = "Category is required") String category
) {
}
