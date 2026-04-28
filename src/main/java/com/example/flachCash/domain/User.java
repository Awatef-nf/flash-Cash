package com.example.flachCash.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    //@ManyToMany
    //private List<Link> links;
}
