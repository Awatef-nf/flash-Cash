package com.example.flachCash.service;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.User;
import com.example.flachCash.repository.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    public void addLink(Link newlink){
        linkRepository.save(newlink);
    }

    public void deleteLink(Integer linkId){

        Link link= linkRepository.findById(linkId)
                .orElseThrow(() -> new RuntimeException("Link not found"));

        linkRepository.removeLinkById(linkId);
    }
    }



