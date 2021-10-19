package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.model.request.user.LoginRequest;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.response.user.JwtResponse;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.AuthService;
import com.example.shoppingmall.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private static final int EXPIRE_SECONDS= 3600;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();

        log.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> roles = new HashMap<>();
        roles.put("roles",  customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String accessToken = JwtUtils.build(customUserDetails.getUsername(), EXPIRE_SECONDS, roles);

        String refreshToken = generateRefreshToken(authentication);

        JwtResponse response = new JwtResponse();

        response.setFullName(customUserDetails.getUser().getFullName());
        response.setUsername(request.getUsername());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setRoles(customUserDetails.getAuthorities());

        return response;
    }

    @Override
    public Boolean register(RegisterRequest request) {
        return null;
    }

    //refresh_token with expired longer than access_token
    private String generateRefreshToken(Authentication authentication) {
        CustomUserDetail userPrincipal = (CustomUserDetail) authentication.getPrincipal();
        return JwtUtils.build(userPrincipal.getUsername(), 3*EXPIRE_SECONDS);
    }
}
