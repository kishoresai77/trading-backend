package com.sai.service;

import com.sai.modal.Withdrawal;
import com.sai.modal.User;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithdrawal(Long amount, User user);
    Withdrawal procedWithdrawal(Long withdrawalId,boolean accept) throws Exception;
    List<Withdrawal> getUsersWithdrawalHistory(User user);
    List<Withdrawal> getAllWithdrawalRequest();
}
