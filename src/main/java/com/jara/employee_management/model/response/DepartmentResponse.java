package com.jara.employee_management.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "INFORMACIÓN DEL DEPARTAMENTO QUE SE ENCUENTRA ASGINADO EL EMPLEADO")
public class DepartmentResponse {

    @Schema(description = "ID DEL DEPARTAMENTO")
    private Long id;
    @Schema(description = "NOMBRE DEL DEPARTAMENTO")
    private String name;

}
