package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.request.user.RoleToUserRequest;
import com.example.shoppingmall.model.response.user.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService{

    List<UserResponse> findAll();
    User findByUsername(String username);
    User save(RegisterRequest request);
    Boolean addRoleToUser(RoleToUserRequest request);
    Boolean blockUser(String username);

}
