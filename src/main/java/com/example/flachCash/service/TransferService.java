package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {


    private final TransferRepository transferRepository;



    public void addCash(Double amount){

    }

}
