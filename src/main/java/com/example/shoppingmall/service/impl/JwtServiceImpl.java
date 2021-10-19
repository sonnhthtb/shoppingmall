package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.model.CustomUserDetail;
import com.example.shoppingmall.service.JwtService;
import com.example.shoppingmall.utils.JwtUtils;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final int EXPIRE_SECONDS= 3600;

    public String generateTokenLogin(Authentication authentication) {
        CustomUserDetail userPrincipal = (CustomUserDetail) authentication.getPrincipal();

        Map<String, Object> roles = new HashMap<>();
        roles.put("roles",  userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return JwtUtils.build(userPrincipal.getUsername(), EXPIRE_SECONDS, roles);
    }

    //refresh_token with expired longer than access_token
    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetail userPrincipal = (CustomUserDetail) authentication.getPrincipal();
        return JwtUtils.build(userPrincipal.getUsername(), 3*EXPIRE_SECONDS);
    }

    public Boolean validateJwtToken(String token) {
        try {
            JwtUtils.parse(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e);
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        String userName = JwtUtils.parse(token).getSubject();
        return userName;
    }
}
