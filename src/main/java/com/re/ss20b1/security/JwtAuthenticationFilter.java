package com.re.ss20b1.security;

import com.re.ss20b1.entity.Token;
import com.re.ss20b1.repository.TokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                String username = jwtService.getUsernameFromToken(jwt);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                boolean tokenInDatabaseIsValid = tokenRepository.findByTokenValue(jwt)
                        .map(Token::getRevoked)
                        .map(revoked -> !revoked)
                        .orElse(false);

                boolean tokenNotExpiredInDatabase = tokenRepository.findByTokenValue(jwt)
                        .map(Token::getExpired)
                        .map(expired -> !expired)
                        .orElse(false);

                if (jwtService.validateToken(jwt, userDetails)
                        && tokenInDatabaseIsValid
                        && tokenNotExpiredInDatabase) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("JWT không hợp lệ hoặc đã hết hạn: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
