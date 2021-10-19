package com.example.shoppingmall.model.response.user;

import com.example.shoppingmall.model.response.role.RoleResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private String username;

    private String email;

    private String fullName;

    private Boolean is_deleted;

    private List<RoleResponse> roles;

}
