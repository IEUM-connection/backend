package com.springboot.utils;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Admin;
import com.springboot.member.repository.AdminRepository;
import org.springframework.security.core.Authentication;

public abstract class ExtractAdminCode {

    public Admin extractAdminFromAuthentication(Authentication authentication,
                                                   AdminRepository adminRepository)  {
        if(authentication == null){
            throw new BusinessLogicException(ExceptionCode.TOKEN_INVALID);
        }
        String adminCode = (String) authentication.getPrincipal();

        return adminRepository.findByAdminCode(adminCode)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GUARDIAN_NOT_FOUND));
    }
}
