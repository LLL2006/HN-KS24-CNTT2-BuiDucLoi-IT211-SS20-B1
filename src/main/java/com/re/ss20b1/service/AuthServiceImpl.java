package com.re.ss20b1.service;

import com.re.ss20b1.dto.*;
import com.re.ss20b1.entity.Employee;
import com.re.ss20b1.entity.Token;
import com.re.ss20b1.enums.TokenType;
import com.re.ss20b1.repository.TokenRepository;
import com.re.ss20b1.security.CustomUserDetailsService;
import com.re.ss20b1.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    @Override
    public ApiResponse<TokenResponse> login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Employee employee =
                userDetailsService.loadEmployeeByUsername(request.getUsername());

        String accessToken =
                jwtService.generateAccessToken(
                        employee.getUsername(),
                        employee.getRoles().stream().toList()
                );

        String refreshToken =
                jwtService.generateRefreshToken(employee.getUsername());

        saveToken(employee, accessToken, TokenType.ACCESS);
        saveToken(employee, refreshToken, TokenType.REFRESH);

        return new ApiResponse<>(
                true,
                "Đăng nhập thành công",
                new TokenResponse(accessToken, refreshToken, "Bearer"),
                HttpStatus.OK
        );
    }

    @Override
    public ApiResponse<TokenResponse> refresh(RefreshTokenRequest request) {

        Token oldRefreshToken = tokenRepository.findByTokenValue(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token không tồn tại"));

        if (Boolean.TRUE.equals(oldRefreshToken.getRevoked())
                || Boolean.TRUE.equals(oldRefreshToken.getExpired())) {
            throw new RuntimeException("Refresh Token đã bị thu hồi hoặc hết hạn");
        }

        String username =
                jwtService.getUsernameFromToken(request.getRefreshToken());

        Employee employee =
                userDetailsService.loadEmployeeByUsername(username);

        String newAccessToken =
                jwtService.generateAccessToken(
                        employee.getUsername(),
                        employee.getRoles().stream().toList()
                );

        saveToken(employee, newAccessToken, TokenType.ACCESS);

        return new ApiResponse<>(
                true,
                "Cấp lại Access Token thành công",
                new TokenResponse(newAccessToken, request.getRefreshToken(), "Bearer"),
                HttpStatus.OK
        );
    }

    @Override
    public ApiResponse<String> logout(String username) {

        Employee employee =
                userDetailsService.loadEmployeeByUsername(username);

        List<Token> validTokens =
                tokenRepository.findByEmployeeAndRevokedFalseAndExpiredFalse(employee);

        List<Token> revokedTokens = validTokens.stream()
                .peek(token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                })
                .toList();

        tokenRepository.saveAll(revokedTokens);

        SecurityContextHolder.clearContext();

        return new ApiResponse<>(
                true,
                "Đăng xuất thành công, toàn bộ token đã bị thu hồi",
                null,
                HttpStatus.OK
        );
    }

    private void saveToken(Employee employee, String tokenValue, TokenType tokenType) {
        Token token = Token.builder()
                .employee(employee)
                .tokenValue(tokenValue)
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }
}
