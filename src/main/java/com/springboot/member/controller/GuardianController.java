package com.springboot.member.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.GuardianDto;
import com.springboot.member.entity.Guardian;
import com.springboot.member.mapper.GuardianMapper;
import com.springboot.member.service.GuardianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/guardians")
public class GuardianController {
    private final GuardianMapper mapper;
    private final GuardianService guardianService;

    public GuardianController(GuardianMapper mapper, GuardianService guardianService) {
        this.mapper = mapper;
        this.guardianService = guardianService;
    }

    @PostMapping
    public ResponseEntity createGuardian(@RequestBody @Valid GuardianDto.Post postDto) {
        Guardian guardian = mapper.guardianPostDtoToguardian(postDto);
        guardian = guardianService.createGuardian(guardian);
        GuardianDto.Response responseDto = mapper.guardianToResponseDto(guardian);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @PatchMapping("/{guardian-id}")
    public ResponseEntity updateGuardian(@PathVariable("guardian-id") Long guardianId,
                                                                                  @RequestBody GuardianDto.Patch patchDto) {
        Guardian guardian = guardianService.getGuardian(guardianId);
        mapper.updateGuardianFromPatchDto(patchDto, guardian);
        guardian = guardianService.updateGuardian(guardianId, guardian);
        GuardianDto.Response responseDto = mapper.guardianToResponseDto(guardian);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @DeleteMapping("/{guardian-id}")
    public ResponseEntity deleteGuardian(@PathVariable("guardian-id") Long guardianId) {
        guardianService.quitGuardian(guardianId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{guardian-id}")
    public ResponseEntity getGuardian(@PathVariable("guardian-id") Long guardianId) {
        Guardian guardian = guardianService.findVerifiedGuardian(guardianId);
        GuardianDto.Response responseDto = mapper.guardianToResponseDto(guardian);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @PostMapping("/check-email")
    public ResponseEntity  checkEmailDuplicate( @Valid @RequestBody GuardianDto.EmailCheckDto requestBody ) {
        String email = requestBody.getEmail();
        boolean isAvailable = guardianService.isEmailDuplicate(email);
        if (isAvailable) {
            return ResponseEntity.ok("Email is available");
        } else {
            return ResponseEntity.ok("Email is already in use");
        }
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity patchGuardianPassword(
            @Valid @RequestBody GuardianDto.PatchPassword requestBody,
            Authentication authentication) {
        String email = authentication.getName();
        Guardian guardian = guardianService.findVerifiedGuardian(email);
        guardianService.verifyPassword(email, requestBody.getPassword());
        guardianService.updatePassword(guardian);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
