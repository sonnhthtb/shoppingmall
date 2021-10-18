package com.example.shoppingmall.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private Long id;
    private String token;
    private String username;
    private String fullName;
    private Collection<? extends GrantedAuthority> roles;


}
