package com.sai.controller;

import com.sai.config.CustomeUserServiceImplementation;
import com.sai.config.JwtProvider;
import com.sai.dto.AuthResponse;
import com.sai.dto.LoginRequest;
import com.sai.exception.UserException;
import com.sai.modal.TwoFactorOTP;
import com.sai.modal.User;
import com.sai.repository.UserRepository;
import com.sai.service.EmailService;
import com.sai.service.TwoFactorOtpService;
import com.sai.service.UserServiceImpl;
import com.sai.util.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final  UserRepository userRepository;
    private  final CustomeUserServiceImplementation customUserDetails;

    private  final PasswordEncoder passwordEncoder;

    private final UserServiceImpl userService;

    private  final EmailService emailService;


    private final TwoFactorOtpService twoFactorOtpService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String mobile=user.getMobile();


        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist!=null) {

            throw new UserException("Email Is Already Used With Another Account");
        }

        // Create new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setMobile(mobile);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(createdUser);

       //  watchlistService.createWatchList(savedUser);
//		walletService.createWallet(user);


        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) throws UserException, MessagingException {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + " ----- " + password);

        Authentication authentication = authenticate(username, password);

        User user=userService.findUserByEmail(username);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two factor authentication enabled");
            authResponse.setTwoFactorAuthEnabled(true);

            String otp= OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOTP=twoFactorOtpService.findByUser(user.getId());
            if(oldTwoFactorOTP!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }


            TwoFactorOTP twoFactorOTP=twoFactorOtpService.createTwoFactorOtp(user,otp,token);

            emailService.sendverificationOtpEmail(user.getEmail(),otp);

            authResponse.setSession(twoFactorOTP.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }

        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

public ResponseEntity<AuthResponse> verifySignOtp(@PathVariable String otp,@RequestParam String id) throws Exception {

    TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

    if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
        AuthResponse response= new AuthResponse();
        response.setMessage("two factor authentication verified");
        response.setTwoFactorAuthEnabled(true);
        response.setJwt(twoFactorOTP.getJwt());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    throw new Exception("invalid otp");
}


}
