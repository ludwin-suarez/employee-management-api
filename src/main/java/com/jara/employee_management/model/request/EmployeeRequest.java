package com.jara.employee_management.model.request;

//import org.apache.logging.log4j.message.Message;/*No se está usando */
import lombok.Data;
import jakarta.validation.constraints.*;/* Incluye a .NotBlank| .NotNull | .Pattern | DecimalMin, Column*/
import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DATOS NECESARIOS PARA REGISTRAR UN EMPLEADO")
@Data
public class EmployeeRequest {
    @Schema(description = "DOCUMENTO NACIONAL DE IDENTIDAD ", example = "70567890")
    @NotNull(message = "EL CAMPO DE DNI NO DEBE SER NULO")
    @Size(min = 8, max = 8, message = "EL DNI DEBE TENER UN TAMAÑO FIJO DE 8 CARACTERES")
    @NotBlank(message = "DNI NO DEBE ESTAR EN BLANCO")
    @Pattern(regexp = "\\d*", message = "El DNI DEBE DE ESTAR EN FORMATO DE SOLO NÚMEROS")
    private String dni;

    @Schema(description = "NOMBRE EL EMPLEADO", example = "Juan Campos")
    @NotNull(message = "EL CAMPO DE NOMBRE NO DEBE SER NULO")
    @NotBlank
    @Size(max = 70)
    private String name;

    @Schema(description = "APELLIDOS DEL EMPLEADO", example = "SUAREZ LOPEZ")
    @NotNull(message = "EL CAMPO DEL APELLIDO NO DEBE SER NULO ")
    @NotBlank
    @Size(max = 70)
    private String lastName;

    @Schema(description = "CORREO DEL EMPLEADO", example = "juan.suarez@gmail.com")
    @Email(message = "DEBE INGRESAR UN CORREO ELECTRÓNICO VÁLIDO")
    private String email;

    @Schema(description = "FECHA DE NACIMIENTO DEL EMPLEADO", example = "1996-08-01")
    @NotNull(message = "EL CAMPO FECHA DE NACIMIENTO NO PUEDE SER NULO")
    private LocalDate birthDate;

    // @Min(0) // tipos numéricos enteros, pero para BigDecimal @DecimalMin
    @Schema(description = "SALARIO DEL EMPLEADO", example = "2500.00", minimum = "0")
    @DecimalMin(value = "0.0", inclusive = true, message = "EL SALARIO NO PUEDE SER NEGATIVO")
    private BigDecimal salary;

    @Schema(description = "FECHA DE INICIO DE CONTRATO DEL EMPLEADO", example = "2026-08-24")
    private LocalDate hireDate;

    @Schema(description = "FECHA DE FIN DE CONTRATO DEL EMPLEADO", example = "2026-08-24")
    private LocalDate endDate;

    @Schema(description = "DESCRIPCIÓN DEL EMPLEADO A QUE ÁREA ES ASIGNADO", example = "ASIGNADO A AREA DE RR HH")
    @NotBlank(message = "LA DESCRIPCIÓN ES OBLIGATORIA, NO ACEPTA NULL")
    private String description;

    @Schema(description = "ESTADO DE ESTATUS DE EMPLEADO: ACTIVE-VACATION-REST-INACTIVE", example = "VACATION = VACACIONES")
    @Pattern(regexp = "ACTIVE|VACATION|REST|INACTIVE", message = "EL ESTADO DEBE SER ACTIVE, VACATION, REST O INACTIVE")
    private String code;

    @Schema(description = "DEPARTAMENTO ASIGNADO POR ID ENTRE: 1,2,3,4", example = "1: Tecnología; 2: Recursos Humanos; 3: Finanzas; 4:Operaciones")
    @Min(1)
    @Max(4)
    @NotNull(message = "EL CAMPO ESTADO NO PUEDE SER NULO-1: TECNOLOGIA,2: RRHH,3: FINANZAS, 4: OPERACIONES")
    private Long departmentId;
}
