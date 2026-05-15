package com.example.flachCash.repository;

import com.example.flachCash.domain.Link;
import com.example.flachCash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    boolean existsByUserOwnerAndUserFriend(User userOwner, User userFriend);

}
