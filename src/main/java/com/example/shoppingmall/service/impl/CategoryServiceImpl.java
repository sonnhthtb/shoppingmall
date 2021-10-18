package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Category;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.request.category.CategoryRequest;
import com.example.shoppingmall.model.response.category.CategoryResponse;
import com.example.shoppingmall.repository.CategoryRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse save(CategoryRequest request) {
        Category category = new Category();
        modelMapper.map(request, category);
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findByIsDeletedIsFalse();
        List<CategoryResponse> categoryResponses = categories.stream().map(
            category -> modelMapper.map(category, CategoryResponse.class)
        ).collect(Collectors.toList());
        return categoryResponses;
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY)
        );
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse update(CategoryRequest request, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY)
        );
        modelMapper.map(request, category);
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public Boolean deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY, id)
        );
        List<Product> productList = productRepository.findProductByCategoriesContains(category);
        productList.forEach(
                product -> {
                    Set<Category> categories = product.getCategories();
                    categories.remove(category);
                    product.setCategories(categories);
                    productRepository.save(product);
                }
        );
        category.setIsDeleted(true);
        categoryRepository.save(category);
        return true;
    }
}
