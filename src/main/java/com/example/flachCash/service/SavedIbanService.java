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

    public SavedIban addIban(String iban, String bankName, User user){
    {
        // 1. vérifier si IBAN existe déjà pour cet user
        boolean exists = savedIbanRepository.existsByIbanAndUserId(iban, user.getId());

        if (exists) {
            throw new RuntimeException("IBAN already exists for this user");
        }

        // 2. sauvegarde si pas existant
        SavedIban savedIban = SavedIban.builder()
                .iban(iban)
                .bankName(bankName)
                .user(user)
                .build();

        return savedIbanRepository.save(savedIban);
    }
    }
    public List<SavedIban> showIbans(Integer userId){
        return savedIbanRepository.findByUserId(userId);
    }


    public void deleteIban(Long id, User user) {

        SavedIban savedIban = savedIbanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IBAN not found"));

        // sécurité : vérifier que l'IBAN appartient au user connecté
        if (!savedIban.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        savedIbanRepository.delete(savedIban);
    }

    public void modifyIban(String iban, String bankName, User user){

            SavedIban savedIban = SavedIban.builder()
                    .iban(iban)
                    .bankName(bankName)
                    .user(user)
                    .build();

        savedIbanRepository.save(savedIban);
    }

    public Optional<SavedIban> findById(Long id) {
        return savedIbanRepository.findById(id);
    }
}




