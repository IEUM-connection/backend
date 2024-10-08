package com.springboot.security.userdetails;

import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Guardian;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.AdminRepository;
import com.springboot.member.repository.GuardianRepository;
import com.springboot.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final GuardianRepository guardianRepository;  // Guardian 데이터베이스 레포지토리
    private final AdminRepository adminRepository;  // Admin 데이터베이스 레포지토리
    private final MemberRepository memberRepository;  // Member 데이터베이스 레포지토리


    public CustomUserDetailsService(GuardianRepository guardianRepository, AdminRepository adminRepository, MemberRepository memberRepository) {
        this.guardianRepository = guardianRepository;
        this.adminRepository = adminRepository;
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Guardian 테이블에서 사용자를 조회
        Optional<Guardian> guardian = guardianRepository.findByEmail(username);
        if (guardian.isPresent()) {
            return new UserDetailsImpl(guardian.get());  // Guardian이 존재하면 반환
        }

        // Admin 테이블에서 사용자를 조회
        Optional<Admin> admin = adminRepository.findByAdminCode(username);
        if (admin.isPresent()) {
            return new AdminDetails(admin.get());  // Admin이 존재하면 반환
        }

        // Member 테이블에서 사용자를 조회
        Optional<Member> member = memberRepository.findByMemberCode(username);
        if (member.isPresent()) {
            return new MemberDetails(member.get());  // Member가 존재하면 반환
        }

        // 사용자를 찾지 못한 경우 예외 발생
        throw new UsernameNotFoundException("User not found with username: " + username);
    }



    public UserDetails loadUserByUsernameAndRole(String username, String role) throws UsernameNotFoundException {
        switch (role) {
            case "guardian":
                // Guardian 테이블에서 사용자 조회
                Optional<Guardian> guardian = guardianRepository.findByEmail(username);
                if (guardian.isPresent()) {
                    return new UserDetailsImpl(guardian.get());  // Guardian 정보 반환
                }
                break;

            case "admin":
                // Admin 테이블에서 사용자 조회
                Optional<Admin> admin = adminRepository.findByAdminCode(username);
                if (admin.isPresent()) {
                    return new AdminDetails(admin.get());  // Admin 정보 반환
                }
                break;

            case "member":
                // Member 테이블에서 사용자 조회
                Optional<Member> member = memberRepository.findByMemberCode(username);
                if (member.isPresent()) {
                    return new MemberDetails(member.get());  // Member 정보 반환
                }
                break;

            default:
                // 유효하지 않은 역할(role)일 경우 예외 발생
                throw new UsernameNotFoundException("Invalid user role: " + role);
        }

        // 해당 역할과 사용자명을 기반으로 사용자 정보를 찾지 못한 경우 예외 발생
        throw new UsernameNotFoundException("User not found with username: " + username + " and role: " + role);
    }
}