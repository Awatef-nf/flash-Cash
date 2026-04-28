package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class UserAcount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    private Double amount;
    private String iban;

    public UserAcount plus(Double amount){
        this.amount += amount;
        return this;
    }

    public UserAcount minus(Double amount){
        this.amount -= amount;
        return this;
    }



}
