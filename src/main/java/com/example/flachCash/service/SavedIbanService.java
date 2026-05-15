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

    public List<SavedIban> findAll(){
        return savedIbanRepository.findAll();
    }
    //=====> ADD IBAN
    public void addIban(String iban, String bankName, User user) {
        // exist ?
        boolean exists = savedIbanRepository. existsByIbanAndUser_Id(iban, user.getId());

        if (exists) {
            throw new RuntimeException("IBAN already exists for this user");
        }
        if (findAll().size()<5) {
            // save
            SavedIban savedIban = SavedIban.builder()
                    .iban(iban)
                    .bankName(bankName)
                    .user(user)
                    .build();
            savedIbanRepository.save(savedIban);
        }

    }

    //=====> SHOW IBAN
    public List<SavedIban> showIbans(Integer userId) {
        return savedIbanRepository.findByUser_Id(userId);
    }

    //=====> DELETE IBAN
    public void deleteIban(Long id, User user) {

        SavedIban savedIban = savedIbanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("IBAN not found"));
        //vérification que c'est bien un iban de l'utilisateur connecté
        if (!savedIban.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized");
        }
        //delete
        savedIbanRepository.delete(savedIban);
    }
    //=====> MODIFY IBAN
    public SavedIban modifyIban(Long id, String iban, String bankName, User user) {

        SavedIban savedIban = savedIbanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IBAN not found"));

        if (!savedIban.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        // vérifier doublon
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

        return savedIbanRepository.save(savedIban);
    }

    //=====> SAVE IBAN
    public Optional<SavedIban> findById(Long id) {
        return savedIbanRepository.findById(id);
    }
    //=====> FIND IBAN
    public Optional<SavedIban> findByIdAndUserId(Long id, Integer userId) {
        return savedIbanRepository.findByIdAndUser_Id(id, userId);
    }
}



