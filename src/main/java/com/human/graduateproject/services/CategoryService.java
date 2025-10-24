package com.human.graduateproject.services;

import com.human.graduateproject.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(CategoryDto categoryDto);
    boolean deleteCategory(Long categoryId);
    CategoryDto getCategoryById(Long categoryId);

}
