package com.example.registerempledosweb.model.request;

/* Aplicar en el project los imports coment */
//import org.apache.logging.log4j.message.Message;/*No se está usando */
//import jakarta.persistence.Column; /*No se está usando */
import lombok.Data;
import jakarta.validation.constraints.*;/* Incluye a .NotBlank| .NotNull | .Pattern*/
import java.math.BigDecimal;
import java.util.Date;

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
    private String lastname;
    @NotNull(message = "EL CAMPO FECHA DE NACIMIENTO NO PUEDE SER NULO")
    private Date dateofbirth;// Fecha de Nacimiento
    private BigDecimal salary;
    @NotNull(message = "EL CAMPO FECHA DE CONTRATO NO PUEDE SER NULO")
    private Date contractdate;
    // @NotNull(message = "EL CAMPO ESTADO NO PUEDE SER NULO")
    // @NotBlank(message = "EL CAMPO ESTADO NO PUEDE QUEDAR EN BLANCO")
    private Date contractenddate;
    @NotNull
    @NotBlank
    private String description;
    @NotNull(message = "EL CAMPO DE ESTADO DEL EMPLEADO DEBE ESTAR EN MAYUSCULA")
    // @Pattern(regexp = "\\d*", message = "El ESTADO DE TRABAJO SOLO DEBE DE ESTAR
    // EN FORMATO DE SOLO NÚMEROS")
    // @Pattern(regexp = "^[1-4]+$", message = "El ESTADO DE TRABAJO TIENE QUE SER
    // 1,2,3 Ó 4")
    private int workingstatus;

    @NotNull(message = "EL CAMPO ESTADO NO PUEDE SER NULO")
    private boolean state;
}
