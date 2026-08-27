package com.jara.employee_management.model.response;

//import jakarta.persistence.Column;/* Aplicarlo despues */
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL EMPLEADO", example = """
        {
          "dni": "70567890",
          "name": "Maria",
          "lastName": "Lopez Torres",
          "email": "maria.lopez@gmail.com",
          "birthDate": "1995-05-15",
          "salary": 2600.00,
          "hireDate": "2026-05-15",
          "endDate": null,
          "description": "Asignado al área de Recursos Humanos",
          "employeeStatus": {
            "id": 1,
            "code": "ACTIVE",
            "name": "ACTIVO"
          },
          "department": {
            "id": 2,
            "name": "Recursos Humanos"
          },
          "active": true
        }
        """)
public class EmployeeResponse {

    @Schema(description = "ID DEL EMPLEADO", example = "102")
    private Long id;

    @Schema(description = "DNI DEL EMPLEADO", example = "70234567")
    private String dni;

    @Schema(description = "NOMBRE DEL EMPLEADO", example = "Maria")
    private String name;

    @Schema(description = "APELLIDOS DEL EMPLEADO", example = "Lopez Torres")
    private String lastName;

    @Schema(description = "CORREO PERSONAL DEL EMPLEADO", example = "maria.lopez@gmail.com")
    private String email;

    @Schema(description = "FECHA DE NACIMIENTO DEL EMPLEADO", example = "1995-05-15")
    private LocalDate birthDate;

    @Schema(description = "SALARIO DEL EMPLEADO", example = "2600.00")
    private BigDecimal salary;

    @Schema(description = "FECHA DE INICIO DE CONTRATO DEL EMPLEADO", example = "2026-05-15")
    private LocalDate hireDate;

    @Schema(description = "FECHA DE FIN DE CONTRATO DEL EMPLEADO", example = "2026-08-15")
    private LocalDate endDate;

    @Schema(description = "DESCRIPCIÓN DEL EMPLEADO", example = "ASIGNADO AL ÁREA DE RR HH")
    private String description;

    @Schema(description = "ESTADO ACTUAL DEL EMPLEADO")
    private EmployeeStatusResponse employeeStatus;

    @Schema(description = "DEPARTAMENTO ASIGNADO AL EMPLEADO")
    private DepartmentResponse department;

    @Schema(description = "INDICA SI EL EMPLEADO SE ENCUENTRA ACTIVO", example = "true")
    private Boolean active;
}
