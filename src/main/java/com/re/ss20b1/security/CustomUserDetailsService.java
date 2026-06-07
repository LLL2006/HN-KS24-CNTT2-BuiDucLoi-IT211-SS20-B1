package com.re.ss20b1.security;

import com.re.ss20b1.entity.Employee;
import com.re.ss20b1.entity.Role;
import com.re.ss20b1.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public Employee loadEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy nhân viên"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = loadEmployeeByUsername(username);

        if (Boolean.FALSE.equals(employee.getEnabled())) {
            throw new DisabledException("Tài khoản đã bị vô hiệu hóa");
        }

        List<SimpleGrantedAuthority> authorities = employee.getRoles()
                .stream()
                .map(Role::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new org.springframework.security.core.userdetails.User(
                employee.getUsername(),
                employee.getPassword(),
                authorities
        );
    }
}
