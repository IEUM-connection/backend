package com.springboot.member.controller;

import com.springboot.dto.*;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberDto.Post postDto) {
        Member member = memberMapper.memberPostDtoToMember(postDto);
        Member registerMember = memberService.createMember(member);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(registerMember);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") Long memberId) {
        Member member = memberService.getMember(memberId);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @GetMapping("/member")
    public ResponseEntity getMember(Authentication authentication) {
        String memberCode = authentication.getName();
        Member member = memberService.findMember(memberCode);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@PathVariable("member-id") Long memberId, @RequestBody MemberDto.Patch patchDto) {
        Member member = memberService.getMember(memberId);
        memberMapper.updateMemberFromPatchDto(patchDto, member);
        member = memberService.updateMember(memberId, member);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") Long memberId) {
        memberService.quitMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/notes")
    // 가디언이나 어드민만 수정 가능
    public ResponseEntity addAdminComment(@PathVariable("member-id") Long memberId, @RequestBody String notes) {
        Member member = memberService.addNotes(memberId, notes);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.status(201).body(new SingleResponseDto<>(responseDto));
    }

    @PatchMapping("/{member-id}/adminNote")
    // 어드민만 수정 가능
    public ResponseEntity addAdminNote(@PathVariable("member-id") Long memberId, @RequestBody String notes) {
        Member member = memberService.addAdminNote(memberId, notes);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.status(201).body(new SingleResponseDto<>(responseDto));
    }

    @GetMapping
    public ResponseEntity getAllMembers(@Positive @RequestParam int page, @Positive @RequestParam int size) {
        Page<Member> pageMembers = memberService.findAllMembers(page - 1, size);
        List<MemberDto.Response> responseDtos = pageMembers.getContent().stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new MultiResponseDto<>(responseDtos, pageMembers), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity getMembersByStatus(@PathVariable Member.MemberStatus status, @RequestParam int page, @RequestParam int size) {
        List<Member> members = memberService.getMembersByStatus(status);
        List<MemberDto.Response> responseDtos = members.stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new SingleResponseDto<>(responseDtos));
    }

    @PatchMapping("/{member-id}/approve")
    // 어드민만 승인 가능
    public ResponseEntity approveMember(@PathVariable("member-id") Long memberId) {
        Member member = memberService.aprroveMember(memberId);
        member.setCreatedAt(LocalDateTime.now());
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }
    // FCM 토큰 코드 추가
    // FCM 토큰 업데이트를 위한 새로운 엔드포인트
    @PostMapping("/{member-id}/fcm-token")
    public ResponseEntity updateFcmToken(@PathVariable("member-id") Long memberId, @RequestBody MemberDto.FcmTokenUpdate fcmTokenDto) {
        // 1. memberService를 통해 FCM 토큰을 업데이트합니다.
        Member member = memberService.updateFcmToken(memberId, fcmTokenDto.getFcmToken());

        // 2. 업데이트된 멤버 정보를 DTO로 변환합니다.
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);

        // 3. 업데이트된 멤버 정보를 응답으로 반환합니다.
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }
}
