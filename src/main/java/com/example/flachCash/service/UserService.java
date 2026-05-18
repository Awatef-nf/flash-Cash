package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.flachCash.domain.Role.USER;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findUserByEmail(mail);
    }

    public void register(User user) {
        // email is already used?
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserAccount account = UserAccount.builder()
                .balance(0.0)
                .build();
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(USER)
                .account(account)
                .build();

        account.setUser(newUser);
        userRepository.save(newUser);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByEmailWithLinks(String email) {
        return userRepository.findUserByEmailWithLinks(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
