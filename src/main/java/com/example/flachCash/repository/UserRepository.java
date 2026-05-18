package com.example.flachCash.repository;
import com.example.flachCash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);


    @Query("select u from User u left join fetch u.links where u.email = :email")
    Optional<User> findUserByEmailWithLinks(@Param("email") String email);

    // With fetch → All it here
    //user.getLinks(); // link dispo immediately [Link1, Link2]

    //    -- LEFT JOIN: recuperate all user even without link
    //      -- Bob will included even without links
    //    SELECT * FROM users u
    //    LEFT JOIN links l ON l.user_id = u.id
    //
    //    Result  :
    //    alice | google.com
    //    alice | youtube.com
    //    bob   | NULL


}
