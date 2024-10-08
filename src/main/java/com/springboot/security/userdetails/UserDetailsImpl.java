package com.springboot.security.userdetails;


import com.springboot.member.entity.Guardian;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private Guardian guardian;

    // 생성자에서 Guardian 객체를 받아 필드에 설정
    public UserDetailsImpl(Guardian guardian) {
        this.setGuardian(guardian);
    }

    // Guardian 객체를 설정하는 setter
    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    // Guardian 객체를 반환하는 getter
    public Guardian getGuardian() {
        return guardian;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(guardian.getRole()));
    }

    @Override
    public String getPassword() {
        return guardian.getPassword();
    }

    @Override
    public String getUsername() {
        return guardian.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
