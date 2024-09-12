package com.sai.service;

import com.sai.Domain.VerificationType;
import com.sai.modal.ForgotPasswordToken;
import com.sai.modal.User;
import com.sai.repository.ForgotPasswordRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
@NoArgsConstructor

public class ForgotPasswordServiceImpl implements ForgotPasswordService{

    private ForgotPasswordRepository repository;
    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotPasswordToken forgotPasswordToken= new ForgotPasswordToken();
        forgotPasswordToken.setId(id);
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setVerificationType(verificationType);
        forgotPasswordToken.setSendTo(sendTo);
        forgotPasswordToken.setOtp(otp);
        return repository.save(forgotPasswordToken);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> otp = repository.findById(id);
        return otp.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        repository.delete(token);

    }

    @Override
    public boolean verifyToken(ForgotPasswordToken token, String otp) {
        return token.getOtp().equals(otp);
    }
}
