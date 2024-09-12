package com.sai.controller;

import com.sai.dto.ApiResponse;
import com.sai.dto.ResetPasswordRequest;
import com.sai.dto.UpdatePasswordRequest;
import com.sai.Domain.VerificationType;
import com.sai.dto.AuthResponse;
import com.sai.modal.ForgotPasswordToken;
import com.sai.modal.TwoFactorOTP;
import com.sai.modal.User;
import com.sai.modal.VerificationCode;
import com.sai.service.EmailService;

import com.sai.service.ForgotPasswordService;
import com.sai.service.UserService;
import com.sai.service.VerficationService;
import com.sai.util.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final  UserService userService;
    private  final VerficationService verficationService;
    private  final EmailService emailService;
    private  final ForgotPasswordService forgotPasaswordService;


    @GetMapping("/users/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) {

        User user = userService.findUserProfileByJwt(jwt);
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/{usereId}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserById(id);
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enabledTwoFactorAuthentication(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp
    ) throws Exception {


        User user = userService.findUserProfileByJwt(jwt);


        VerificationCode verificationCode = verficationService.findUsersVerification(user);

        String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();


        boolean isVerified = verficationService.VerifyOtp(otp, verificationCode);

        if (isVerified) {
            User updatedUser = userService.enabledTwoFactorAuthentication(verificationCode.getVerificationType(),
                    sendTo,user);
            verficationService.deleteVerification(verificationCode);
            return ResponseEntity.ok(updatedUser);
        }
        throw new Exception("wrong otp");

    }



    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req
    ) throws Exception {
        ForgotPasswordToken forgotPasswordToken=forgotPasaswordService.findById(id);

        boolean isVerified = forgotPasaswordService.verifyToken(forgotPasswordToken,req.getOtp());

        if (isVerified) {

            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse apiResponse=new ApiResponse();
            apiResponse.setMessage("password updated successfully");
            return ResponseEntity.ok(apiResponse);
        }
        throw new Exception("wrong otp");

    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendUpdatePasswordOTP(
            @RequestBody UpdatePasswordRequest req)
            throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());
        String otp= OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasaswordService.findByUser(user.getId());

        if(token==null){
            token=forgotPasaswordService.createToken(
                    user,id,otp,req.getVerificationType(), req.getSendTo()
            );
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendverificationOtpEmail(
                    user.getEmail(),
                    token.getOtp()
            );
        }

        AuthResponse res=new AuthResponse();
        res.setSession(token.getId());
        res.setMessage("Password Reset OTP sent successfully.");

        return ResponseEntity.ok(res);

    }

    @PatchMapping("/api/users/verification/verify-otp/{otp}")
    public ResponseEntity<User> verifyOTP(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp
    ) throws Exception {


        User user = userService.findUserProfileByJwt(jwt);


        VerificationCode verificationCode = verficationService.findUsersVerification(user);


        boolean isVerified = verficationService.VerifyOtp(otp, verificationCode);

        if (isVerified) {
            verficationService.deleteVerification(verificationCode);
            User verifiedUser = userService.verifyUser(user);
            return ResponseEntity.ok(verifiedUser);
        }
        throw new Exception("wrong otp");

    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(
            @PathVariable VerificationType verificationType,
            @RequestHeader("Authorization") String jwt)
            throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verficationService.findUsersVerification(user);

        if(verificationCode == null) {
            verificationCode =verficationService.sendVerificationOTP(user,verificationType);
        }


        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendverificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }



        return ResponseEntity.ok("Verification OTP sent successfully.");

    }

}

