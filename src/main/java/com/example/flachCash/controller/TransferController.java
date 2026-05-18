package com.example.flachCash.controller;

import com.example.flachCash.domain.Role;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.TransferRepository;
import com.example.flachCash.repository.UserAccountRepository;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.TransferService;
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
public class TransferController {

    private final TransferService transferService;
    private final UserAccountService userAccountService;

    @GetMapping("/transfer")
    public String showTransferPage(Model model) {

        User currentUser = userAccountService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("accounts", transferService.findAllExceptCurrentUserAndAdmin(currentUser.getEmail(), Role.ADMIN));

        return "transfer";
    }
    @PostMapping("/transfer")
    public String transferMoney(@RequestParam String receiverEmail,
                                @RequestParam Double amount,
                                RedirectAttributes redirectAttributes) {
        try {
            transferService.transferMoney(receiverEmail, amount);
            redirectAttributes.addFlashAttribute("success", "Virement effectué !");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/transfer";
        }
        return "redirect:/profile";
    }
}
