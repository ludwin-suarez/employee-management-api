package com.jara.employee_management.model.response;

//import jakarta.persistence.Column;/* Aplicarlo despues */
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class EmployeeResponse {
    // private long id;
    private String dni;
    private String name;
    private String lastname;
    private Date dateofbirth;// Fecha de Nacimiento
    private BigDecimal salary;
    private Date contractdate;

    private Date contractenddate;
    private String description;

    private EmployeeStatusResponse employeStatus;
    private DepartmentResponse department;

    private Boolean active;

}
