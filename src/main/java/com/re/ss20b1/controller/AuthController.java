package com.re.ss20b1.controller;

import com.re.ss20b1.dto.*;
import com.re.ss20b1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody LoginRequest request
    ) {
        ApiResponse<TokenResponse> response =
                authService.login(request);

        return new ResponseEntity<>(
                response,
                response.getHttpStatus()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        ApiResponse<TokenResponse> response =
                authService.refresh(request);

        return new ResponseEntity<>(
                response,
                response.getHttpStatus()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            Authentication authentication
    ) {
        ApiResponse<String> response =
                authService.logout(authentication.getName());

        return new ResponseEntity<>(
                response,
                response.getHttpStatus()
        );
    }
}
