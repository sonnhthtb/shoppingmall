package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.entity.VerifyToken;
import com.example.shoppingmall.exception.AuthCode;
import com.example.shoppingmall.exception.BusinessCode;
import com.example.shoppingmall.exception.BusinessException;
import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.model.NotificationEmail;
import com.example.shoppingmall.model.request.user.LoginRequest;
import com.example.shoppingmall.model.request.user.RefreshTokenRequest;
import com.example.shoppingmall.model.request.user.RegisterRequest;
import com.example.shoppingmall.model.response.user.JwtResponse;
import com.example.shoppingmall.repository.RoleRepository;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.repository.VerifyTokenRepository;
import com.example.shoppingmall.service.AuthService;
import com.example.shoppingmall.service.JwtService;
import com.example.shoppingmall.service.NotificationService;
import com.example.shoppingmall.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private static final int EXPIRE_SECONDS= 3600;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final NotificationService notificationService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerifyTokenRepository verifyTokenRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

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
    public JwtResponse refreshToken(RefreshTokenRequest request) {

        try {
            //validate refresh token
            String refreshToken = request.getRefreshToken();
            if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
                String username = jwtService.getUserNameFromJwtToken(refreshToken);

                UserDetails userDetails = loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                CustomUserDetail customUserDetails = (CustomUserDetail) authentication.getPrincipal();

                log.info("Refresh token for user: " + customUserDetails.getUsername());


                // generate new token
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

    @Override
    public Boolean register(RegisterRequest request) {

        if(!checkUsernameExisted(request) && !checkEmailExisted(request)) {
            User user = modelMapper.map(request, User.class);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByName("ROLE_USER").orElseThrow(
                    () -> new BusinessException(BusinessCode.NOT_FOUND_ROLE, "ROLE_USER")
            );
            user.setRoles(new HashSet<>(Arrays.asList(role)));
            user.setIsVerified(false);
            userRepository.save(user);

            String token = generateVerificationToken(user);
            Date date = new Date();
            NotificationEmail email = new NotificationEmail("Please Activate your Account", user.getEmail(),
                    "Thank you for signing up to Shopping Mall, " +
                            "this is your secret key to active your account: " + token +
                            "\nThis secret key will expired after 1 hour" );
            notificationService.sendMail(email);
            return true;
        }
        return false;
    }

    @Override
    public Boolean verifyAccount(String token) {
        VerifyToken verifyToken = verifyTokenRepository.findByToken(token);
        Date now = new Date();

        //check can find token and token not expired
        if(verifyToken != null && new Timestamp(now.getTime()).before(verifyToken.getExpiryDate())) {
            String username = verifyToken.getUser().getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BusinessException(BusinessCode.NOT_FOUND_USER, username)
            );
            user.setIsVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkUsernameExisted(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkEmailExisted(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return true;
        }
        return false;
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

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerifyToken verificationToken = new VerifyToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        Date now = new Date();
        verificationToken.setExpiryDate(new Timestamp(now.getTime() + EXPIRE_SECONDS * 1000L));
        verifyTokenRepository.save(verificationToken);
        return token;
    }
}
