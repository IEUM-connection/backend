package com.springboot.config;

import com.springboot.security.filter.CustomAuthenticationProvider;
import com.springboot.security.filter.JwtAuthenticationFilter;
import com.springboot.security.filter.JwtVerificationFilter;
import com.springboot.security.handler.MemberAuthenticationFailureHandler;
import com.springboot.security.handler.MemberAuthenticationSuccessHandler;
import com.springboot.security.jwt.JwtTokenizer;
import com.springboot.security.userdetails.CustomUserDetailsService;
import com.springboot.security.utils.JwtAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenizer jwtTokenizer;
    private final JwtAuthorityUtils authorityUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenizer jwtTokenizer, JwtAuthorityUtils authorityUtils, RedisTemplate<String, Object> redisTemplate) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // H2 Console 사용 시 필요
                .and()
                .csrf().disable() // CSRF 비활성화
                .cors(Customizer.withDefaults()) // CORS 설정
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/h2/**").permitAll() // H2 Console 접근 허용
                        .antMatchers(HttpMethod.POST, "/members", "/guardians", "/auth/login", "/api/identity/**", "/email-code", "/verify-email-code/**", "/find-password/**", "/find-email").permitAll() // 회원가입 및 인증 관련 엔드포인트 허용
                        .antMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지
                        .antMatchers("/guardians/**").hasAnyRole("GUARDIAN", "ADMIN") // 보호자와 관리자만 접근 가능
                        .antMatchers(HttpMethod.GET, "/members/**").hasAnyRole("MEMBER", "GUARDIAN", "ADMIN") // 회원 정보 조회 권한
                        .antMatchers(HttpMethod.PATCH, "/members/**").hasAnyRole("MEMBER", "GUARDIAN", "ADMIN") // 회원 정보 수정 권한
                        .antMatchers(HttpMethod.POST, "/questions", "/answers").hasAnyRole("GUARDIAN", "ADMIN") // 문의사항 처리
                        .antMatchers(HttpMethod.GET, "/questions/**").permitAll() // 모든 문의사항 조회 허용
                        .antMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole("MEMBER", "GUARDIAN", "ADMIN") // 로그아웃 요청
                        .anyRequest().permitAll()) // 그 외 모든 요청은 인증 필요
                .sessionManagement()
                .sessionFixation().newSession() // 세션 고정 공격 방지
                .maximumSessions(1); // 한 번에 하나의 세션만 유지 가능

        // JWT 필터 설정 적용
        http.apply(new CustomFilterConfigurer());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 도메인 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh")); // 노출할 헤더
        configuration.setAllowCredentials(true); // 자격증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 커스텀 필터 구성
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            // JWT 인증 필터
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            // JWT 검증 필터
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils, redisTemplate);

            // 필터 적용 순서 설정
            builder.addFilter(jwtAuthenticationFilter)

                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }
}
