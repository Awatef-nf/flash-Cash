package com.example.flachCash.service;

import com.example.flachCash.domain.Role;
import com.example.flachCash.domain.Transfer;
import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.TransferRepository;
import com.example.flachCash.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;
    private final TransferRepository transferRepository;

    private static final BigDecimal FEE_RATE = new BigDecimal("0.005"); // 0.5 %


    @Transactional
    public void transferMoney(String receiverEmail, BigDecimal amount) {

        String senderEmail = userAccountService.getCurrentUser().getEmail();

        // sender account
        UserAccount senderAccount = (UserAccount) userAccountRepository
                .findByUserEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // receiver account
        UserAccount receiverAccount = (UserAccount) userAccountRepository
                .findByUserEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // amount validation
        BigDecimal totalDebit = getBigDecimal(amount, senderAccount, receiverAccount);

        // remove amount + fee from sender, add the full amount to receiver
        senderAccount.setBalance(senderAccount.getBalance().subtract(totalDebit));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        userAccountRepository.save(senderAccount);
        userAccountRepository.save(receiverAccount);

        // history
        Transfer transfer = new Transfer();
        transfer.setDate(LocalDateTime.now());
        transfer.setSenderAccount(senderAccount);
        transfer.setReceiverAccount(receiverAccount);
        transfer.setAmountBeforeFee(amount);
        transfer.setAmountAfterFee(totalDebit);
        transferRepository.save(transfer);
    }

    private static @NotNull BigDecimal getBigDecimal(BigDecimal amount, UserAccount senderAccount, UserAccount receiverAccount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // forbid transfer to yourself
        if (senderAccount.getAccountId().equals(receiverAccount.getAccountId())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // fee of transfer (0.5 %), rounded to the cent
        BigDecimal fee = amount.multiply(FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDebit = amount.add(fee);

        // balance verification (amount + fee)
        if (senderAccount.getBalance().compareTo(totalDebit) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        return totalDebit;
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




