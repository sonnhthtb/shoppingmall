package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.request.user.RoleToUserRequest;
import com.example.shoppingmall.model.response.role.RoleResponse;
import com.example.shoppingmall.model.response.user.UserResponse;
import com.example.shoppingmall.repository.RoleRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> findAll() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = users.stream().map(
                user -> {
                    UserResponse userResponse = modelMapper.map(user, UserResponse.class);
                    List<RoleResponse> roleResponses = user.getRoles().stream().map(
                            role -> modelMapper.map(role, RoleResponse.class)
                    ).collect(Collectors.toList());
                    userResponse.setRoles(roleResponses);
                    return userResponse;
                }
        ).collect(Collectors.toList());

        return responses;
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

        User user = modelMapper.map(request, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
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
