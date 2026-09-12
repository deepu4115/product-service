package com.example.product.application.service;

import com.example.product.application.dto.ProductResponse;
import com.example.product.application.mapper.ProductMapper;
import com.example.product.domain.exception.InsufficientStockException;
import com.example.product.domain.exception.ProductNotFoundByIdException;
import com.example.product.domain.model.Product;
import com.example.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void decrementStock_Success() {
        Product product = Product.builder().id(1L).stock(10).build();
        ProductResponse response = new ProductResponse(1L, "Title", "Desc", 100.0, 8, "Cat", null, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.mapToProductResponse(any())).thenReturn(response);

        ProductResponse result = productService.decrementStock(1L, 2);

        assertNotNull(result);
        assertEquals(8, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void decrementStock_InsufficientStock() {
        Product product = Product.builder().id(1L).stock(1).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> productService.decrementStock(1L, 5));
    }

    @Test
    void decrementStock_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundByIdException.class, () -> productService.decrementStock(1L, 1));
    }
}
