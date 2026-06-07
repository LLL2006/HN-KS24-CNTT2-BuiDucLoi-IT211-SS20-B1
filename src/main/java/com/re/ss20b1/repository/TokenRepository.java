package com.re.ss20b1.repository;


import com.re.ss20b1.entity.Employee;
import com.re.ss20b1.entity.Token;
import com.re.ss20b1.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenValue(String tokenValue);

    List<Token> findByEmployeeAndRevokedFalseAndExpiredFalse(Employee employee);

    List<Token> findByEmployeeAndTokenTypeAndRevokedFalseAndExpiredFalse(
            Employee employee,
            TokenType tokenType
    );
}
