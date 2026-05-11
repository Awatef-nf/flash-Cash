package com.example.flachCash.controller;

import com.example.flachCash.service.TrandferService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class TransferController {

    private final TrandferService trandferService;

    @GetMapping("/transfer")
    public



}
