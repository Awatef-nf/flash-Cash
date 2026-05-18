package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private LocalDateTime date;

    @ManyToOne
    private UserAccount senderAccount;

    @ManyToOne
    private UserAccount receiverAccount;

    private Double amountBeforeFee;
    private Double amountAfterFee;



}
