package com.springboot.security.userdetails;

import com.springboot.member.entity.Admin;
import com.springboot.member.entity.member;
import com.springboot.member.repository.AdminRepository;
import com.springboot.member.repository.memberRepository;
import org.springframework.security.core.memberdetails.memberDetails;
import org.springframework.security.core.memberdetails.memberDetailsService;
import org.springframework.security.core.memberdetails.membernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustommemberDetailsService implements memberDetailsService {

    private final memberRepository memberRepository;
    private final AdminRepository adminRepository;

    public CustommemberDetailsService(memberRepository memberRepository, AdminRepository adminRepository) {
        this.memberRepository = memberRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public memberDetails loadmemberBymembername(String membername) throws membernameNotFoundException {
        // 먼저 member 테이블에서 찾음
        Optional<member> member = memberRepository.findBymembername(membername);
        if (member.isPresent()) {
            return new memberDetailsImpl(member.get());
        }

        // member에 없으면 Admin 테이블에서 찾음
        Optional<Admin> admin = adminRepository.findByAdminmembername(membername);
        if (admin.isPresent()) {
            return new AdminDetailsImpl(admin.get());
        }

        throw new membernameNotFoundException("member not found with membername: " + membername);
    }
}
