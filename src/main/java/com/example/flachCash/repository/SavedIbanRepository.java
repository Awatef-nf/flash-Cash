package com.example.flachCash.repository;

import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SavedIbanRepository extends JpaRepository<SavedIban, Long> {

    boolean existsByIbanAndUser_IdAndIdNot(String iban, Integer userId, Long id);

    List<SavedIban> findByUser_Id(Integer userId);

    Optional<SavedIban> findByIdAndUser_Id(Long id, Integer userId);

    int countByUser_Id(Integer id);

    boolean existsByIban(String iban);
}