package com.example.flachCash.repository;

import com.example.flachCash.domain.Role;
import com.example.flachCash.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Integer> {

    Optional<Object> findByUserEmail(String senderEmail);
    List<UserAccount> findByUserEmailNotAndUserRoleNot(
            String email,
            Role role
    );
}
