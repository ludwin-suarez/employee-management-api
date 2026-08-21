package com.jara.employee_management.model.mapper;

import java.time.LocalDateTime;

import com.jara.employee_management.model.domain.Department;
import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.domain.EmployeeStatus;
import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.DepartmentResponse;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.model.response.EmployeeStatusResponse;

public class EmployeeMapper {

    private EmployeeMapper() {
        // Evita crear instancias de esta clase
    }

    public static Employee toEntity(EmployeeRequest request) {
        Employee employ = new Employee();
        employ.setDni(request.getDni());
        employ.setName(request.getName());
        employ.setLastName(request.getLastName());
        employ.setEmail(request.getEmail());
        employ.setBirthDate(request.getBirthDate());
        employ.setSalary(request.getSalary());
        employ.setHireDate(request.getHireDate());
        employ.setEndDate(request.getEndDate());
        employ.setDescription(request.getDescription());

        employ.setActive(true);
        employ.setCreatedAt(LocalDateTime.now());
        employ.setUpdatedAt(LocalDateTime.now());

        return employ;
    }

    public static EmployeeResponse toResponse(Employee employee) {

        return EmployeeResponse.builder()
                .dni(employee.getDni())
                .name(employee.getName())
                .lastname(employee.getLastName())
                .dateofbirth(employee.getBirthDate())
                .salary(employee.getSalary())
                .contractdate(employee.getHireDate())
                .contractenddate(employee.getEndDate())
                .description(employee.getDescription())
                .employeStatus(toStatusResponse(employee.getEmployeeStatus()))
                .department(toDepartmentResponse(employee.getDepartment()))
                .active(employee.isActive())
                .build();
    }

    private static EmployeeStatusResponse toStatusResponse(EmployeeStatus status) {

        if (status == null) {
            return null;
        }

        return EmployeeStatusResponse.builder()
                .id(status.getId())
                .code(status.getCode())
                .name(status.getName())
                .build();
    }

    private static DepartmentResponse toDepartmentResponse(Department department) {

        if (department == null) {
            return null;
        }

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

}
