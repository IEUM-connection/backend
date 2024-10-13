package com.springboot.member.controller;

import com.springboot.dto.*;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.AdminService;
import com.springboot.member.service.GuardianService;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final GuardianService guardianService;
    private final AdminService adminService;

    public MemberController(MemberService memberService, MemberMapper memberMapper, GuardianService guardianService, AdminService adminService) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.guardianService = guardianService;
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberDto.Post memberPostDto) {
        // 인증된 가디언의 이메일을 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String guardianEmail = authentication.getName();

        // 가디언을 조회
        Guardian guardian = guardianService.findVerifiedGuardian(guardianEmail);


        // 위치에 따른 어드민 이름 조회
        String address = memberPostDto.getAddress();  // memberPostDto에 address가 있다고 가정
        String adminName = adminService.findAdminNameByLocation(address);
       ;
        // 멤버 DTO -> 엔티티로 변환
        Member member = memberMapper.memberPostDtoToMember(memberPostDto);

        member.setAdminName(adminName);
        member.setGuardian(guardian);  // 가디언 정보 설정

        // 멤버 생성
        Member newMember = memberService.createMember(member, guardian);

        // 어드민 이름을 멤버 응답 DTO에 추가
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(newMember);
        // adminName을 응답 DTO에 추가

        // 생성된 멤버의 응답 DTO 반환
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
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
        Page<Member> members = memberService.getMembersByStatus(status, page - 1, size);
        List<MemberDto.Response> responseDtos = members.getContent().stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new MultiResponseDto<>(responseDtos, members), HttpStatus.OK);
    }

    @GetMapping("/withoutPage")
    public ResponseEntity getMembersNOPage() {
        List<Member> members = memberService.getMembers();
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
    @PostMapping("/fcm-token")
    public ResponseEntity updateFcmToken(Authentication authentication, @RequestBody MemberDto.FcmTokenUpdate fcmTokenDto) {
        // 1. 인증 객체에서 memberCode를 추출합니다.
        String memberCode = authentication.getName();

        // 2. memberService를 통해 멤버를 찾고 FCM 토큰을 업데이트합니다.
        Member member = memberService.findMember(memberCode);
        member = memberService.updateFcmToken(member, fcmTokenDto.getFcmToken());

        // 3. 업데이트된 멤버 정보를 DTO로 변환합니다.
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);

        // 4. 업데이트된 멤버 정보를 응답으로 반환합니다.
        return ResponseEntity.ok(new SingleResponseDto<>(responseDto));
    }
    // 휴대폰 미사용 시간 업데이트를 위한 새로운 엔드포인트
    @PatchMapping("/phone-inactive")
    public ResponseEntity updatePhoneInactiveTime(Authentication authentication,
        @RequestBody MemberDto.PhoneInactiveTimeUpdate request) {
        String memberCode = authentication.getName();

        Member member = memberService.findMember(memberCode);

        member = memberService.updatePhoneInactiveTime(member, (int) request.getPhoneInactiveTimeMs());
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }
}
