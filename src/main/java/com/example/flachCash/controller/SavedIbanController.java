package com.example.flachCash.controller;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.SavedIbanRepository;
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
    private final SavedIbanRepository savedIbanRepository;


    @GetMapping("/addIban")
    public String showAddIban(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "addIban";
    }

    @PostMapping("/addIban")
    public String addIban(@RequestParam String iban,
                          @RequestParam String bankName,
                          Authentication authentication,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = savedIbanRepository.existsByIbanAndUser_Id(iban, user.getId());

        if (exists) {
            redirectAttributes.addFlashAttribute("error", "IBAN already exists for this user");
            return "redirect:/addIban";
        }


        if (savedIbanRepository.countByUser_Id(user.getId()) >= 5) {
            redirectAttributes.addFlashAttribute("error", "You have reached the maximum number of IBANs (5)");
            return "redirect:/showIban";
        }

        SavedIban savedIban = SavedIban.builder()
                .iban(iban)
                .bankName(bankName)
                .user(user)
                .build();

        savedIbanRepository.save(savedIban);
        redirectAttributes.addFlashAttribute("success", "IBAN added successfully");
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
    public String showIban(@PathVariable Long id,
                           Authentication authentication,
                           Model model) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedIban savedIban = savedIbanService
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        model.addAttribute("ibanData", savedIban);

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
            savedIbanService.modifyIban(id, iban, bankName, user);
            redirectAttributes.addFlashAttribute("success", "IBAN modified");

            return "redirect:/showIban";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/showIban";
        }
    }
}
