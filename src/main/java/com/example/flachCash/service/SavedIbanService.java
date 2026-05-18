package com.example.flachCash.service;

import com.example.flachCash.domain.SavedIban;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.SavedIbanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SavedIbanService {

    private final SavedIbanRepository savedIbanRepository;

    //=====> ADD IBAN
    public void addIban(String iban, String bankName, User user) {
        // 1. IBAN unique global
        boolean exists = savedIbanRepository.existsByIban(iban);
        if (exists) {
            throw new RuntimeException("IBAN already exists");
        }

        // 2. max 5 IBAN par user
        long count = savedIbanRepository.countByUser_Id(user.getId());
        if (count >= 5) {
            throw new RuntimeException("Max 5 IBAN allowed");
        }

        // 3. save
        SavedIban savedIban = SavedIban.builder()
                .iban(iban)
                .bankName(bankName)
                .user(user)
                .build();
        savedIbanRepository.save(savedIban);
    }

    //=====> SHOW IBAN
    public List<SavedIban> showIbans(Integer userId) {
        return savedIbanRepository.findByUser_Id(userId);
    }

    //=====> DELETE IBAN
    public void deleteIban(Long id, User user) {
        SavedIban savedIban = savedIbanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("IBAN not found"));

        //it is the userConnected Iban?
        if (!savedIban.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized");
        }
        //delete
        savedIbanRepository.delete(savedIban);
    }

    //=====> MODIFY IBAN
    public void modifyIban(Long id, String iban, String bankName, User user) {
        SavedIban savedIban = savedIbanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IBAN not found"));

        if (!savedIban.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // verification of duplicate
        boolean exists = savedIbanRepository
                .existsByIbanAndUser_IdAndIdNot(
                        iban,
                        user.getId(),
                        id
                );
        if (exists) {
            throw new RuntimeException("IBAN already exists");
        }

        savedIban.setIban(iban);
        savedIban.setBankName(bankName);
        savedIbanRepository.save(savedIban);
    }

    //=====> FIND IBAN
    public Optional<SavedIban> findByIdAndUserId(Long id, Integer userId) {
        return savedIbanRepository.findByIdAndUser_Id(id, userId);
    }
}



