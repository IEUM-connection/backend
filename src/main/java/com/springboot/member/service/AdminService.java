package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Admin;
import com.springboot.member.repository.AdminRepository;
import com.springboot.security.utils.JwtAuthorityUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.Optional;

import static com.springboot.member.entity.Admin.AdminStatus.*;


public class AdminService {
    
    public Admin verifyAdminCredentials(String employeeCd, String password) {
        Admin admin = new Admin();
        return admin;
    }

    private final AdminRepository adminRepository;
    private final ApplicationEventPublisher publisher;
    private final JwtAuthorityUtils authorityUtils;
    
    public AdminService(AdminRepository adminRepository, ApplicationEventPublisher publisher, JwtAuthorityUtils authorityUtils) {
        this.adminRepository = adminRepository;
        this.publisher = publisher;
        this.authorityUtils = authorityUtils;
    }

    public Admin verifyadminCredentials(String email, String password) {
        Admin admin = new Admin();
        return admin;
    }


    public Admin updateAdmin(Admin admin) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Admin findadmin = findVerifiedAdmin(admin.getAdminCode());

        return adminRepository.save(findadmin);
    }

    public Admin updateAdminPassword(Admin admin) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        Admin findAdmin = findVerifiedAdmin(admin.getAdminCode());

        Optional.ofNullable(admin.getPassword())
                .ifPresent(password -> findAdmin.setPassword(password));
        return adminRepository.save(findAdmin);
    }



    public Admin findAdmin(String adminCode) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        return findVerifiedAdmin(adminCode);
    }

    public Admin findAdmin(String adminCode, String email) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        return findVerifiedAdmin(adminCode);
    }


    public Page<Admin> findAdmins(int page, int size) {
        // TODO should business logic
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
        return adminRepository.findAll(PageRequest.of(page, size, Sort.by("adminId").descending()));

    }
// 사번 넣어야함
    public void deleteAdmin(String adminCode) {
        // TODO should business logic
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



}
