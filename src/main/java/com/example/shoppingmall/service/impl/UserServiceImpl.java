package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.request.user.RoleToUserRequest;
import com.example.shoppingmall.repository.RoleRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.shoppingmall.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {

        log.info("Fetching user {}", username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_USER, username)
        );
    }

    @Transactional
    @Override
    public User save(RegisterRequest request) {

        log.info("Saving new user to the database {}", request.getUsername());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_ROLE, "USER")
        );
        user.setRoles(new HashSet<>(Arrays.asList(role)));
        return userRepository.save(user);
    }

    @Override
    public Boolean addRoleToUser(RoleToUserRequest request) {

        log.info("Adding role {} to the user {}", request.getRoleName(), request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_USER, request.getUsername())
        );
        Role role = roleRepository.findByName(request.getRoleName()).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_ROLE, request.getRoleName())
        );
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean blockUser(String username) {

        log.info("Blocking user {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_USER)
        );
        user.setIsDeleted(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading User from database");
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_USER, username)
        );
        return new CustomUserDetail(user);
    }
}
