package com.eCommerce.CartOperations.dto;

import com.eCommerce.CartOperations.model.Category;
import com.eCommerce.CartOperations.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String description;
    private Category category;
    private List<ImageDto> images;

}
