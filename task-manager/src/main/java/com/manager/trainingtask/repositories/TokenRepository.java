package com.manager.trainingtask.repositories;

import com.manager.trainingtask.entities.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Tokens, Integer> {

    @Query("""
            select t from Tokens t inner join User u on t.userId = u.id
            where u.id = :userId and (t.expired=false or t.revoked = false)
    """)
    List<Tokens> findAllValidTokensByUser(int userId);

    Tokens findByData(String data);
}
