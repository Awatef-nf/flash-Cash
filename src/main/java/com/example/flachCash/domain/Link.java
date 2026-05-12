package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    User userTo;

    @ManyToOne
    User userFrom;
}