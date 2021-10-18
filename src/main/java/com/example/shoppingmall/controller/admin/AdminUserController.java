package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.model.BaseResponse;
import com.example.shoppingmall.model.request.user.RoleToUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin/api/v1/users")
@AllArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public BaseResponse<List<User>> getAllUsers() {
        return BaseResponse.ofSuccess(userService.findAll());
    }

    @PostMapping("/addRoleToUser")
    public BaseResponse<Boolean> addRoleToUser(@Valid @RequestBody RoleToUserRequest request) {
        return BaseResponse.ofSuccess(userService.addRoleToUser(request));
    }

    @PutMapping("/block")
    public BaseResponse<Boolean> blockUser(@NotBlank String username) {
        return BaseResponse.ofSuccess(userService.blockUser(username));
    }

}
