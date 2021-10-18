package com.example.shoppingmall.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleToUserRequest {

    @NotBlank(message = "Username can not blank")
    private String username;

    @NotBlank(message = "Role name can not blank")
    private String roleName;
}
