package com.springboot.member.service;


import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Guardian;
import com.springboot.member.repository.GuardianRepository;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.springboot.member.entity.Guardian.GuardianStatus.*;

@Service
public class GuardianService {

    private final PasswordEncoder passwordEncoder;
    private final GuardianRepository guardianRepository;
    private final ApplicationEventPublisher publisher;

    public GuardianService(GuardianRepository guardianRepository, ApplicationEventPublisher publisher, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.guardianRepository = guardianRepository;
        this.publisher = publisher;
    }

    public Guardian getGuardian(Long guardianId) {
        return guardianRepository.findById(guardianId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    public Guardian createGuardian(Guardian guardian) {
        verifyExistsEmail(guardian.getEmail());
        String encryptedPassword = passwordEncoder.encode(guardian.getPassword());
        guardian.setPassword(encryptedPassword);
        guardian.setRole("ROLE_GUARDIAN"); // 고정된 역할로 설정
        Guardian savedGuardian = guardianRepository.save(guardian);

        //publisher.publishEvent(new GuardianRegistrationApplicationEvent(this, savedGuardian));
        return savedGuardian;
    }

    public Guardian updateGuardian(Long guardianId, Guardian guardian) {
        Guardian findGuardian = findVerifiedGuardian(guardianId);
        boolean updated = false;

        // Optional 필드 업데이트 (업데이트 시 updated 플래그를 true로 설정)
        if (Optional.ofNullable(guardian.getName()).isPresent()) {
            findGuardian.setName(guardian.getName());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getTel()).isPresent()) {
            findGuardian.setTel(guardian.getTel());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getPhone()).isPresent()) {
            findGuardian.setPhone(guardian.getPhone());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getAddress()).isPresent()) {
            findGuardian.setAddress(guardian.getAddress());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getDetailedAddress()).isPresent()) {
            findGuardian.setDetailedAddress(guardian.getDetailedAddress());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getPostalCode()).isPresent()) {
            findGuardian.setPostalCode(guardian.getPostalCode());
            updated = true;
        }

        if (Optional.ofNullable(guardian.getGuardianStatus()).isPresent()) {
            findGuardian.setGuardianStatus(guardian.getGuardianStatus());
            updated = true;
        }

        // 만약 업데이트된 필드가 없다면 예외 발생
        if (!updated) {
            throw new BusinessLogicException(ExceptionCode.NO_UPDATABLE_FIELDS);
        }

        return guardianRepository.save(findGuardian);
    }

    public void quitGuardian(long guardianId) {
        Guardian findGuardian = findVerifiedGuardian(guardianId);

        if (findGuardian.getGuardianStatus() != GUARDIAN_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_GUARDIAN_STATUS);
        }
        findGuardian.setGuardianStatus(GUARDIAN_QUIT);
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

        public boolean isEmailDuplicate(String email) {
            return !guardianRepository.existsByEmail(email);
        }

}


