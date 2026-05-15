package com.example.flachCash.controller;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.Role;
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

import java.util.List;

import static com.example.flachCash.domain.Role.USER;

@Controller
@AllArgsConstructor
public class LinkController {

     private final LinkService linkService;
     private final UserService userService;
     private final LinkRepository linkRepository;


    @GetMapping("/link")
    public String showContact(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userService.findUserByEmailWithLinks(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("role", USER);

        return "link";
    }


    @GetMapping("/addFriend")
    public String showAddFriend(Authentication authentication, Model model) {

        String email = authentication.getName();

        User currentUser = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> users = userService.findAll()
                .stream()
                .filter(u -> !u.getEmail().equals(currentUser.getEmail())) //userconecter est exclu
                .filter(u -> u.getRole() != Role.ADMIN)//exclu admin
                .toList();

        model.addAttribute("users", users);

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

        //  Vérification si le lien existe déjà
        boolean exists = linkRepository.existsByUserOwnerAndUserFriend(user, friend);

        if (exists) {
            model.addAttribute("error", "this friend exist !");
            model.addAttribute("user", user);
            return "link";
        }

        Link link = new Link();
        link.setUserOwner(user);
        link.setUserFriend(friend);

        linkService.addLink(link);

        return "redirect:/link";
    }

    @PostMapping("/deleteLink/{id}")
    public String deleteLink(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // IMPORTANT : supprimer le link avec son ID
            linkService.deleteLink(id,user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/link";
    }

    }

