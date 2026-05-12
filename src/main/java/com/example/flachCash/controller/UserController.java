package com.example.flachCash.controller;


import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final SavedIbanService savedIbanService;

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", USER);
        return "register";
    }
    @PostMapping("/register")
    public String signUp(@ModelAttribute("user") User user, Model model) {
        try {
            userService.register(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); //  "Email déjà utilisé"
            model.addAttribute("user", user); //  garde les champs remplis
            return "register"; //  reste sur la page
        }
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/transfer")
    public String showTransfer(Authentication authentication,Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer() {
        return "redirect: profile";
    }

    @GetMapping("/showIban")
    public String showIbans(Authentication authentication, Model model) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedIban> ibans = savedIbanService.showIbans(user.getId());
        model.addAttribute("ibans", ibans);

        return "showIban";
    }


}
