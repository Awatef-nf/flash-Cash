package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;
    private BigDecimal balance;
    //l'autre coté
    @OneToOne(mappedBy = "account")
    private User user;
    //Pour pouvoir supprimer un user il faut aussi supprimer les autres liens transfers
    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfer> sentTransfers;

    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfer> receivedTransfers;

}