package com.sai.service;

import com.sai.Domain.VerificationType;
import com.sai.modal.User;
import com.sai.modal.VerificationCode;

public interface VerficationService {
    VerificationCode sendVerificationOTP(User user, VerificationType verificationType);

    VerificationCode findVerificationById(Long id) throws Exception;

    VerificationCode findUsersVerification(User user) throws Exception;

    Boolean VerifyOtp(String opt, VerificationCode verificationCode);

    void deleteVerification(VerificationCode verificationCode);
}
