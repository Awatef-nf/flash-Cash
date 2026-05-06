package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private Double amount;
    private String iban;

    public UserAccount plus(Double amount){
        this.amount += amount;
        return this;
    }

    public UserAccount minus(Double amount){
        this.amount -= amount;
        return this;
    }



}
