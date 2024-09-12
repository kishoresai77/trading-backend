package com.sai.controller;

import com.sai.service.EmailService;
import com.sai.service.UserService;
import com.sai.service.VerficationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final VerficationService verificationService;
    private final UserService userService;


    private final EmailService emailService;






}