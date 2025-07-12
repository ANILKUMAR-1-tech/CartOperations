package com.eCommerce.CartOperations.service.category;


import com.eCommerce.CartOperations.exceptions.AlreadyExistsException;
import com.eCommerce.CartOperations.exceptions.ProductNotFoundException;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.Category;
import com.eCommerce.CartOperations.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName()+"already exist"));
    }

    @Override
    public Category updateCategory(Category category,Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory-> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(()-> new ProductNotFoundException("Category not found"));

    }

    @Override
    public void deleteCategoryById(Long id) {
          categoryRepository.findById(id).ifPresentOrElse(categoryRepository ::delete,
                  ()-> {
              throw new ResourceNotFoundException("Category not found");
                  });
    }
}
