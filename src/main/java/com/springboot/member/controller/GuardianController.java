package com.springboot.member.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.GuardianDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Guardian;
import com.springboot.member.mapper.GuardianMapper;
import com.springboot.member.service.GuardianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<?> createGuardian(@RequestBody GuardianDto.Post postDto) {
        try {
            // 데이터 확인을 위한 로그
            System.out.println("postDto: " + postDto);

            // 매핑
            Guardian guardian = guardianMapper.guardianPostDtoToguardian(postDto);
            System.out.println("Mapped guardian: " + guardian);

            // 서비스 호출
            guardian = guardianService.createGuardian(guardian);
            System.out.println("Saved guardian: " + guardian);

            // 응답 매핑
            GuardianDto.Response responseDto = guardianMapper.guardianToResponseDto(guardian);
            System.out.println("Response DTO: " + responseDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            // 예외 발생 시 로깅
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
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


//    @GetMapping("/check-email")
//    public ResponseEntity checkEmailDuplicate(@RequestBody GuardianDto.EmailCheckDto requestBody){
//
//        boolean isDuplicate = guardianService.isEmailDuplicate(requestBody.getEmail());
//
////        GuardianDto.Check responseDto = new  GuardianDto.Check(isDuplicate);
//
//        return new ResponseEntity<>(
//                new SingleResponseDto<>(isDuplicate), HttpStatus.OK);
//    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = guardianService.isEmailDuplicate(email);
        if (!isDuplicate) {
            return ResponseEntity.ok("Email is already in use");
        }
        return ResponseEntity.ok("Email is available");
    }


}
