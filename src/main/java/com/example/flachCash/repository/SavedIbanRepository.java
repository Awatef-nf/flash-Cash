package com.example.flachCash.repository;

import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedIbanRepository extends JpaRepository<SavedIban, Long> {

    boolean existsByIbanAndUserId(String iban, Integer id);

    List<SavedIban> findByUserId(Integer userId);

    SavedIban findSavedIbansById(Long id);
}
