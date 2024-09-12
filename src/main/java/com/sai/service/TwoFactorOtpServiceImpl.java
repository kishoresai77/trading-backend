package com.sai.service;

import com.sai.modal.TwoFactorOTP;
import com.sai.modal.User;
import com.sai.repository.TwoFactorOtpRepository;
import com.sai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor

public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{
    private final TwoFactorOtpRepository twoFactorOtpRepository;

//    public TwoFactorOtpServiceImpl(TwoFactorOtpRepository twoFactorOtpRepository) {
//        this.twoFactorOtpRepository = twoFactorOtpRepository;
//    }

    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid= UUID.randomUUID();
        String id= uuid.toString();
        TwoFactorOTP twoFactorOTP= new TwoFactorOTP();
        twoFactorOTP.setId(id);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setJwt(jwt);
        twoFactorOTP.setOpt(otp);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {

       return  twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> otp = twoFactorOtpRepository.findById(id);

        return otp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String opt) {
        return twoFactorOtp.getOpt().equals(opt);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);

    }
}
