package com.springboot.member.service;

import com.springboot.member.entity.Member;

import com.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Member createMember(Member member) {
        return memberRepository.save(member);
    }


    public Member updateMember(Long memberId, Member updatedMember) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Optional.ofNullable(updatedMember.getName()).ifPresent(member::setName);
        Optional.ofNullable(updatedMember.getTel()).ifPresent(member::setTel);
        Optional.ofNullable(updatedMember.getAddress()).ifPresent(member::setAddress);
        Optional.ofNullable(updatedMember.getDetailedAddress()).ifPresent(member::setDetailedAddress);
        Optional.ofNullable(updatedMember.getPostalCode()).ifPresent(member::setPostalCode);

        return memberRepository.save(member);
    }


    public Member quitMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT); // 상태 변경
        return memberRepository.save(member);  // 상태만 업데이트
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }


    public Member addAdminComment(Long memberId, String notes) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setAdminNote(notes); // 관리자 코멘트 필드 추가
        return memberRepository.save(member);
    }

    // 멤버 조회 (구청 위치 기반)
    public List<Member> getMembersByLocation(String location) {
        return memberRepository.findByAddressContaining(location);
    }

    public List<Member> searchMembers(String query, int age, String medicalHistory) {
        return memberRepository.findByNameContainingAndAgeAndMedicalHistoryContaining(query, age, medicalHistory);
    }

    //
    public List<Member> getMembersByStatus(String status) {
        return memberRepository.findByMemberStatus(status);
    }


    // 모든 멤버 목록 조회
    public Page<Member> findAllMembers(int page, int size) {
        return memberRepository.findAll(
                PageRequest.of(page, size, Sort.by("memberId").descending())
        );
    }
}