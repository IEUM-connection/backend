package com.springboot.member.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.entity.Admin;
import com.springboot.member.mapper.AdminMapper;
import com.springboot.member.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;

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


    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity patchAdminPassword(
            @Valid @RequestBody AdminDto.PatchPassword requestBody,
            Authentication authentication) {
        String adminCode = authentication.getName();
        Admin admin = adminService.findVerifiedAdmin(adminCode);
        adminService.verifyPassword(adminCode, requestBody.getPassword());
        adminService.updatePassword(admin);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
