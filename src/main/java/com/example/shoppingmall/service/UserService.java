package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.request.user.RoleToUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService{

    List<User> findAll();
    User findByUsername(String username);
    User save(RegisterRequest request);
    Boolean addRoleToUser(RoleToUserRequest request);
    Boolean blockUser(String username);

}
