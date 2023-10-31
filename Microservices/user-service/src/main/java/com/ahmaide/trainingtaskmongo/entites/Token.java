package com.ahmaide.trainingtaskmongo.entites;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Token {

    @Id
    private String id;

    private String data;

    private String type;

    private boolean expired;

    private boolean revoked;

    private String userId;

}