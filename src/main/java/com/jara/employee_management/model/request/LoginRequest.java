
package com.jara.employee_management.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "EL USUARIO ES OBLIGATORIO")
    private String username;

    @NotBlank(message = "LA CONTRASEÑA ES OBLIGATORIA")
    private String password;
}
