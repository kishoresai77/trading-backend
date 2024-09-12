package com.sai.service;

import com.sai.exception.WalletException;
import com.sai.modal.Order;
import com.sai.modal.User;
import com.sai.modal.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user) throws WalletException;

    public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException;

    public Wallet findWalletById(Long id) throws WalletException;

    public Wallet walletToWalletTransfer(User sender,Wallet receiverWallet, Long amount) throws WalletException;

   public Wallet payOrderPayment(Order order, User user) throws WalletException;

}
