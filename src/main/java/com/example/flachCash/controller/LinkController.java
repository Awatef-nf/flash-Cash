package com.example.flachCash.controller;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class LinkController {

    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", USER);
        return "link";
    }


    @GetMapping("/addFriend")
    public String showAddFriend(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "addFriend";
    }

    @PostMapping("/addFriend")
    public Link AddFriend(Link link, Model model){
        return new Link();

    }


}
