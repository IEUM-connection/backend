package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member, Guardian guardian) {
        // memberCode를 기준으로 중복 검사
        if (memberRepository.existsByMemberCode(member.getMemberCode())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }
        if (existsByGuardian(guardian)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }


        // 로그인된 Guardian을 Member에 설정
        member.setGuardian(guardian);

        // 초기 멤버 상태 설정
        member.setMemberStatus(Member.MemberStatus.AWAITING_APPROVAL);

        // 멤버 저장
        return memberRepository.save(member);
    }


    public Member updateMember(Long memberId, Member updatedMember) {
        Member member = getMember(memberId);
        Optional.ofNullable(updatedMember.getName()).ifPresent(member::setName);
        Optional.ofNullable(updatedMember.getTel()).ifPresent(member::setTel);
        Optional.ofNullable(updatedMember.getPhone()).ifPresent(member::setPhone);
        Optional.ofNullable(updatedMember.getAddress()).ifPresent(member::setAddress);
        Optional.ofNullable(updatedMember.getMedicalHistory()).ifPresent(member::setMedicalHistory);
        Optional.ofNullable(updatedMember.getDetailedAddress()).ifPresent(member::setDetailedAddress);
        Optional.ofNullable(updatedMember.getLatitude()).ifPresent(member::setLatitude);
        Optional.ofNullable(updatedMember.getLongitude()).ifPresent(member::setLongitude);
        Optional.ofNullable(updatedMember.getPostalCode()).ifPresent(member::setPostalCode);
        Optional.ofNullable(updatedMember.getEmergencyContact()).ifPresent(member::setEmergencyContact);
        Optional.ofNullable(updatedMember.getDocumentAttachment()).ifPresent(member::setDocumentAttachment);
        Optional.ofNullable(updatedMember.getResidentNumber()).ifPresent(member::setResidentNumber);
        return memberRepository.save(member);
    }

    public Member quitMember(Long memberId) {
        Member member = getMember(memberId);
        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        return memberRepository.save(member);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member addNotes(Long memberId, String notes) {
        Member member = getMember(memberId);
        member.setNotes(notes);
        return memberRepository.save(member);
    }

    public Member addAdminNote(Long memberId, String notes) {
        Member member = getMember(memberId);
        member.setAdminNote(notes);
        return memberRepository.save(member);
    }

    public List<Member> getMembersByLocation(String location) {
        List<Member> members = memberRepository.findByAddressContaining(location);
        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return members;
    }

    public List<Member> searchMembers(String query, int age, String medicalHistory) {
        return memberRepository.findByNameContainingAndAgeAndMedicalHistoryContaining(query, age, medicalHistory);
    }

    public Page<Member> getMembersByStatus(Member.MemberStatus status, int page, int size) {
        Page<Member> members = memberRepository.findByMemberStatus(status, PageRequest.of(page, size, Sort.by("memberId").descending()));
        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return members;
    }

    public List<Member> getMembers() {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return members;
    }

    public Page<Member> findAllMembers(int page, int size) {
        Page<Member> members = memberRepository.findAll(PageRequest.of(page, size, Sort.by("memberId").descending()));
        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return members;
    }

    public Member aprroveMember(Long memberId) {
        Member member = getMember(memberId);
        member.setMemberStatus(Member.MemberStatus.ACTIVE);
        return memberRepository.save(member);
    }

    public Member findMember(String memberCode) {
        return memberRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

    }
    // FCM 토큰 코드 추가
    // FCM 토큰 업데이트를 위한 새로운 메소드
    public Member updateFcmToken(Member member, String fcmToken) {
        // 1. 멤버의 FCM 토큰을 새로운 토큰으로 업데이트합니다.
        member.setFcmToken(fcmToken);

        // 2. 변경된 멤버 정보를 저장하고 반환합니다.
        return memberRepository.save(member);
    }

    public boolean existsByGuardian(Guardian guardian) {
        return memberRepository.existsByGuardian(guardian);
    }

    // 휴대폰 미사용시간 업데이트
    public Member updatePhoneInactiveTime(Member member, int phoneInactiveDuration) {
        member.setPhoneInactiveDuration(phoneInactiveDuration);
        return memberRepository.save(member);
    }
}
