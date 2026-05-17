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
            throw new RuntimeException("Montant invalide");
        }

        if (!savedIbanRepository.existsByIban(iban)) {
            throw new RuntimeException("IBAN non autorisé");
        }

        User user = getCurrentUser();

        UserAccount account = user.getAccount();

        // 🔥 IMPORTANT : protection contre ton bug actuel
        if (account == null) {
            throw new RuntimeException("Compte utilisateur introuvable");
        }

        account.setAmount(account.getAmount() + amount);

        userAccountRepository.save(account);
    }



    public void internTransfer(String iban, Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Montant invalide");
        }

        if (!savedIbanRepository.existsByIban(iban)) {
            throw new RuntimeException("IBAN non autorisé");
        }

        User user = getCurrentUser();

        UserAccount account = user.getAccount();

        // 🔥 IMPORTANT : protection contre ton bug actuel
        if (account == null) {
            throw new RuntimeException("Compte utilisateur introuvable");

        }
        if (amount > account.getAmount()) {
            throw new RuntimeException("Solde insuffisant");
        }
        account.setAmount(account.getAmount() - amount);


        userAccountRepository.save(account);
    }


//==========================================================================================================
//  RECUPERER L USER CONNECTE

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }
//==========================================================================================================


}