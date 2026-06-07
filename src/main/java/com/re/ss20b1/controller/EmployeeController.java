package com.re.ss20b1.controller;


import com.re.ss20b1.dto.ApiResponse;
import com.re.ss20b1.entity.Employee;
import com.re.ss20b1.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Employee>> getCurrentEmployee(
            Authentication authentication
    ) {
        ApiResponse<Employee> response =
                employeeService.getCurrentEmployee(authentication.getName());

        return new ResponseEntity<>(
                response,
                response.getHttpStatus()
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        ApiResponse<List<Employee>> response =
                employeeService.getAllEmployees();

        return new ResponseEntity<>(
                response,
                response.getHttpStatus()
        );
    }
}
