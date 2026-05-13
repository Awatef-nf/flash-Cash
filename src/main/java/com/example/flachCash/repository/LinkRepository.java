package com.example.flachCash.repository;

import com.example.flachCash.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    void removeLinkById(Integer linkId);
}
