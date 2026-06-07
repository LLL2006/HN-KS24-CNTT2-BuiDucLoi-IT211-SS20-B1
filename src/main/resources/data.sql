INSERT INTO roles (id, role_name)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_HR'),
    (3, 'ROLE_EMPLOYEE');

-- password là 123456 đã BCrypt
INSERT INTO employees (id, username, password, enabled)
VALUES
    (1, 'admin', '$2a$10$kH0YQ7iK3GwQ9JcuWh7zHeqy.7naAiQhe.xLUmE3TQe0WXmpO9Jla', true),
    (2, 'hr', '$2a$10$kH0YQ7iK3GwQ9JcuWh7zHeqy.7naAiQhe.xLUmE3TQe0WXmpO9Jla', true),
    (3, 'employee', '$2a$10$kH0YQ7iK3GwQ9JcuWh7zHeqy.7naAiQhe.xLUmE3TQe0WXmpO9Jla', true);

INSERT INTO employee_roles (employee_id, role_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);