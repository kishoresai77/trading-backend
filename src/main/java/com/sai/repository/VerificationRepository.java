package com.sai.repository;

import com.sai.modal.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationCode,Long> {

    VerificationCode findByUserId(Long id);
}
