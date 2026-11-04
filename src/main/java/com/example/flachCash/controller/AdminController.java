package com.example.flachCash.controller;


import com.example.flachCash.domain.Role;
import com.example.flachCash.domain.User;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AdminController {

    private UserService  userService;

    @GetMapping("/admin")
    public String showaAdmin(Model model, Authentication authentication){
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        List<User> users = userService.findAll()
                .stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .toList();
        model.addAttribute("users", users);
        return "admin";

    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        System.out.println(">>> deleteUser called with id=" + id);

        try {

            userService.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "User deleted with success"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/api/v1/admin";
    }
}
