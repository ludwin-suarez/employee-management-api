package com.jara.employee_management.exception;

public class BusinessException extends RuntimeException {
    // El recurso existe, pero la operación no está permitida por las reglas del
    // negocio. ejempl dateEnd contracto no puede ser menor a DateHirst
    public BusinessException(String message) {
        super(message);
    }
}
