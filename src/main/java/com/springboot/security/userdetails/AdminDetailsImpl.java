package com.springboot.security.userdetails;

import com.springboot.member.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.memberdetails.memberDetails;

import java.util.Collection;
import java.util.Collections;

public class AdminDetailsImpl implements memberDetails {
    private final Admin admin;

    public AdminDetailsImpl(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(admin.getRole()));
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getmembername() {
        return admin.getAdminCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
