//package com.springboot.member.service;
//
//import com.springboot.member.repository.MemberRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MemberService {
//
//    private final ExternalApiService externalApiService;
//    private final MemberRepository memberRepository;
//
//    public MemberService(ExternalApiService externalApiService, MemberRepository memberRepository) {
//        this.externalApiService = externalApiService;
//        this.memberRepository = memberRepository;
//    }
//
//    public boolean verifyPersonalId(String residentNumber) {
//        // 외부 API로 주민번호 인증 처리
//        return externalApiService.verifyResidentNumber(residentNumber);
//    }
//
//    public void registerMember(MemberDto memberDto) {
//        if (!verifyPersonalId(memberDto.getResidentNumber())) {
//            throw new IllegalArgumentException("주민번호 인증 실패");
//        }
//
//        Member member = new Member();
//        member.setName(memberDto.getName());
//        member.setBirthDate(memberDto.getBirthDate()); // 인증 통과 후 생년월일만 저장
//
//        memberRepository.save(member);
//    }
//}
