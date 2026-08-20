package com.jara.employee_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Debes definir este constructor explícitamente
    public ResourceNotFoundException(String message) {
        super(message);
        /*
         * // excepciion propia de Spring Boot
         * .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
         * "Estado no encontrado con ID: " + request.getEmployeeStatusId()));
         */
    }

}
