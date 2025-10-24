package com.human.graduateproject.services;

import com.human.graduateproject.dto.CategoryDto;
import com.human.graduateproject.entity.Category;
import com.human.graduateproject.mapper.CategoryMapper;
import com.human.graduateproject.repository.CategoryRepository;
import com.human.graduateproject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private  final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        String normalizedName = categoryDto.getName().trim().toLowerCase();

        Optional<Long> existingCategoryId = categoryRepository.findIdByNameIgnoreCase(normalizedName);
        if (existingCategoryId.isPresent()) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDto.getName());
        }

        Category category = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());


        List<Category> categories = categoryRepository.findAll();



        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());



        return categoryDtos;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Long id = categoryDto.getId();
        String normalizedName = categoryDto.getName().trim().toLowerCase();

        // 1. Kiểm tra category có tồn tại
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // 2. Kiểm tra tên có bị trùng với category khác không
        Optional<Long> otherCategoryId = categoryRepository.findIdByNameIgnoreCase(normalizedName);
        if (otherCategoryId.isPresent() && !otherCategoryId.get().equals(id)) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDto.getName());
        }

        // 3. Lưu lại category
        Category updatedCategory = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(updatedCategory);

        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        // 1. Check category có tồn tại (tối ưu hơn findById)
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with id: " + categoryId);
        }

        // 2. Check có product đang dùng category này không
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new RuntimeException("Exists product with categoryId: " + categoryId);
        }

        // 3. Xóa bằng deleteById (không load entity lên)
        categoryRepository.deleteById(categoryId);

        return true;

    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new EntityNotFoundException("category not found with id: "+ categoryId));
        return CategoryMapper.mapToCategoryDto(category);

    }
}
