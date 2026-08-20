package com.jara.employee_management.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DepartmentRequest {

    @NotNull(message = "EL CAMPO DEPARTAMENTO NO DEBE SER NULO")
    @NotBlank
    @Size(max = 70)
    // @Pattern(regexp = "^[A-Z]+$", message = "SE ADMITEN SOLO MAYUSCULAS")
    private String name;

    @NotNull(message = "EN ÉSTE CAMPO DEBE DESCRIBIR EL DEPARTAMENTO")
    @NotBlank // Pensado para campos string y que no estén vacios
    private String description;

}
