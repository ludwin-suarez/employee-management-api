package com.jara.employee_management.model.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@Data
public class Employee implements Serializable {
    // private static final long SerialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni", length = 8)
    private String dni;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "hireDate") // fecha de contratación
    private LocalDate hireDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;
    // fetch = FetchType.LAZY:Carga Perezosa) Le indica a JPA que no traiga los
    // datos del objeto relacionado de la BD
    // hasta que sean necesarios explícitamente (por ejemplo, cuando llames a
    // employee.getDepartment()).
    @JoinColumn(name = "statusId")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeStatus employeeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private Department department;

    @Column(name = "active")
    private boolean active;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
