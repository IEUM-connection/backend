package com.springboot.guardian.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.GuardianDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Guardian;
import com.springboot.member.mapper.GuardianMapper;
import com.springboot.member.service.GuardianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guardians")
public class GuardianController {
   private  final GuardianMapper guardianMapper;
   private final GuardianService guardianService;

    public GuardianController(GuardianMapper guardianMapper, GuardianService guardianService) {
        this.guardianMapper = guardianMapper;
        this.guardianService = guardianService;
    }

    @PostMapping
    public ResponseEntity createGuardian(@RequestBody GuardianDto.Post postDto) {
        Guardian guardian = guardianMapper.guardianPostDtoToguardian(postDto);
        guardian = guardianService.createGuardian(guardian);
        GuardianDto.Response responseDto = guardianMapper.guardianToResponseDto(guardian);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{guardian-id}")
    public ResponseEntity updateGuardian(@PathVariable Long guardianId,
                                                           @RequestBody GuardianDto.Patch patchDto) {
        Guardian guardian = guardianService.getGuardian(guardianId);
        guardianMapper.updateGuardianFromPatchDto(patchDto, guardian);
        guardian = guardianService.updateGuardian(guardianId, guardian);
        GuardianDto.Response responseDto = guardianMapper.guardianToResponseDto(guardian);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{guardian-id}")
    public ResponseEntity deleteguardian(@PathVariable Long guardianId) {
        guardianService.quitGuardian(guardianId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/check-email")
    public ResponseEntity checkEmailDuplicate(@RequestBody GuardianDto.EmailCheckDto requestBody){

        boolean isDuplicate = guardianService.isEmailDuplicate(requestBody.getEmail());

        GuardianDto.Check responseDto = new  GuardianDto.Check(isDuplicate);

        return new ResponseEntity<>(
                new SingleResponseDto<>(responseDto), HttpStatus.OK);
    }





}
