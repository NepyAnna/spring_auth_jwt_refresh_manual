package com.exemple.auth.security.dtos;

import lombok.Data;

@Data
public class JwtAuthDto {
    String token;
    String refreshToken;
}
