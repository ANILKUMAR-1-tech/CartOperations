package com.eCommerce.CartOperations.service.product;


import com.eCommerce.CartOperations.dto.ImageDto;
import com.eCommerce.CartOperations.dto.ProductDto;
import com.eCommerce.CartOperations.exceptions.ProductNotFoundException;
import com.eCommerce.CartOperations.model.Category;
import com.eCommerce.CartOperations.model.Image;
import com.eCommerce.CartOperations.model.Product;
import com.eCommerce.CartOperations.repository.CategoryRepository;
import com.eCommerce.CartOperations.repository.ImageRepository;
import com.eCommerce.CartOperations.repository.ProductRepository;
import com.eCommerce.CartOperations.request.AddProductRequest;
import com.eCommerce.CartOperations.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    @Override
    public Product addpProduct(AddProductRequest request) {

        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->
                {
                    Category newCategory=new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getDescription(),
               category

                );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                ()-> {throw new ProductNotFoundException("product not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
         return productRepository.findById(productId)
                 .map(existingProduct-> updateExistingProduct(existingProduct,request))
                 .map(productRepository ::save)
                 .orElseThrow(()-> new ProductNotFoundException("product not found"));

    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());

        Category category=categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setName(category.getName());
        return existingProduct;

    }




    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> getConvertDto(List<Product> products){
        return products.stream().map(this::convertToProductDto).toList();
    }

    @Override
    public ProductDto convertToProductDto(Product product){
       ProductDto productDto= modelMapper.map(product,ProductDto.class);
       Optional<Image> images=imageRepository.findById(product.getId());
       List<ImageDto> imageDtos=images.stream()
               .map(image -> modelMapper.map(image,ImageDto.class))
               .toList();
       productDto.setImages(imageDtos);
       return productDto;
    }
}
