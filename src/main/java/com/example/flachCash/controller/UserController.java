package com.example.flachCash.controller;


import com.example.flachCash.domain.User;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", USER);
        return "register";
    }
    @PostMapping("/register")
    public String signUp(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user",new User());

        if (userService.findUserByEmail(user.getEmail()).isEmpty()) {

            User newUser = User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(USER)
                    .build();

            userService.addUser(newUser);
        }

        return "redirect:/login";
    }


}
