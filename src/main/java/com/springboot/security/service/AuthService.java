package com.springboot.security.service;


import com.springboot.member.entity.Member;
import com.springboot.security.jwt.JwtTokenizer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private final JwtTokenizer jwtTokenizer;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthService(JwtTokenizer jwtTokenizer, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenizer = jwtTokenizer;
        this.redisTemplate = redisTemplate;
    }
    public boolean logout(String membername) {
        return jwtTokenizer.deleteRegisterToken(membername); // JwtTokenizer를 사용하여 저장된 토큰을 삭제합니다.
    }

    public Member getMemberById(String memberId) {
        return null;
    }
}
