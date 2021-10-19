package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Category;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.request.product.ProductRequest;
import com.example.shoppingmall.model.response.category.CategoryResponse;
import com.example.shoppingmall.model.response.product.ProductResponse;
import com.example.shoppingmall.repository.CategoryRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Override
    public ProductResponse save(ProductRequest request) {

        Product product = modelMapper.map(request, Product.class);

        request.getCategoryIds().forEach(categoryId -> categoryRepository.findById(categoryId).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY, categoryId )
        ));

        //find all categories in list categoryId to save
        List<Category> categories = categoryRepository.findAllByIdInAndIsDeletedIsFalse(request.getCategoryIds());
        product.setCategories(new HashSet<>(categories));

        product = productRepository.save(product);

        //get all category of product to show
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        List<CategoryResponse> categoryResponses = product.getCategories().stream().map(
                category -> modelMapper.map(category, CategoryResponse.class)
        ).collect(Collectors.toList());
        response.setCategoryResponseList(categoryResponses);
        return response;
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return mapProductPageToProductResponsePage(products);
    }

    @Override
    public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_ITEM, id)
        );

        //get all category of product to show
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        List<CategoryResponse> categoryResponses = product.getCategories().stream().map(
                category -> modelMapper.map(category, CategoryResponse.class)
        ).collect(Collectors.toList());
        response.setCategoryResponseList(categoryResponses);

        return response;
    }

    @Override
    public Page<ProductResponse> findByCategoryCode(String code, Pageable pageable) {

        Category categoryRequest = categoryRepository.findByCode(code).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY, code)
        );
        Page<Product> products = productRepository.findProductByCategoriesContains(categoryRequest, pageable);

        return mapProductPageToProductResponsePage(products);
    }

    @Override
    public Page<ProductResponse> searchByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findProductByIsDeletedIsFalseAndNameLike("%" + name + "%", pageable);
        return mapProductPageToProductResponsePage(products);
    }

    @Override
    public ProductResponse update(ProductRequest request, Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_ITEM, id)
        );

        request.getCategoryIds().forEach(categoryId -> categoryRepository.findById(categoryId).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_CATEGORY, categoryId)
        ));

        // set new list category for product
        List<Category> categories = categoryRepository.findAllByIdInAndIsDeletedIsFalse(request.getCategoryIds());
        product.setCategories(new HashSet<>(categories));

        modelMapper.map(request, product);
        product = productRepository.save(product);
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        List<CategoryResponse> categoryResponses = product.getCategories().stream().map(
                category -> modelMapper.map(category, CategoryResponse.class)
        ).collect(Collectors.toList());
        response.setCategoryResponseList(categoryResponses);
        return response;
    }

    @Override
    public Boolean deleteById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_ITEM, id)
        );
        product.setIsDeleted(true);

        productRepository.save(product);
        return true;
    }

    private Page<ProductResponse> mapProductPageToProductResponsePage(Page<Product> products) {
        Page<ProductResponse> productResponses = products.map(
                product -> {
                    ProductResponse response = modelMapper.map(product, ProductResponse.class);

                    // map list category of product to list CategoryResponse of response
                    List<CategoryResponse> categoryResponses = product.getCategories().stream().map(
                            category -> modelMapper.map(category, CategoryResponse.class)
                    ).collect(Collectors.toList());
                    response.setCategoryResponseList(categoryResponses);
                    return response;
                }
        );
        return productResponses;
    }

}
