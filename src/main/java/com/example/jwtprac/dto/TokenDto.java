package com.example.jwtprac.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

    private final String token;

    @Builder
    public TokenDto(String token) {
        this.token = token;
    }
}
