package com.example.flachCash.controller;

import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.TransferService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class TransferController {

    private final TransferService trandferService;
    private final UserService userService;
    private final SavedIbanService savedIbanService;

    @GetMapping("/addCash")
    public String showTransferPage(Model model, Authentication authentication) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedIban> savedIbans = savedIbanService.showIbans(user.getId());

        model.addAttribute("savedIbans", savedIbans);

        return "addCash";
    }


}
