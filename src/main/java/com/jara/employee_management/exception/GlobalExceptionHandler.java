package com.jara.employee_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleResourceNotFound(
                        ResourceNotFoundException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("error", " RESOURCE NOT_FOUND");
                response.put("message", exception.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<Map<String, Object>> handleBusinessException(
                        BusinessException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", "BUSINESS_ERROR");
                response.put("message", exception.getMessage());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationException(
                        MethodArgumentNotValidException exception) {

                Map<String, Object> errors = new HashMap<>();

                exception.getBindingResult() // obtiene el result. de la validación. DNI, vacio
                                .getFieldErrors()// obtiene específicamente los errores asociados a campos
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                // forEach: "Recorre todos los errores y guarda el nombre del campo y su
                // mensaje."
                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", "VALIDATION_ERROR");
                response.put("messages", errors);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }
}
