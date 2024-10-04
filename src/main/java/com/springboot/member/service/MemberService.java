package com.springboot.member.service;

import com.springboot.member.entity.Member;

import com.springboot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long memberId, Member updatedMember) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setName(updatedMember.getName());
        member.setTel(updatedMember.getTel());
        member.setAddress(updatedMember.getAddress());
        member.setDetailedAddress(updatedMember.getDetailedAddress());
        member.setPostalCode(updatedMember.getPostalCode());
        member.setEmergencyContact(updatedMember.getEmergencyContact());
        member.setNotes(updatedMember.getNotes());
        member.setMedicalHistory(updatedMember.getMedicalHistory());

        return memberRepository.save(member);
    }

    @Transactional
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

    @Transactional
    public Member addAdminComment(Long memberId, String notes) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setAdminNote(notes); // 관리자 코멘트 필드 추가
        return memberRepository.save(member);
    }

    public List<Member> searchMembers(String query, int age, String medicalHistory) {
        return memberRepository.findByNameContainingAndAgeAndMedicalHistoryContaining(query, age, medicalHistory);
    }

    public List<Member> getMembersByStatus(String status) {
        return memberRepository.findByStatus(status);
    }


}
