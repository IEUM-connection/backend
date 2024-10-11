package com.springboot.security.controller;

import com.springboot.security.dto.LoginDto;
import com.springboot.security.jwt.JwtTokenizer;
import com.springboot.security.service.AuthService;
import com.sun.mail.imap.protocol.IMAPSaslAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenizer jwtTokenizer;

    public AuthController(AuthService authService, JwtTokenizer jwtTokenizer) {
        this.authService = authService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @PostMapping("/logout")
    public ResponseEntity postLogout(Authentication authentication) {
        String username = authentication.getName();


        return authService.logout(username) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
