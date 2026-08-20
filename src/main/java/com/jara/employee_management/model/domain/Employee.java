package com.jara.employee_management.model.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jara.employee_management.model.statusenum.EmployeeWorkingStatus;

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
    private String lastname;

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

    @ManyToOne
    @JoinColumn(name = "statusId")
    private EmployeeStatus statusId;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department departmentId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
