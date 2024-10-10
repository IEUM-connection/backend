package com.springboot.member.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") Long memberId) {

        Member member = memberService.getMember(memberId);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }
    @PatchMapping("/{member-id}")
    public ResponseEntity updateMember(@PathVariable("member-id") Long memberId,
                                                           @RequestBody MemberDto.Patch patchDto) {
        Member member = memberService.getMember(memberId);
        memberMapper.updateMemberFromPatchDto(patchDto, member);
        member = memberService.updateMember(memberId, member);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") Long memberId) {
        memberService.quitMember(memberId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{member-id}/notes")
    //가디언이나 어드민만 고치고 올릴 수 있게
    public ResponseEntity  addAdminComment(@PathVariable("member-id") Long memberId, @RequestBody String notes) {
        Member member = memberService.addNotes(memberId, notes);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PatchMapping("/{member-id}/adminNote")
    //어드민만 고치고 올릴 수 있게
    public ResponseEntity  addAdminNote(@PathVariable("member-id") Long memberId, @RequestBody String notes) {
        Member member = memberService.addAdminNote(memberId, notes);
        MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
        return ResponseEntity.status(201).body(responseDto);
    }


    @GetMapping
    public ResponseEntity getAllMembers(
            @Positive @RequestParam int page,
            @Positive @RequestParam int size) {

        // MemberService에서 멤버 목록 조회
        Page<Member> pageMembers = memberService.findAllMembers(page - 1, size);

        // Member 엔티티를 DTO로 변환
        List<MemberDto.Response> responseDtos = pageMembers.getContent().stream()
                .map(memberMapper::memberToResponseDto)
                .collect(Collectors.toList());

        // MultiResponseDto로 감싸서 응답
        return new ResponseEntity<>(new MultiResponseDto<>(responseDtos, pageMembers), HttpStatus.OK);
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
