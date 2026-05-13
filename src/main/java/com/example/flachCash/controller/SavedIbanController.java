package com.example.flachCash.controller;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@AllArgsConstructor
public class SavedIbanController {

    private final SavedIbanService savedIbanService;
    private final UserService userService;


    @GetMapping("/addIban")
    public String showAddIban(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "addIban";
    }

    @PostMapping("/addIban")
    public String savedIban(@RequestParam String iban,
                            @RequestParam String bankName,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            savedIbanService.addIban(iban, bankName, user);
            redirectAttributes.addFlashAttribute("success", "IBAN added successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/showIban";
    }
    @PostMapping("/deleteIban/{id}")
    public String deleteIban(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            savedIbanService.deleteIban(id, user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/showIban";
    }
    @GetMapping("/showIban/{id}")
    public String showIban(@PathVariable Long id, Model model) {

        model.addAttribute("ibanData", savedIbanService.findById(id));

        return "modifyIban";
    }

    @PostMapping("/modifyIban/{id}")
    public String modifyIban(@PathVariable Long id,
                             @RequestParam String iban,
                             @RequestParam String bankName,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            savedIbanService.modifyIban(iban, bankName, user);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/showIban";
    }

}
