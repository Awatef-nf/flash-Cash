package com.example.flachCash.service;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.LinkRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    //====ADD
    public void addLink(Link newlink){
        linkRepository.save(newlink);
    }
    //====DELETE
    @Transactional
    public void deleteLink(Long linkId, User currentUser){

        Link link = linkRepository.findById(Math.toIntExact(linkId))
                .orElseThrow(()-> new RuntimeException("Link not found"));

        if (!link.getUserOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        linkRepository.delete(link);
    }


}



