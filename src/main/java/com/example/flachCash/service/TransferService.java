package com.example.flachCash.service;

import com.example.flachCash.domain.Role;
import com.example.flachCash.domain.Transfer;
import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.TransferRepository;
import com.example.flachCash.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransferService {

    private final UserAccountService userAccountService;
    private UserAccountRepository userAccountRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public void transferMoney(String receiverEmail,
                              Double amount) {

        String senderEmail = userAccountService.getCurrentUser().getEmail();
        // sender account
        UserAccount senderAccount = (UserAccount) userAccountRepository
                        .findByUserEmail(senderEmail)
                        .orElseThrow(() ->
                                new RuntimeException("Sender not found"));

        // receiver account
        UserAccount receiverAccount = (UserAccount) userAccountRepository
                        .findByUserEmail(receiverEmail).orElseThrow(() ->
                                new RuntimeException("Receiver not found"));

        // amount validation
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // diable to sender to the owner
        if (senderAccount.getAccountId().equals(receiverAccount.getAccountId())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // balance verification
        if (senderAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // fee of transfer
        double fee = amount * 0.05;

        double amountAfterFee = amount - fee;

        // remove amount from sender account
        senderAccount.setBalance(
                senderAccount.getBalance() - amount
        );

        // add amount to receiver account
        receiverAccount.setBalance(
                receiverAccount.getBalance() + amountAfterFee
        );

        // save
        userAccountRepository.save(senderAccount);
        userAccountRepository.save(receiverAccount);

        // historical
        Transfer transfer = new Transfer();

        transfer.setDate(LocalDateTime.now());

        transfer.setSenderAccount(senderAccount);

        transfer.setReceiverAccount(receiverAccount);

        transfer.setAmountBeforeFee(amount);

        transfer.setAmountAfterFee(amountAfterFee);

        transferRepository.save(transfer);
    }


    public List<Transfer> findByUser(User user) {
        UserAccount account = user.getAccount();
        return transferRepository.findBySenderAccountOrReceiverAccount(
                account,
                account
        );
    }

    public Object findAllExceptCurrentUserAndAdmin(String email, Role role) {
        return userAccountRepository.findByUserEmailNotAndUserRoleNot(email,role);
    }
}




