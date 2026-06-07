package com.re.ss20b1.service;

import com.re.ss20b1.dto.*;

public interface AuthService {

    ApiResponse<TokenResponse> login(LoginRequest request);

    ApiResponse<TokenResponse> refresh(RefreshTokenRequest request);

    ApiResponse<String> logout(String username);
}
