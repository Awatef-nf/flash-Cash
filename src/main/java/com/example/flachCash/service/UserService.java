package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.domain.UserAccount;
import com.example.flachCash.dto.RegisterDto;
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

    public void register(RegisterDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.findUserByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserAccount account = UserAccount.builder()
                .balance(0.0)
                .build();

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        // encode AFTER validation
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(USER);
        user.setAccount(account);
        account.setUser(user);

        userRepository.save(user);
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
