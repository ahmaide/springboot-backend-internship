package com.ahmaide.trainingtaskmongo.repositories;

import com.ahmaide.trainingtaskmongo.entites.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
    Token findByData(String data);
}
