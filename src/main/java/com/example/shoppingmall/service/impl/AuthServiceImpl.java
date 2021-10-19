package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.model.request.user.LoginRequest;
import com.example.shoppingmall.model.request.user.RefreshTokenRequest;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.response.user.JwtResponse;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.AuthService;
import com.example.shoppingmall.service.JwtService;
import com.example.shoppingmall.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    private final JwtService jwtService;

    private final UserRepository userRepository;

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

        JwtResponse response = new JwtResponse(accessToken, refreshToken,request.getUsername(),
                customUserDetails.getUser().getFullName(), customUserDetails.getAuthorities());

        return response;
    }

    @Override
    public Boolean register(RegisterRequest request) {
        return null;
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
                String username = jwtService.getUserNameFromJwtToken(refreshToken);

                UserDetails userDetails = loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();

                log.info("Refresh token for user: " + customUserDetails.getUsername());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                Map<String, Object> roles = new HashMap<>();
                roles.put("roles",  customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

                String accessToken = JwtUtils.build(customUserDetails.getUsername(), EXPIRE_SECONDS, roles);

                refreshToken = generateRefreshToken(authentication);

                JwtResponse response = new JwtResponse(accessToken, refreshToken,request.getUsername(),
                        customUserDetails.getUser().getFullName(), customUserDetails.getAuthorities());
                return response;

            }
        } catch (Exception e) {
            log.error("Cannot refresh_token root cause: {} ", e);
        }
        return null;
    }

    private UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading User from database");
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(BusinessCode.NOT_FOUND_USER, username)
        );
        return new CustomUserDetail(user);
    }

    //refresh_token with expired longer than access_token
    private String generateRefreshToken(Authentication authentication) {
        CustomUserDetail userPrincipal = (CustomUserDetail) authentication.getPrincipal();
        return JwtUtils.build(userPrincipal.getUsername(), 3*EXPIRE_SECONDS);
    }
}
