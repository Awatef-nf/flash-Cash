package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.SavedIbanRepository;
import com.example.flachCash.repository.TransferRepository;
import com.example.flachCash.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAccountService {

    private final SavedIbanRepository savedIbanRepository;
    private final UserAccountRepository userAccountRepository;

    public void addCash(String iban, Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (!savedIbanRepository.existsByIban(iban)) {
            throw new RuntimeException("IBAN not authorized");
        }

        User user = getCurrentUser();
        UserAccount account = user.getAccount();

        if (account == null) {
            throw new RuntimeException("User Account not found");
        }

        account.setBalance(account.getBalance() + amount);
        userAccountRepository.save(account);
    }


    public void personalTransfer(String iban, Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (!savedIbanRepository.existsByIban(iban)) {
            throw new RuntimeException("IBAN not authorized");
        }

        User user = getCurrentUser();
        UserAccount account = user.getAccount();

        if (account == null) {
            throw new RuntimeException("User Account not found");

        }
        if (amount > account.getBalance()) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        userAccountRepository.save(account);
    }


//==========================================================================================================
//  recuperate User Connected !!!

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }
//==========================================================================================================


}