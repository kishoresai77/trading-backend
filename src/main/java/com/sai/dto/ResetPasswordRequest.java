package com.sai.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String password;
    private String otp;
}
