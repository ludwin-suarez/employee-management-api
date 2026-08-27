package com.jara.employee_management.model.response;

//import jakarta.persistence.Column;/* Aplicarlo despues */
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL EMPLEADO")
public class EmployeeResponse {

    @Schema(description = "DNI DEL EMPLEADO", example = "70234567")
    private String dni;

    @Schema(description = "NOMBRE DEL EMPLEADO", example = "Maria")
    private String name;
    @Schema(description = "APELLIDOS DEL EMPLEADO", example = "Lopes Torres")
    private String lastname;
    @Schema(description = "CORREO PERSONAL DEL EMPLEADO", example = "maria.lopez@gmail.com")
    private String email;
    @Schema(description = "FECHA DE NACIMIENTO DEL EMPLEADO", example = "1995-05-15")
    private LocalDate dateofbirth;
    @Schema(description = "SALARIO DEL EMPLEADO", example = "2600.00")
    private BigDecimal salary;
    @Schema(description = "FECHA DE INICIO DE CONTRATO DEL EMPLEADO", example = "2026-05-15")
    private LocalDate contractdate;
    @Schema(description = "FECHA DE FIN DE CONTRATO DEL EMPLEADO", example = "2026-08-15")
    private LocalDate contractenddate;
    @Schema(description = "DESCRIPCIÓN DEL EMPLEADO A QUE ÁREA ES ASIGNADO", example = "ASIGNADO A AREA DE RR HH")
    private String description;

    private EmployeeStatusResponse employeStatus;
    private DepartmentResponse department;

    @Schema(description = "MUESTRA QUE SI EL EMPLEADO SE ENCUENTRA ACTIVO", example = "ACTIVO")
    private Boolean active;

}
