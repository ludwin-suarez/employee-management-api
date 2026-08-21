package com.jara.employee_management.model.response;

//import jakarta.persistence.Column;/* Aplicarlo despues */
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class EmployeeResponse {
    // private long id;
    private String dni;
    private String name;
    private String lastname;
    private String email;
    private LocalDate dateofbirth;// Fecha de Nacimiento
    private BigDecimal salary;
    private LocalDate contractdate;
    private LocalDate contractenddate;
    private String description;
    private EmployeeStatusResponse employeStatus;
    private DepartmentResponse department;
    private Boolean active;

}
