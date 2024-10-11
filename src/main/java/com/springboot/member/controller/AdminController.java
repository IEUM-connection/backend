package com.springboot.member.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.entity.Admin;
import com.springboot.member.mapper.AdminMapper;
import com.springboot.member.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService ;
    private final AdminMapper mapper;

    public AdminController(AdminService adminService, AdminMapper mapper) {
        this.adminService = adminService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity getAdmin(Authentication authentication) {
        String adminCode = authentication.getName();
        Admin admin = adminService.findAdmin(adminCode);
        AdminDto.Response responseDto = mapper.adminToResponseDto(admin);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }
}
