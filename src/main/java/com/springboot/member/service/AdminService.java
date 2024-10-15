package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Admin;
import com.springboot.member.entity.Guardian;
import com.springboot.member.repository.AdminRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.springboot.member.entity.Admin.AdminStatus.*;

@Service
public class AdminService {


    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Admin updateAdmin(Admin admin) {

        Admin findadmin = findVerifiedAdmin(admin.getAdminCode());

        return adminRepository.save(findadmin);
    }

    public Admin updateAdminPassword(Admin admin) {
        Admin findAdmin = findVerifiedAdmin(admin.getAdminCode());

        // 새 비밀번호를 암호화한 후 저장
        Optional.ofNullable(admin.getPassword())
                .ifPresent(password -> findAdmin.setPassword(passwordEncoder.encode(password)));

        return adminRepository.save(findAdmin);
    }


    public Admin findAdmin(String adminCode) {

        return findVerifiedAdmin(adminCode);
    }

    public Admin findAdmin(String adminCode, String email) {

        return findVerifiedAdmin(adminCode);
    }


    public Page<Admin> findAdmins(int page, int size) {

        return adminRepository.findAll(PageRequest.of(page, size, Sort.by("adminId").descending()));

    }

    // 사번 넣어야함
    public void deleteAdmin(String adminCode) {

        Admin findAdmin = findVerifiedAdmin(adminCode);
        findAdmin.setAdminStatus(ADMIN_QUIT);
// 가디언이랑 연결된 서비스 > 사용자도 지워져야하는가
        adminRepository.save(findAdmin);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public void quitAdmin(String adminCode) {
        Admin findAdmin = findVerifiedAdmin(adminCode);

        if (findAdmin.getAdminStatus() != ADMIN_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ADMIN_STATUS);
        }
        findAdmin.setAdminStatus(Admin.AdminStatus.ADMIN_QUIT);
        adminRepository.save(findAdmin);


    }


    public void sleepAdmin(String adminCode) {
        Admin findadmin = findVerifiedAdmin(adminCode);

        if (findadmin.getAdminStatus() != ADMIN_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ADMIN_STATUS);
        }
        findadmin.setAdminStatus(Admin.AdminStatus.ADMIN_SLEEP);
        adminRepository.save(findadmin);
    }


    public Admin findVerifiedAdmin(String adminCode) {
        Optional<Admin> optionaladmin = adminRepository.findByAdminCode(adminCode);
        return optionaladmin.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.ADMIN_NOT_FOUND));
    }


    public void verifyPassword(String adminCode, String password) {
        Admin admin = findVerifiedAdmin(adminCode);

        if (!passwordEncoder.matches(password, admin.getPassword())) {

            throw new BusinessLogicException(ExceptionCode.CONFIRM_PASSWORD_MISMATCH);
        }
    }

    public Admin updatePassword(Admin admin) {

        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_WRONG);
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }


    public Admin findAdminByLocation(String address) {
        String[] parts = address.split(" ");
        String district = null;

        for (String part : parts) {
            if (part.endsWith("구")) {
                district = part;
                break;
            }
        }

        // "구"가 없으면 예외 발생
        if (district == null) {
            throw new BusinessLogicException(ExceptionCode.INVALID_LOCATION); // 구가 없는 경우
        }

        // "구" 단위를 사용해 관리자를 검색
        Optional<Admin> admin = adminRepository.findByLocationContaining(district);
        if (admin.isPresent()) {
            return admin  .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADMIN_NOT_FOUND));
        } else {
            throw new BusinessLogicException(ExceptionCode.ADMIN_NOT_FOUND);
        }

    }


}
