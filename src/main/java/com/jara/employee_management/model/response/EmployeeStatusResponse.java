package com.jara.employee_management.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL ESTADO DEL EMPLEADO")
public class EmployeeStatusResponse {

    @Schema(description = "ID DEL ESTADO", example = "1")
    private Long id;

    @Schema(description = "CÓDIGO DEL ESTADO DEL EMPLEADO", example = "ACTIVE")
    private String code;

    @Schema(description = "NOMBRE DEL ESTADO DEL EMPLEADO", example = "ACTIVO")
    private String name;
}
