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
import java.util.Collections;

@Configuration  // 스프링 설정 클래스임을 나타냄
@EnableWebSecurity  // Spring Security를 활성화함
public class SecurityConfig {

    // 사용자 인증 관련 커스텀 로직을 구현한 서비스
    private final CustomUserDetailsService customUserDetailsService;

    // JWT 토큰 관련 유틸리티 클래스
    private final JwtTokenizer jwtTokenizer;

    // JWT 권한 관련 유틸리티 클래스
    private final JwtAuthorityUtils authorityUtils;

    // Redis에 대한 템플릿, 세션 및 토큰 관리를 위한 캐시 역할
    private final RedisTemplate<String, Object> redisTemplate;

    // SecurityConfig 생성자
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenizer jwtTokenizer, JwtAuthorityUtils authorityUtils, RedisTemplate<String, Object> redisTemplate) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.redisTemplate = redisTemplate;
    }

    @Bean  // SecurityFilterChain을 정의하는 Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()  // 동일 출처에서의 프레임 사용을 허용
                .and()
                .csrf().disable()  // CSRF 보호 비활성화
                .cors(Customizer.withDefaults())
                .authorizeRequests()  // URL별 요청 권한을 설정
                .antMatchers("/h2/**").permitAll()  // H2 콘솔에 대한 요청을 모두 허용
                .antMatchers(HttpMethod.POST, "/members").permitAll()  // 회원가입 요청 허용
                .antMatchers(HttpMethod.POST, "/guardians").permitAll()  // 보호자 가입 요청 허용
                .antMatchers("/auth/login").permitAll()  // 로그인 관련 요청 허용
                .antMatchers("/admin/**").hasRole("ADMIN")  // 관리자 페이지에 접근하려면 'ADMIN' 권한 필요
                .antMatchers("/guardians/**").hasAnyRole("GUARDIAN", "ADMIN")  // 보호자 또는 관리자는 접근 가능
                .antMatchers(HttpMethod.GET, "/members/**").hasAnyRole("MEMBER", "GUARDIAN", "ADMIN")  // 회원 정보 조회는 멤버, 보호자, 관리자가 가능
                .antMatchers(HttpMethod.PATCH, "/members/**").hasAnyRole("GUARDIAN", "ADMIN")  // 회원 정보 수정은 보호자와 관리자가 가능
                .antMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole("GUARDIAN", "ADMIN")  // 로그아웃 요청은 보호자와 관리자가 가능 /api/identity
                .antMatchers(HttpMethod.POST, "/api/identity/**").permitAll()  // 인증은 모두 다
                .antMatchers(HttpMethod.POST, "/email-code").permitAll()  // 인증은 모두 다
                .antMatchers(HttpMethod.POST, "/verify-email-code/**").permitAll()  // 인증은 모두 다
                .antMatchers(HttpMethod.POST, "/find-password/**").permitAll()  // 비밀번호찾기는 모두 다
                .antMatchers(HttpMethod.POST, "/find-email").permitAll()  // 이메일찾기는 모두 다
                .antMatchers(HttpMethod.POST, "/questions").hasAnyRole("GUARDIAN", "ADMIN")  // 보호자 또는 관리자가 문의사항에 접근 가능
                .antMatchers(HttpMethod.POST, "/answers").hasAnyRole( "ADMIN")  // 관리자가 문의사항 답변에 접근
                .antMatchers(HttpMethod.GET, "/questions/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/logout").hasAnyRole("GUARDIAN", "ADMIN")  // 로그아웃 요청은 보호자와 관리자가 가능
                .anyRequest().permitAll()   // 나머지 모든 요청은 인증이 필요
                .and()
                .sessionManagement()  // 세션 관리 설정
                .sessionFixation().newSession()  // 세션 고정 보호 활성화, 새로운 세션 생성
                .maximumSessions(1);  // 각 사용자는 한 번만 로그인 가능

        http.apply(new CustomFilterConfigurer());  // JWT 인증 및 검증 필터 설정을 적용

        return http.build();  // 최종적으로 SecurityFilterChain 반환
    }

    @Bean  // 비밀번호 암호화를 위한 BCryptPasswordEncoder Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 방식으로 비밀번호 암호화
    }

    @Bean  // AuthenticationManager를 정의하는 Bean, 커스텀 인증 관리자를 사용
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Spring이 제공하는 AuthenticationManager를 반환
    }

    @Bean  // CustomAuthenticationProvider를 Bean으로 등록
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder());  // 커스텀 인증 제공자를 사용하여 사용자 인증
    }

    @Bean  // CORS 설정을 위한 Bean
    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));  // 모든 출처를 허용
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));  // GET, POST, PATCH, DELETE 메서드를 허용
//        configuration.addAllowedOrigin("http:");  // 특정 출처를 추가 허용 (현재는 'http:'로 되어 있지만 정확한 출처를 설정해야 함)
//        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));  // Authorization, Refresh 헤더를 노출
//        configuration.addAllowedHeader("*");  // 모든 헤더 허용
//        configuration.addAllowedMethod("*");  // 모든 메서드 허용
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  // URL 기반 CORS 설정 소스 생성
//        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정 적용
//        return source;  // CORS 설정 반환
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTION"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 커스텀 필터 구성 클래스 정의
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);  // AuthenticationManager를 가져옴

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);  // JWT 인증 필터 생성
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");  // 로그인 URL 설정
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());  // 인증 성공 핸들러 설정
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());  // 인증 실패 핸들러 설정

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils, redisTemplate);  // JWT 검증 필터 생성

            builder.addFilter(jwtAuthenticationFilter)  // JWT 인증 필터 추가
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class)  // JWT 검증 필터를 인증 필터 이후에 추가
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);  // OAuth2 로그인 필터 이후에도 JWT 검증 필터 추가
        }
    }
}

