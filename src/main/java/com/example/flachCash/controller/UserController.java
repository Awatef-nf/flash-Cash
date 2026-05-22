package com.example.flachCash.controller;


import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.Transfer;
import com.example.flachCash.domain.User;
import com.example.flachCash.dto.RegisterDto;
import com.example.flachCash.service.SavedIbanService;
import com.example.flachCash.service.TransferService;
import com.example.flachCash.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final SavedIbanService savedIbanService;
    private final TransferService transferService;

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {

        String email = authentication.getName();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        // recuperate the connected user
        User user = optionalUser.get();
        List<Transfer> transfers = transferService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("transfers", transfers);

        return "profile";

    }

//============= REGISTER =========================

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String signUp(@Valid @ModelAttribute("user") RegisterDto dto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registerDto", dto);
            return "register";
        }
        try {
            userService.register(dto);
            return "redirect:/login";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }


    @GetMapping("/showIban")
    public String showIbanList(Authentication authentication, Model model) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedIban> ibanList = savedIbanService.showIbans(user.getId());
        model.addAttribute("ibanList", ibanList);

        return "showIban";
    }


}
