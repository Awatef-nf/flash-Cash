package com.example.flachCash.service;

import com.example.flachCash.domain.User;
import com.example.flachCash.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findUserByEmail(mail);
    }
}