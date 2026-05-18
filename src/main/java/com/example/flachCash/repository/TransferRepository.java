package com.example.flachCash.repository;

import com.example.flachCash.domain.Transfer;
import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Integer> {
    //on chercher par account_id: colonne jointure
    //soit en trouve user qui recois ou user qui envois mais on parle du memem compte, la transaction est envers le meme compte
    List<Transfer> findBySenderAccountOrReceiverAccount(
            UserAccount senderAccount,
            UserAccount receiverAccount
    );
}
