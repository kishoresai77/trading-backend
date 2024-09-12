package com.sai.service;

import com.sai.Domain.OrderType;
import com.sai.exception.WalletException;
import com.sai.modal.Order;
import com.sai.modal.User;
import com.sai.modal.Wallet;
import com.sai.modal.WalletTransaction;
import com.sai.repository.WalletRepository;
import com.sai.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{
    public final WalletRepository walletRepository;
    public final WalletTransactionRepository walletTransactionRepository;

    public Wallet generateWallet(User user){
        Wallet wallet= new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }
    @Override
    public Wallet getUserWallet(User user) throws WalletException {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet != null){
            return wallet;
        }
        return generateWallet(user);
    }

    @Override
    public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException {

        BigDecimal newBalance = wallet.getBalance().add(BigDecimal.valueOf(money));

//        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
//            throw new Exception("Insufficient funds for this transaction.");
//        }


        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(money)));

        walletRepository.save(wallet);
        System.out.println("updated wallet - "+wallet);
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws WalletException {
        Optional<Wallet> wallet = walletRepository.findById(id);
               if(wallet==null){
                   throw  new WalletException("Didn't find the wallet with id "+id);
               }
        return wallet.get();
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException {
        Wallet senderWallet = getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
            throw new WalletException("Insuffient balance");
        }
        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
             senderWallet.setBalance(senderBalance);
             walletRepository.save(senderWallet);
        BigDecimal receiverBalance = receiverWallet.getBalance();
        receiverBalance = receiverBalance.add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);

        return senderWallet;

    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws WalletException {
        Wallet wallet = getUserWallet(user);

        WalletTransaction walletTransaction=new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setPurpose(order.getOrderType()+ " " + order.getOrderItem().getCoin().getId() );

        walletTransaction.setDate(LocalDate.now());
        walletTransaction.setTransferId(order.getOrderItem().getCoin().getSymbol());


        if(order.getOrderType().equals(OrderType.BUY)){
//            walletTransaction.setType(WalletTransactionType.BUY_ASSET);
            walletTransaction.setAmount(-order.getPrice().longValue());
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if (newBalance.compareTo(order.getPrice())<0) {
                System.out.println("inside");
                throw new WalletException("Insufficient funds for this transaction.");
            }
            System.out.println("outside---------- ");
            wallet.setBalance(newBalance);
        }
        else if(order.getOrderType().equals(OrderType.SELL)){
//            walletTransaction.setType(WalletTransactionType.SELL_ASSET);
            walletTransaction.setAmount(order.getPrice().longValue());
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }


//        System.out.println("wallet balance "+wallet+"-------"+order.getPrice());
        walletTransactionRepository.save(walletTransaction);
        walletRepository.save(wallet);
        return wallet;

    }
}
