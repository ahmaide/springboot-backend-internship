package com.ahmaide.trainingtaskmongo.auth;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends AuthenticationRequest {

    private String email;

    private int age;

    private int enabled;

}