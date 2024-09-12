package com.sai.service;

import com.sai.modal.PaymentDetails;
import com.sai.modal.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails( String accountNumber,
                                             String accountHolderName,
                                             String ifsc,
                                             String bankName,
                                             User user
    );

    public PaymentDetails getUsersPaymentDetails(User user);


}