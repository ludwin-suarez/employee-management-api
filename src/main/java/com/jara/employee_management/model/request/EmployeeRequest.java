package com.jara.employee_management.model.request;

/* Aplicar en el project los imports coment */
//import org.apache.logging.log4j.message.Message;/*No se está usando */
//import jakarta.persistence.Column; /*No se está usando */
import lombok.Data;
import jakarta.validation.constraints.*;/* Incluye a .NotBlank| .NotNull | .Pattern*/
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeRequest {
    @NotNull(message = "EL CAMPO DE DNI NO DEBE SER NULO")
    @Size(min = 8, max = 8, message = "EL DNI DEBE TENER UN TAMAÑO FIJO DE 8 CARACTERES")
    @NotBlank(message = "DNI NO DEBE ESTAR EN BLANCO")
    @Pattern(regexp = "\\d*", message = "El DNI DEBE DE ESTAR EN FORMATO DE SOLO NÚMEROS")
    private String dni;

    @NotNull(message = "EL CAMPO DE NOMBRE NO DEBE SER NULO")
    @NotBlank
    @Size(max = 70)
    // @Pattern(regexp = "^[A-Z]+$", message = "SE ADMITEN SOLO MAYUSCULAS")
    private String name;
    @NotNull(message = "EL CAMPO DEL APELLIDO NO DEBE SER NULO ")
    @NotBlank
    @Size(max = 70)
    // @Pattern(regexp = "^[A-Z]+$", message = "SE ADMITEN SOLO MAYUSCULAS")
    private String lastName;

    @Email(message = "Debe ingresar un correo electrónico válido")
    private String email;

    @NotNull(message = "EL CAMPO FECHA DE NACIMIENTO NO PUEDE SER NULO")
    private LocalDate birthDate;

    @Min(0)
    private BigDecimal salary;

    // @NotNull(message = "EL CAMPO FECHA DE CONTRATO NO PUEDE SER NULO")
    private LocalDate hireDate;

    private LocalDate endDate;

    @NotNull
    @NotBlank // Pensado para campos string y que no estén vacios
    private String description;

    @NotNull(message = "EL CAMPO DE ESTADO DEL EMPLEADO DEBE ESTAR EN MAYUSCULA")
    // @Pattern(regexp = "\\d*", message = "El ESTADO DE TRABAJO SOLO DEBE DE ESTAR
    // EN FORMATO DE SOLO NÚMEROS")
    // @Pattern(regexp = "^[1-4]+$", message = "El ESTADO DE TRABAJO TIENE QUE SER
    // 1,2,3 Ó 4")
    @NotNull(message = "EL CAMPO ESTADO NO PUEDE SER NULO-ACTIVE-VACATION-REST-INACTIVE")
    private String code;

    @Min(1)
    @Max(4)
    @NotNull(message = "EL CAMPO ESTADO NO PUEDE SER NULO-1: TECNOLOGIA,2: RRHH,3: FINANZAS, 4: OPERACIONES")
    private Long departmentId;
}
