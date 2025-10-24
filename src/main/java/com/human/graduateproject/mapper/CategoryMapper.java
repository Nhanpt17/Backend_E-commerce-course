package com.human.graduateproject.mapper;

import com.human.graduateproject.dto.CategoryDto;
import com.human.graduateproject.entity.Category;

public class CategoryMapper {
    public static Category mapToCategory(CategoryDto categoryDto){
        if (categoryDto == null)
            return null;
        return new Category(categoryDto.getId(), categoryDto.getName(), categoryDto.getDescription());
    }

    public static CategoryDto mapToCategoryDto(Category category){
        if (category == null)
            return null;
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }
}
