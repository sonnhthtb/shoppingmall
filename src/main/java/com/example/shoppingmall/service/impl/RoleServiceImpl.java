package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.shoppingmall.service.RoleService;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new BusinessException(BusinessCode.INTERNAL_SERVER)
        );
    }
}
