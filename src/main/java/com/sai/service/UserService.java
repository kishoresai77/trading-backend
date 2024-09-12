package com.sai.service;

import com.sai.Domain.VerificationType;
import com.sai.modal.User;

public interface UserService {

    public User findUserByEmail(String email);
    public User findUserProfileByJwt(String jwt);
    public User findUserById(Long  userId);
    public User verifyUser(User user);
    public User enabledTwoFactorAuthentication(VerificationType verificationType,String SendTo,User user);

    User updatePassword(User user,String newPassword);
    void sendUpdatePasswordOtp(String email,String otp);





}
