package com.springboot.security.provider;

import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.service.AdminService;
import com.springboot.member.service.GuardianService;
import com.springboot.security.service.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AdminService adminService;
    private final GuardianService guardianService;
    private final AuthService authService;

    public CustomAuthenticationProvider(AdminService adminService, GuardianService guardianService, AuthService authService) {
        this.adminService = adminService;
        this.guardianService = guardianService;
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginType = (String) authentication.getDetails(); // 로그인 타입 가져오기

        if ("admin".equals(loginType)) {
            return authenticateAdmin(authentication);
        } else if ("guardian".equals(loginType)) {
            return authenticateGuardian(authentication);
        } else if ("user".equals(loginType)) {
            return authenticateUser(authentication);
        } else {
            throw new AuthenticationException("지원하지 않는 로그인 타입입니다") {};
        }
    }

    private Authentication authenticateAdmin(Authentication authentication) {
        String employeeCd = authentication.getName();
        String password = (String) authentication.getCredentials();
        Admin admin = adminService.verifyAdminCredentials(employeeCd, password); // 관리자 인증 로직

        if (admin == null) {
            throw new BadCredentialsException("사번 또는 비밀번호가 잘못되었습니다.");
        }

        return new UsernamePasswordAuthenticationToken(admin, password, admin.getAuthorities());
    }

    private Authentication authenticateGuardian(Authentication authentication) {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        Guardian guardian = guardianService.verifyGuardianCredentials(email, password); // 보호자 인증 로직

        if (guardian == null) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return new UsernamePasswordAuthenticationToken(guardian, password, guardian.getAuthorities());
    }

    private Authentication authenticateUser(Authentication authentication) {
        String memberId = authentication.getName();
        // 사용자 인증은 일회용 인증 코드로 처리하므로 비밀번호는 사용하지 않음
        Member member = authService.getMemberById(memberId);

        if (member == null) {
            throw new BadCredentialsException("유효하지 않은 사용자입니다.");
        }

        return new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}