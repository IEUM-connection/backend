package com.springboot.security.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.security.dto.LoginDto;
import com.springboot.security.jwt.JwtTokenizer;
import com.springboot.security.userdetails.AdminDetails;
import com.springboot.security.userdetails.MemberDetails;
import com.springboot.security.userdetails.UserDetailsImpl;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.springboot.member.entity.Member.MemberStatus.MEMBER_QUIT;

/**
 * JwtAuthenticationFilter는 JWT를 이용한 인증 필터로, 사용자의 로그인 요청을 처리하고 JWT 토큰을 발급합니다.
 * <p>
 * Spring Security의 {@link UsernamePasswordAuthenticationFilter}를 확장하여
 * 사용자 로그인 인증 로직을 커스터마이즈합니다.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;  // 인증 관리자
    private final JwtTokenizer jwtTokenizer;  // JWT 토큰 생성 및 검증 도구

    /**
     * JwtAuthenticationFilter 생성자.
     *
     * @param authenticationManager 인증 관리자
     * @param jwtTokenizer          JWT 토큰 생성 및 검증 도구
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 사용자의 로그인 요청을 처리하고, 로그인 타입에 따라 사용자 인증을 시도합니다.
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @return 인증 결과 (Authentication 객체)
     * @throws AuthenticationException 인증 실패 시 예외 발생
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);  // 요청 본문에서 로그인 정보를 파싱

        String loginType = request.getHeader("loginType"); // 로그인 타입을 헤더에서 가져옴

        // 로그인 타입에 따라 CustomAuthenticationToken 생성
        CustomAuthenticationToken authenticationToken;

        switch (loginType) {
            case "guardian":
                authenticationToken = new CustomAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword(), loginType);  // 보호자 로그인
                break;
            case "admin":
                authenticationToken = new CustomAuthenticationToken(
                        loginDto.getAdminCode(), loginDto.getPassword(), loginType);  // 관리자 로그인
                break;
            case "member":
                authenticationToken = new CustomAuthenticationToken(
                        loginDto.getMemberCode(), null, loginType);  // 회원 로그인 (비밀번호 없이 인증코드로 로그인)
                break;
            default:
                throw new AuthenticationException("잘못된 로그인 타입입니다") {};  // 잘못된 로그인 타입 처리
        }

        return authenticationManager.authenticate(authenticationToken);  // 인증 시도
    }

    /**
     * 인증 성공 시 JWT 토큰을 생성하고 응답 헤더에 추가합니다.
     *
     * @param request        HTTP 요청
     * @param response       HTTP 응답
     * @param filterChain    필터 체인
     * @param authentication 인증 객체
     * @throws ServletException, IOException 예외 처리
     */
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) throws ServletException, IOException {
        Object principal = authentication.getPrincipal();  // 인증된 사용자의 principal 객체 가져오기
        String accessToken;
        String refreshToken;

        if (principal instanceof UserDetailsImpl) {
            // Guardian에 대한 토큰 처리
            Guardian guardian = ((UserDetailsImpl) principal).getGuardian();
            accessToken = delegateAccessToken(guardian);
            refreshToken = delegateRefreshToken(guardian, accessToken);
        } else if (principal instanceof AdminDetails) {
            // Admin에 대한 토큰 처리
            Admin admin = ((AdminDetails) principal).getAdmin();
            accessToken = delegateAccessToken(admin);
            refreshToken = delegateRefreshToken(admin, accessToken);
        } else if (principal instanceof MemberDetails) {
            // Member에 대한 토큰 처리 (비밀번호 없이)
            Member member = ((MemberDetails) principal).getMember();
            accessToken = delegateAccessToken(member);
            refreshToken = delegateRefreshToken(member, accessToken);

            // 탈퇴한 회원 체크
            if (member.getMemberStatus() == MEMBER_QUIT) {
                throw new AuthenticationException("탈퇴한 회원입니다") {};
            }
        } else {
            throw new AuthenticationException("알 수 없는 권한입니다") {};
        }

        // 토큰을 응답 헤더에 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        // 인증 성공 후 처리 (기존 SuccessHandler 호출)
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 회원(Member)에 대한 액세스 토큰을 생성합니다.
     *
     * @param member 회원 객체
     * @return 생성된 액세스 토큰
     */
    protected String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getMemberCode());
        claims.put("roles", member.getRole());

        String subject = member.getMemberCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getMemberAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    /**
     * 보호자(Guardian)에 대한 액세스 토큰을 생성합니다.
     *
     * @param guardian 보호자 객체
     * @return 생성된 액세스 토큰
     */
    protected String delegateAccessToken(Guardian guardian) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", guardian.getEmail());
        claims.put("roles", guardian.getRole());

        String subject = guardian.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    /**
     * 관리자(Admin)에 대한 액세스 토큰을 생성합니다.
     *
     * @param admin 관리자 객체
     * @return 생성된 액세스 토큰
     */
    protected String delegateAccessToken(Admin admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", admin.getAdminCode());
        claims.put("roles", admin.getRole());

        String subject = admin.getAdminCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }


    protected String delegateRefreshToken(Member member, String accessToken) {
        String subject = member.getMemberCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey, accessToken);
    }

    /**
     * 보호자(Guardian)에 대한 리프레시 토큰을 생성합니다.
     *
     * @param guardian 보호자 객체
     * @param accessToken 생성된 액세스 토큰
     * @return 생성된 리프레시 토큰
     */
    protected String delegateRefreshToken(Guardian guardian, String accessToken) {
        String subject = guardian.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey, accessToken);
    }

    /**
     * 관리자(Admin)에 대한 리프레시 토큰을 생성합니다.
     *
     * @param admin 관리자 객체
     * @param accessToken 생성된 액세스 토큰
     * @return 생성된 리프레시 토큰
     */
    protected String delegateRefreshToken(Admin admin, String accessToken) {
        String subject = admin.getAdminCode();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey, accessToken);
    }
}
