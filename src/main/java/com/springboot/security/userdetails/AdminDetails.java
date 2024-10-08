package com.springboot.security.userdetails;

import com.springboot.member.entity.Admin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * AdminDetailsImpl은 Spring Security의 {@link UserDetails}를 구현하여,
 * 관리자(Admin) 정보를 기반으로 사용자 인증 및 권한을 관리하는 클래스입니다.
 */
@Getter
public class AdminDetails implements UserDetails {

    @Setter
    private Admin admin;  // 관리자 정보를 담는 Admin 객체

    /**
     * AdminDetailsImpl 생성자.
     * <p>
     * {@link Admin} 객체를 주입받아 초기화합니다.
     *
     * @param admin 관리자의 정보를 담은 객체
     */
    public AdminDetails(Admin admin) {
        this.admin = admin;
    }

    /**
     * 관리자 권한을 반환합니다.
     * <p>
     * {@link SimpleGrantedAuthority}를 사용하여 관리자의 역할(role)을 권한으로 반환합니다.
     *
     * @return 관리자의 역할을 포함한 권한 리스트
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(admin.getRole()));
    }

    /**
     * 관리자의 비밀번호를 반환합니다.
     *
     * @return 관리자의 비밀번호
     */
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    /**
     * 관리자의 사용자명을 반환합니다.
     * <p>
     * 여기서는 관리자 코드를 사용자명으로 사용합니다.
     *
     * @return 관리자의 코드 (사용자명)
     */
    @Override
    public String getUsername() {
        return admin.getAdminCode();
    }

    /**
     * 계정이 만료되지 않았는지 여부를 반환합니다.
     * <p>
     * 기본적으로 만료된 것으로 설정되어 있습니다.
     *
     * @return 계정이 만료되었으면 {@code false}, 그렇지 않으면 {@code true}
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 계정이 잠겨있지 않은지 여부를 반환합니다.
     * <p>
     * 기본적으로 잠겨있는 것으로 설정되어 있습니다.
     *
     * @return 계정이 잠겨있으면 {@code false}, 그렇지 않으면 {@code true}
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * 자격 증명이 만료되지 않았는지 여부를 반환합니다.
     * <p>
     * 기본적으로 자격 증명(비밀번호)이 만료된 것으로 설정되어 있습니다.
     *
     * @return 자격 증명이 만료되었으면 {@code false}, 그렇지 않으면 {@code true}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 계정이 활성화되어 있는지 여부를 반환합니다.
     * <p>
     * 기본적으로 비활성화된 것으로 설정되어 있습니다.
     *
     * @return 계정이 활성화되었으면 {@code true}, 그렇지 않으면 {@code false}
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}