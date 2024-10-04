package com.springboot.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Collection;
import static com.springboot.member.entity.Member.MemberStatus.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true, nullable = false)
    private String memberCode;

    private String name;

    private String address;

    private String detailedAddress;

    private String postalCode;

    private String tel;

    @Column(nullable = false, unique = true)
    private String phone;

    @OneToOne
    @JoinColumn(name = "guardianId", unique = true)
    private Guardian guardian;

    private String emergencyContact;

    private int age;

    private String notes;

    private String relationship;

    private String medicalHistory;

    private int powerUsage;

    private int phoneInactiveDuration;

    @Column(nullable = false)
    private String role;

    private boolean milkDeliveryRequest;

    private String documentAttachment;

    private String residentNumber;

    @Lob
    private String adminNote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus = AWAITING_APPROVAL; // Enum 사용

    @Column(nullable = false)
    private LocalDate birthDate;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();
    }

    public enum MemberStatus {
        ACTIVE,      // 활성 상태
        AWAITING_APPROVAL,  //
        MEMBER_QUIT, // 탈퇴한 회원
        SUSPENDED    // 정지된 회원
    }


}
