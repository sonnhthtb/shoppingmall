package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {

    VerifyToken findByToken(String token);

}
