package com.sai.service;

import com.sai.exception.UserException;
import com.sai.modal.User;
import com.sai.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    public UserServiceimpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findUserByEmail(String username) throws UserException {
        User user = userRepository.findByEmail(username);
          if(user!=null){
              return user;
          }
          throw new UserException("user not exist username"+username);

    }



}
