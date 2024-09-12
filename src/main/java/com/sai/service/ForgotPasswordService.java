package com.sai.service;

import com.sai.Domain.VerificationType;
import com.sai.modal.ForgotPasswordToken;
import com.sai.modal.User;
public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp,
                                    VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

    boolean verifyToken(ForgotPasswordToken token,String otp);


}
