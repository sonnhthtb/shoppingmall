package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Category;
import com.example.shoppingmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findProductByCategoriesContains(Category category, Pageable pageable);

    List<Product> findProductByCategoriesContains(Category category);

    Page<Product> findProductByIsDeletedIsFalseAndNameLike(String name, Pageable pageable);
}
