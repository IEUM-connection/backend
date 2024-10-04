package com.springboot.member.service;


import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.repository.GuardianRepository;
import com.springboot.security.utils.JwtAuthorityUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.springboot.member.entity.Guardian.GuardianStatus.*;

@Service
public class GuardianService {


    private final PasswordEncoder passwordEncoder;
    private final GuardianRepository guardianRepository;
    private final ApplicationEventPublisher publisher;
    private final JwtAuthorityUtils authorityUtils;


    public GuardianService(PasswordEncoder passwordEncoder, GuardianRepository guardianRepository, ApplicationEventPublisher publisher, JwtAuthorityUtils authorityUtils) {
        this.passwordEncoder = passwordEncoder;
        this.guardianRepository = guardianRepository;
        this.publisher = publisher;
        this.authorityUtils = authorityUtils;
    }

    public Guardian verifyGuardianCredentials(String email, String password) {
        Guardian guardian = new Guardian();
        return guardian;
    }

    public Guardian createGuardian(Guardian guardian) {
        // TODO should business logic


        verifyExistsEmail(guardian.getEmail());


        String encryptedPassword = passwordEncoder.encode(guardian.getPassword());
        guardian.setPassword(encryptedPassword);


        String role = authorityUtils.createRole(guardian.getEmail());
        guardian.setRole(role);

        Guardian savedGuardian = guardianRepository.save(guardian);

      //  publisher.publishEvent(new GuardianRegistrationApplicationEvent(this, savedGuardian));
        return savedGuardian;
    }

    public Guardian updateGuardian(Guardian guardian) {
        // TODO should business logic

        Guardian findGuardian = findVerifiedGuardian(guardian.getGuardianId());

        if (guardian.getName() != null) {
            findGuardian.setName(guardian.getName());
        }
        if (guardian.getTel() != null) {
            findGuardian.setTel(guardian.getTel());
        }
        if (guardian.getPhone() != null) {
            findGuardian.setPhone(guardian.getPhone());
        }
        Optional.ofNullable(guardian.getAddress())
                .ifPresent(address -> findGuardian.setAddress(address));

        return guardianRepository.save(findGuardian);
    }

    public Guardian updateGuardianPassword(Guardian guardian) {
        // TODO should business logic

        Guardian findGuardian = findVerifiedGuardian(guardian.getGuardianId());

        Optional.ofNullable(guardian.getPassword())
                .ifPresent(password -> findGuardian.setPassword(password));
        return guardianRepository.save(findGuardian);
    }



    public Guardian findGuardian(long guardianId) {
        // TODO should business logic

        return findVerifiedGuardian(guardianId);
    }

    public Guardian findGuardian(long guardianId, String email) {
        // TODO should business logic

        return findVerifiedGuardian(guardianId);
    }


    public Page<Guardian> findGuardians(int page, int size) {
        // TODO should business logic

        return guardianRepository.findAll(PageRequest.of(page, size, Sort.by("guardianId").descending()));

    }

    public void deleteGuardian(long guardianId) {
        // TODO should business logic
        Guardian findGuardian = findVerifiedGuardian(guardianId);
        findGuardian.setGuardianStatus(GUARDIAN_QUIT);
// 가디언이랑 연결된 서비스 > 사용자도 지워져야하는가
        guardianRepository.save(findGuardian);
        //throw new BusinessLogicException(ExceptionCode.NOT_IMPLEMENTATION);
    }

    public void quitGuardian(long guardianId) {
        Guardian findGuardian = findVerifiedGuardian(guardianId);

        if (findGuardian.getGuardianStatus() != GUARDIAN_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_GUARDIAN_STATUS);
        }
        findGuardian.setGuardianStatus(Guardian.GuardianStatus.GUARDIAN_QUIT);
        guardianRepository.save(findGuardian);


    }


    public void sleepGuardian(long guardianId) {
        Guardian findGuardian = findVerifiedGuardian(guardianId);

        if (findGuardian.getGuardianStatus() != GUARDIAN_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_GUARDIAN_STATUS);
        }
        findGuardian.setGuardianStatus(Guardian.GuardianStatus.GUARDIAN_SLEEP);
        guardianRepository.save(findGuardian);
    }


    public Guardian findVerifiedGuardian(Long guardianId) {
        Optional<Guardian> optionalGuardian = guardianRepository.findById(guardianId);
        return optionalGuardian.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.GUARDIAN_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {
        Optional<Guardian> guardian = guardianRepository.findByEmail(email);
        if (guardian.isPresent()) throw new BusinessLogicException(ExceptionCode.GUARDIAN_EXISTS);
    }


    @Transactional(readOnly = true)
    public Guardian findVerifiedGuardian(String email) {
        Optional<Guardian> optionalGuardian = guardianRepository.findByEmail(email);
        Guardian findGuardian = optionalGuardian.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.GUARDIAN_NOT_FOUND));
        return findGuardian;
    }

    public boolean isEmailDuplicate(String email) {
        return !guardianRepository.existsByEmail(email);
    }
}


