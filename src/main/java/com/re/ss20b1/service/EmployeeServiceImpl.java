package com.re.ss20b1.service;

import com.re.ss20b1.dto.ApiResponse;
import com.re.ss20b1.entity.Employee;
import com.re.ss20b1.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public ApiResponse<Employee> getCurrentEmployee(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        return new ApiResponse<>(
                true,
                "Lấy thông tin nhân viên hiện tại thành công",
                employee,
                HttpStatus.OK
        );
    }

    @Override
    public ApiResponse<List<Employee>> getAllEmployees() {
        return new ApiResponse<>(
                true,
                "Lấy danh sách nhân viên thành công",
                employeeRepository.findAll(),
                HttpStatus.OK
        );
    }
}
