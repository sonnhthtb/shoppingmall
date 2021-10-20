package com.example.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shoppingmall.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
