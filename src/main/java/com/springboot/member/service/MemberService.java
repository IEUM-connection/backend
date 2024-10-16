package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.memberHistory.MemberHistory;
import com.springboot.memberHistory.MemberHistoryMapper;
import com.springboot.memberHistory.MemberHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberHistoryRepository memberHistoryRepository;

    public MemberService(MemberRepository memberRepository, MemberHistoryRepository memberHistoryRepository) {
        this.memberRepository = memberRepository;
        this.memberHistoryRepository = memberHistoryRepository;
    }

    public Member createMember(Member member, Guardian guardian) {
        // memberCode를 기준으로 중복 검사
        if (memberRepository.existsByMemberCode(member.getMemberCode())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }
        if (existsByGuardian(guardian)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_EXISTS);
        }

        member.setGuardian(guardian);

        member.setMemberStatus(Member.MemberStatus.AWAITING_APPROVAL);
        member.setRole("MEMBER");
        Member savedMember = memberRepository.save(member);
        saveMemberHistory(savedMember);


        // 멤버 저장
        return savedMember;
    }


    public Member updateMember(Long memberId, Member updatedMember) {
        Member member = getMember(memberId);
        String changeDetails = getChangeDetails(member, updatedMember);

        if (updatedMember.getName() != null) {
            member.setName(updatedMember.getName());
        }
        if (updatedMember.getAge() != 0) {
            member.setAge(updatedMember.getAge());
        }
        if (updatedMember.getTel() != null) {
            member.setTel(updatedMember.getTel());
        }
        if (updatedMember.getPhone() != null) {
            member.setPhone(updatedMember.getPhone());
        }
        if (updatedMember.getAddress() != null) {
            member.setAddress(updatedMember.getAddress());
        }
        if (updatedMember.getMedicalHistory() != null) {
            member.setMedicalHistory(updatedMember.getMedicalHistory());
        }
        if (updatedMember.getDetailedAddress() != null) {
            member.setDetailedAddress(updatedMember.getDetailedAddress());
        }
        if (updatedMember.getLatitude() != null) {
            member.setLatitude(updatedMember.getLatitude());
        }
        if (updatedMember.getLongitude() != null) {
            member.setLongitude(updatedMember.getLongitude());
        }
        if (updatedMember.getPostalCode() != null) {
            member.setPostalCode(updatedMember.getPostalCode());
        }
        if (updatedMember.getEmergencyContact() != null) {
            member.setEmergencyContact(updatedMember.getEmergencyContact());
        }
        if (updatedMember.getDocumentAttachment() != null) {
            member.setDocumentAttachment(updatedMember.getDocumentAttachment());
        }

        if (updatedMember.getRelationship() != null) {
            member.setRelationship(updatedMember.getRelationship());
        }
        if (updatedMember.getNotes() != null) {
            member.setNotes(updatedMember.getNotes());
        }
        if (updatedMember.getAdminNote() != null) {
            member.setAdminNote(updatedMember.getAdminNote());
        }
        if (updatedMember.getFcmToken() != null) {
            member.setFcmToken(updatedMember.getFcmToken());
        }


        Member savedMember = memberRepository.save(member);

        // 히스토리 기록
        saveMemberHistory(savedMember);

        return savedMember;
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

    public Member approveMember(Long memberId) {
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
       if( memberRepository.findByGuardian(guardian) != null){
           return false;
       }
       return true;
    }

    // 휴대폰 미사용시간 업데이트
    public Member updatePhoneInactiveTime(Member member, int phoneInactiveDuration) {
        member.setPhoneInactiveDuration(phoneInactiveDuration);
        return memberRepository.save(member);
    }


    public Member getMemberByGuardian(Guardian guardian) {
        List<Member> members = memberRepository.findByGuardian(guardian);

        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        // 여러 멤버 중 첫 번째 멤버 반환 (필요한 경우 다른 처리도 가능)
        return members.get(0);
    }

    private void saveMemberHistory(Member member) {
        MemberHistory memberHistory = new MemberHistory();
        memberHistory.setMemberId(member.getMemberId());
        memberHistory.setMemberStatus(member.getMemberStatus());
        memberHistory.setAddress(member.getAddress());  // 수정된 부분
        memberHistory.setMemberCode(member.getMemberCode());
        memberHistory.setAdminNote(member.getAdminNote());
        memberHistory.setAdminName(member.getAdminName());
        memberHistory.setNotes(member.getNotes());
        memberHistory.setTel(member.getTel());
        memberHistory.setMedicalHistory(member.getMedicalHistory());
        memberHistory.setAge(member.getAge());

        // birthDate가 null일 경우 처리
        if (member.getBirthDate() != null) {
            memberHistory.setBirthDate(member.getBirthDate());
        } else {
            // 기본값을 설정하거나 예외 처리를 할 수 있습니다.
            memberHistory.setBirthDate(LocalDate.now());  // 오늘 날짜를 기본값으로 사용
        }

        memberHistory.setPhone(member.getPhone());
        memberHistory.setCreatedAt(member.getCreatedAt());
        memberHistory.setDetailedAddress(member.getDetailedAddress());
        memberHistory.setDocumentAttachment(member.getDocumentAttachment());
        memberHistory.setEmergencyContact(member.getEmergencyContact());
        memberHistory.setFcmToken(member.getFcmToken());
        memberHistory.setLatitude(member.getLatitude());
        memberHistory.setLongitude(member.getLongitude());
        memberHistory.setMilkDeliveryRequest(member.isMilkDeliveryRequest());
        memberHistory.setPostalCode(member.getPostalCode());
        memberHistory.setPowerUsage(member.getPowerUsage());
        memberHistory.setRelationship(member.getRelationship());
        memberHistory.setRole(member.getRole());
        memberHistory.setPhoneInactiveDuration(member.getPhoneInactiveDuration());
        memberHistory.setName(member.getName());
        memberHistory.setGuardianId(member.getGuardian().getGuardianId());

        memberHistoryRepository.save(memberHistory);
    }

    // 변경 사항 추적 메서드
    private String getChangeDetails(Member existingMember, Member updatedMember) {
        StringBuilder changes = new StringBuilder();
        // 디버깅용 로그 추가
        System.out.println("Existing Member: " + existingMember);
        System.out.println("Updated Member: " + updatedMember);
        // 이름 변경 감지
        Optional.ofNullable(updatedMember.getName())
                .filter(name -> !name.equals(existingMember.getName()))
                .ifPresent(name -> {
                    changes.append("Name changed from ")
                            .append(existingMember.getName() == null ? "unknown" : existingMember.getName())
                            .append(" to ")
                            .append(name)
                            .append(". ");
                });

        // 주소 변경 감지
        Optional.ofNullable(updatedMember.getAddress())
                .filter(address -> !address.equals(existingMember.getAddress()))
                .ifPresent(address -> {
                    changes.append("Address changed from ")
                            .append(existingMember.getAddress() == null ? "unknown" : existingMember.getAddress())
                            .append(" to ")
                            .append(address)
                            .append(". ");
                });

        // 전화번호 변경 감지
        Optional.ofNullable(updatedMember.getTel())
                .filter(tel -> !tel.equals(existingMember.getTel()))
                .ifPresent(tel -> {
                    changes.append("Tel changed from ")
                            .append(existingMember.getTel() == null ? "unknown" : existingMember.getTel())
                            .append(" to ")
                            .append(tel)
                            .append(". ");
                });

        // 핸드폰 번호 변경 감지
        Optional.ofNullable(updatedMember.getPhone())
                .filter(phone -> !phone.equals(existingMember.getPhone()))
                .ifPresent(phone -> {
                    changes.append("Phone changed from ")
                            .append(existingMember.getPhone() == null ? "unknown" : existingMember.getPhone())
                            .append(" to ")
                            .append(phone)
                            .append(". ");
                });

        // 의료 기록 변경 감지
        Optional.ofNullable(updatedMember.getMedicalHistory())
                .filter(medicalHistory -> !medicalHistory.equals(existingMember.getMedicalHistory()))
                .ifPresent(medicalHistory -> {
                    changes.append("Medical history changed from ")
                            .append(existingMember.getMedicalHistory() == null ? "unknown" : existingMember.getMedicalHistory())
                            .append(" to ")
                            .append(medicalHistory)
                            .append(". ");
                });

        // 상세 주소 변경 감지
        Optional.ofNullable(updatedMember.getDetailedAddress())
                .filter(detailedAddress -> !detailedAddress.equals(existingMember.getDetailedAddress()))
                .ifPresent(detailedAddress -> {
                    changes.append("Detailed address changed from ")
                            .append(existingMember.getDetailedAddress() == null ? "unknown" : existingMember.getDetailedAddress())
                            .append(" to ")
                            .append(detailedAddress)
                            .append(". ");
                });

        // 위도 변경 감지
        Optional.ofNullable(updatedMember.getLatitude())
                .filter(latitude -> !latitude.equals(existingMember.getLatitude()))
                .ifPresent(latitude -> {
                    changes.append("Latitude changed from ")
                            .append(existingMember.getLatitude() == null ? "unknown" : existingMember.getLatitude())
                            .append(" to ")
                            .append(latitude)
                            .append(". ");
                });

        // 경도 변경 감지
        Optional.ofNullable(updatedMember.getLongitude())
                .filter(longitude -> !longitude.equals(existingMember.getLongitude()))
                .ifPresent(longitude -> {
                    changes.append("Longitude changed from ")
                            .append(existingMember.getLongitude() == null ? "unknown" : existingMember.getLongitude())
                            .append(" to ")
                            .append(longitude)
                            .append(". ");
                });

        // 우편번호 변경 감지
        Optional.ofNullable(updatedMember.getPostalCode())
                .filter(postalCode -> !postalCode.equals(existingMember.getPostalCode()))
                .ifPresent(postalCode -> {
                    changes.append("Postal code changed from ")
                            .append(existingMember.getPostalCode() == null ? "unknown" : existingMember.getPostalCode())
                            .append(" to ")
                            .append(postalCode)
                            .append(". ");
                });

        // 비상 연락처 변경 감지
        Optional.ofNullable(updatedMember.getEmergencyContact())
                .filter(emergencyContact -> !emergencyContact.equals(existingMember.getEmergencyContact()))
                .ifPresent(emergencyContact -> {
                    changes.append("Emergency contact changed from ")
                            .append(existingMember.getEmergencyContact() == null ? "unknown" : existingMember.getEmergencyContact())
                            .append(" to ")
                            .append(emergencyContact)
                            .append(". ");
                });

        // 첨부 문서 변경 감지
        Optional.ofNullable(updatedMember.getDocumentAttachment())
                .filter(documentAttachment -> !documentAttachment.equals(existingMember.getDocumentAttachment()))
                .ifPresent(documentAttachment -> {
                    changes.append("Document attachment changed from ")
                            .append(existingMember.getDocumentAttachment() == null ? "unknown" : existingMember.getDocumentAttachment())
                            .append(" to ")
                            .append(documentAttachment)
                            .append(". ");
                });



        // 관계 변경 감지
        Optional.ofNullable(updatedMember.getRelationship())
                .filter(relationship -> !relationship.equals(existingMember.getRelationship()))
                .ifPresent(relationship -> {
                    changes.append("Relationship changed from ")
                            .append(existingMember.getRelationship() == null ? "unknown" : existingMember.getRelationship())
                            .append(" to ")
                            .append(relationship)
                            .append(". ");
                });

        // 메모 변경 감지
        Optional.ofNullable(updatedMember.getNotes())
                .filter(notes -> !notes.equals(existingMember.getNotes()))
                .ifPresent(notes -> {
                    changes.append("Notes changed from ")
                            .append(existingMember.getNotes() == null ? "unknown" : existingMember.getNotes())
                            .append(" to ")
                            .append(notes)
                            .append(". ");
                });

        // 관리자 메모 변경 감지
        Optional.ofNullable(updatedMember.getAdminNote())
                .filter(adminNote -> !adminNote.equals(existingMember.getAdminNote()))
                .ifPresent(adminNote -> {
                    changes.append("Admin note changed from ")
                            .append(existingMember.getAdminNote() == null ? "unknown" : existingMember.getAdminNote())
                            .append(" to ")
                            .append(adminNote)
                            .append(". ");
                });

        // FCM 토큰 변경 감지
        Optional.ofNullable(updatedMember.getFcmToken())
                .filter(fcmToken -> !fcmToken.equals(existingMember.getFcmToken()))
                .ifPresent(fcmToken -> {
                    changes.append("FCM token changed from ")
                            .append(existingMember.getFcmToken() == null ? "unknown" : existingMember.getFcmToken())
                            .append(" to ")
                            .append(fcmToken)
                            .append(". ");
                });


        if (changes.length() == 0) {
            changes.append("No significant changes.");
        }

        return changes.toString();
    }


    public Page<MemberHistory> getMemberHistory(Long memberId, int page, int size) {
        Page<MemberHistory> histories = memberHistoryRepository.findByMemberId(memberId, PageRequest.of(page, size));
        return histories;
    }
}
