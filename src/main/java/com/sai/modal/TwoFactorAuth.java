package com.sai.modal;

import com.sai.Domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled= false;
    private VerificationType sendTo;
}
