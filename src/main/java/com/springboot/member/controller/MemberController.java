package com.springboot.member.controller;

import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MemberDto.Response> createMember(@RequestBody MemberDto.Post postDto) {
        Member member = memberMapper.memberPostDtoToMember(postDto);
        member = memberService.createMember(member);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto.Response> getMember(@PathVariable Long memberId) {
        Member member = memberService.getMember(memberId);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberDto.Response> updateMember(@PathVariable Long memberId,
                                                           @RequestBody MemberDto.Patch patchDto) {
        Member member = memberService.getMember(memberId);
        memberMapper.updateMemberFromPatchDto(patchDto, member);
        member = memberService.updateMember(memberId, member);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.quitMember(memberId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{memberId}/notes")
    public ResponseEntity<MemberDto.Response> addAdminComment(@PathVariable Long memberId, @RequestBody String notes) {
        Member member = memberService.addAdminComment(memberId, notes);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberDto.Response>> searchMembers(
            @RequestParam String query,
            @RequestParam int age,
            @RequestParam String medicalHistory) {

        List<Member> members = memberService.searchMembers(query, age, medicalHistory);
        List<MemberDto.Response> responseDtos = members.stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<MemberDto.Response>> getMembersByStatus(
            @PathVariable String status,
            @RequestParam int page,
            @RequestParam int size) {

        List<Member> members = memberService.getMembersByStatus(status);
        List<MemberDto.Response> responseDtos = members.stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

}
