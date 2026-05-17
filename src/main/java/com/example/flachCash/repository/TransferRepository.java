package com.example.flachCash.repository;

import com.example.flachCash.domain.Transfer;
import com.example.flachCash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Integer> {


    ;
}
