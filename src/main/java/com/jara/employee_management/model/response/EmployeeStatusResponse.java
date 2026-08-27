package com.jara.employee_management.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL ESTADO DEL EMPLEADO")
public class EmployeeStatusResponse {

    @Schema(description = "ID DEL ESTATUS DEL EMPLEADO")
    private Long id;
    @Schema(description = "CÓDIGO ASIGNADO AL EMPLEADO", example = "ACTIVE-VACATION-REST-INACTIVE")
    private String code;
    @Schema(description = "NOMBRE EN ESPAÑOL DEL ESTADO ASIGNADO")
    private String name;

}
