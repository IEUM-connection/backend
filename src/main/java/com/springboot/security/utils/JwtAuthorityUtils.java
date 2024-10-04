package com.springboot.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthorityUtils {

    @Value("${mail.address.admin}")
    private String adminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_member");
    private final List<GrantedAuthority> member_ROLES =
            AuthorityUtils.createAuthorityList( "ROLE_member");
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "member");
    private final List<String> member_ROLES_STRING = List.of("member");

    public List<GrantedAuthority> createAuthorities(String email){
        if(email.equals(adminMailAddress)){
            return ADMIN_ROLES;
        }
        return member_ROLES;
    }
    public List<GrantedAuthority>createAuthorities(List<String> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
    public List<String> createRoles(String email){
        if(email.equals(adminMailAddress)){
            return ADMIN_ROLES_STRING;
        }
        return member_ROLES_STRING;
    }


    public String createRole(String email) {
        return email;
    }
}
