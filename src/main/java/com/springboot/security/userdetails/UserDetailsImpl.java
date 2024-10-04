package com.springboot.security.userdetails;

import com.springboot.member.entity.member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.memberdetails.memberDetails;

import java.util.Collection;
import java.util.Collections;

public class memberDetailsImpl implements memberDetails {
    private final member member;

    public memberDetailsImpl(member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getmembername() {
        return member.getmembername();
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

    // 기타 메서드 구현 (isAccountNonExpired, isAccountNonLocked, 등등)
}
