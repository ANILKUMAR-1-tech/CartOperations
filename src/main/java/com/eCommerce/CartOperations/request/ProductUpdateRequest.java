package com.eCommerce.CartOperations.request;

import com.eCommerce.CartOperations.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private Category category;
}
