package com.example.flachCash.controller;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.SavedIbanRepository;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showAddIban(Authentication authentication,
                              Model model) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("savedIban", new SavedIban());
        return "addIban";
    }

    @PostMapping("/addIban")
    public String addIban(@Valid @ModelAttribute("savedIban") SavedIban savedIban,
                          BindingResult result,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "addIban";
        }

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {

            savedIbanService.addIban(
                    savedIban.getIban(),
                    savedIban.getBankName(),
                    user
            );

            redirectAttributes.addFlashAttribute(
                    "success",
                    "IBAN added successfully"
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
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
