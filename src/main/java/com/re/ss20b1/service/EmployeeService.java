package com.re.ss20b1.service;


import com.re.ss20b1.dto.ApiResponse;
import com.re.ss20b1.entity.Employee;

import java.util.List;

public interface EmployeeService {

    ApiResponse<Employee> getCurrentEmployee(String username);

    ApiResponse<List<Employee>> getAllEmployees();
}
