package com.jara.employee_management.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL DEPARTAMENTO ASIGNADO AL EMPLEADO")
public class DepartmentResponse {

    @Schema(description = "ID DEL DEPARTAMENTO", example = "1")
    private Long id;

    @Schema(description = "NOMBRE DEL DEPARTAMENTO", example = "TECNOLOGÍA")
    private String name;
}
