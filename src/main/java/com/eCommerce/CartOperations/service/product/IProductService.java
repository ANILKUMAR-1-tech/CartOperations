package com.eCommerce.CartOperations.service.product;

import com.eCommerce.CartOperations.dto.ProductDto;
import com.eCommerce.CartOperations.model.Product;
import com.eCommerce.CartOperations.request.AddProductRequest;
import com.eCommerce.CartOperations.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
   Product addpProduct(AddProductRequest product);
   Product getProductById(Long id);
   void deleteProduct(Long id);

   Product updateProduct(ProductUpdateRequest request, Long productId);

   List<Product> getAllProducts();
   List<Product> getProductsByCategory(String category);
   List<Product> getProductsByBrand(String brand);
   List<Product> getProductsByCategoryAndBrand(String category, String brand);
   List<Product> getProductsByName(String name);
   List<Product> getProductsByBrandAndName(String brand,String name);
   Long countProductsByBrandAndName(String brand,String name);

   List<ProductDto> getConvertDto(List<Product> products);

   ProductDto convertToProductDto(Product product);
}
