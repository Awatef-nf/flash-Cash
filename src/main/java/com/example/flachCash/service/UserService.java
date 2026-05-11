package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.flachCash.domain.Role.USER;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User addUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findUserByEmail(mail);
    }
    //enregister un utilisateur
    public void register(User user) {
        // Vérifie si email déjà pris
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        UserAccount account = UserAccount.builder()
                .amount(0.0)
                .iban("")
                .build();

        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(USER)
                .account(account)
                .build();

        userRepository.save(newUser);
    }



}
