package com.springboot.utils;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.repository.GuardianRepository;
import org.springframework.security.core.Authentication;

public abstract class ExtractGuardianEmail {

    public Guardian extractGuardianFromAuthentication(Authentication authentication,
                                                    GuardianRepository guardianRepository)  {
        if(authentication == null){
            throw new BusinessLogicException(ExceptionCode.TOKEN_INVALID);
        }
        String email = (String) authentication.getPrincipal();

        return guardianRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GUARDIAN_NOT_FOUND));
    }
}
