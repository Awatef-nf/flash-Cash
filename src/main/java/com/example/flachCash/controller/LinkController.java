package com.example.flachCash.controller;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.LinkRepository;
import com.example.flachCash.service.LinkService;
import com.example.flachCash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class LinkController {

     private final LinkService linkService;
     private final UserService userService;


    @GetMapping("/contact")
    public String showContact(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", USER);
        return "link";
    }

    @GetMapping("/addFriend")
    public String showAddFriend(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("links", user.getLinks());
        return "addFriend";
    }
    @PostMapping("/addFriend")
    public String addFriend(@RequestParam("friendEmail") String friendEmail,
                            Model model,
                            Authentication authentication) {

        String email = authentication.getName();
        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userService.findUserByEmail(friendEmail)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        Link link = new Link();
        link.setUserOwner(user);   //  Celui qui est connecté
        link.setUserFriend(friend); //  Celui sélectionné dans le formulaire

        linkService.addLink(link);

        return "redirect:/link";
    }

    @PostMapping("/deleteLink/{id}")
    public String deleteLink(@PathVariable Integer id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {

            // IMPORTANT : supprimer le link avec son ID
            linkService.deleteLink(id);

            linkService.deleteLink(user.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/link";
    }

    }

