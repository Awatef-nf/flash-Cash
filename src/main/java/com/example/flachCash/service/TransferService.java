package com.example.flachCash.service;

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

    private UserAccountRepository userAccountRepository;
    private final TransferRepository transferRepository;


   

    @Transactional
    public void transferMoney(String receiverEmail,
                              Double amount) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String senderEmail = authentication.getName();

        // compte sender
        UserAccount senderAccount = (UserAccount) userAccountRepository
                        .findByUserEmail(senderEmail)
                        .orElseThrow(() ->
                                new RuntimeException("Sender not found"));

        // compte receiver
        UserAccount receiverAccount = (UserAccount) userAccountRepository
                        .findByUserEmail(receiverEmail).orElseThrow(() ->
                                new RuntimeException("Receiver not found"));

        // validation montant
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // empêcher transfert à soi-même
        if (senderEmail.equals(receiverEmail)) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // vérifier solde
        if (senderAccount.getAmount() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // frais
        double fee = amount * 0.05;

        double amountAfterFee = amount - fee;

        // retirer argent sender
        senderAccount.setAmount(
                senderAccount.getAmount() - amount
        );

        // ajouter argent receiver
        receiverAccount.setAmount(
                receiverAccount.getAmount() + amountAfterFee
        );

        // save
        userAccountRepository.save(senderAccount);
        userAccountRepository.save(receiverAccount);

        // historique
        Transfer transfer = new Transfer();

        transfer.setDate(LocalDateTime.now());

        transfer.setSenderAccount(senderAccount);

        transfer.setReceiverAccount(receiverAccount);

        transfer.setAccountBeforeFee(amount);

        transfer.setAccountAfterFee(amountAfterFee);

        transferRepository.save(transfer);
    }


    public List<Transfer> findAll() {
       return transferRepository.findAll();
    }


}




