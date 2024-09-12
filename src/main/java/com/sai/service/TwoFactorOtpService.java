package com.sai.service;

import com.sai.modal.TwoFactorOTP;
import com.sai.modal.User;

public interface TwoFactorOtpService {
    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp,String opt);
    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
