package com.sai.service;

import com.sai.Domain.VerificationType;
import com.sai.config.JwtProvider;
import com.sai.exception.UserException;
import com.sai.modal.TwoFactorAuth;
import com.sai.modal.User;
import com.sai.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private  final UserRepository userRepository;

    public User findUserByEmail(String username) throws UserException {
        User user = userRepository.findByEmail(username);
          if(user!=null){
              return user;
          }
          throw new UserException("user not exist username"+username);
    }

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user= userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("user not exist with email"+ email);
        }
        return user;

    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserException("user not exist with id"+userId);
        }
        return user.get();
    }

    @Override
    public User verifyUser(User user) {

        return null;
    }

    @Override
    public User enabledTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {

        TwoFactorAuth twoFactorAuth= new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }
    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    @Override
    public void sendUpdatePasswordOtp(String email, String otp) {

    }


}
