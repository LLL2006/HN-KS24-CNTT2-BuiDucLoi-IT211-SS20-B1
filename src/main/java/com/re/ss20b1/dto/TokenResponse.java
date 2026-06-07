package com.re.ss20b1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType;
}
