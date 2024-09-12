package com.sai.service;

import com.sai.modal.PaymentDetails;
import com.sai.modal.User;
import com.sai.repository.PaymentDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails  addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user
    ) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
         paymentDetailsRepository.save(paymentDetails);
         return paymentDetails;
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(User user) {
        return paymentDetailsRepository.getPaymentDetailsByUserId(user.getId());
    }
}