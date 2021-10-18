package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByIsDeletedIsFalse();

    Optional<Category> findByCode(String code);

    List<Category> findAllByIdInAndIsDeletedIsFalse(List<Long> ids);
}
