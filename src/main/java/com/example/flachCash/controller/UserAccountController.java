package com.example.flachCash.controller;

import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.UserAccountService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserAccountController {


    private final UserService userService;
    private final SavedIbanService savedIbanService;
    private final UserAccountService userAccountService;


    @GetMapping("/addCash")
    public String showTransferPage(Model model, Authentication authentication) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedIban> savedIbans = savedIbanService.showIbans(user.getId());

        model.addAttribute("savedIbans", savedIbans);

        return "addCash";
    }


    @PostMapping("/addCash")
    public String addCash(
            @RequestParam String iban,
            @RequestParam Double amount
    ) {

        userAccountService.addCash(iban, amount);
        return "redirect:/profile";
    }

    @GetMapping("/personalTransfer")
    public String showPersonalTransferPage(Model model, Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedIban> savedIbans = savedIbanService.showIbans(user.getId());
        model.addAttribute("savedIbans", savedIbans);

        return "personalTransfer";
    }


    @PostMapping("/personalTransfer")
    public String personalTransfer(
            @RequestParam String iban,
            @RequestParam Double amount,
            RedirectAttributes redirectAttributes) {
        try {

            userAccountService.personalTransfer(iban, amount);
            redirectAttributes.addFlashAttribute("success", "Virement effectué !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/personalTransfer";
        }
        return "redirect:/profile";
    }







}