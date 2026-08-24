package com.jara.employee_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        // =========================================================
        // 1. RECURSO NO ENCONTRADO / PERSONALIZADO
        // =========================================================
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleResourceNotFound(
                        ResourceNotFoundException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("error", "RESOURCE NOT_FOUND");
                response.put("message", exception.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }

        // ==========================================================
        // 2.0 REGLAS DE NEGOCIO / ARGUMENTOS INVÁLIDOS / PERSONALIZADO
        // ==========================================================
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

        // ==========================================================
        // 2.1 REGLAS DE NEGOCIO / ARGUMENTOS INVÁLIDOS
        // ==========================================================
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, Object>> handleIllegalArgument(
                        IllegalArgumentException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", "BAD_REQUEST");
                response.put("message", exception.getMessage());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        // ==========================================================
        // 3. VALIDACIONES DE @PathVariable / @RequestParam
        // ==========================================================
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Map<String, Object>> handleConstraintViolation(
                        ConstraintViolationException exception) {

                Map<String, String> errors = new HashMap<>();

                exception.getConstraintViolations()
                                .forEach(error -> errors.put(
                                                error.getPropertyPath().toString(),
                                                error.getMessage()));

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", "PARAMETER_VALIDATION_ERROR");
                response.put("messages", errors);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }
        // ==========================================================
        // 4. VALIDACIONES DEL @RequestBody
        // ==========================================================

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationException(
                        MethodArgumentNotValidException exception) {
                Map<String, String> errors = new HashMap<>();// Especif. los datos a capturar, son String
                // Map<String, Object> errors = new HashMap<>();//Case> General
                exception.getBindingResult() // obtiene el result. de la validación. DNI, vacio
                                .getFieldErrors()// obtiene específicamente los errores asociados a campos
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));
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

        // ==========================================================
        // 5. ERROR DE CONVERSIÓN DE PARÁMETROS
        // ==========================================================
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<Map<String, Object>> handleTypeMismatch(
                        MethodArgumentTypeMismatchException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("error", "INVALID_PARAMETER");
                response.put(
                                "message",
                                "El parámetro '" + exception.getName()
                                                + "' tiene un formato inválido");

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        // =========================================================
        // 6. ERROR GENERAL / INESPERADO (ERROR GENÉRICO)
        // =========================================================

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGeneralException(
                        Exception exception) {
                log.error("ERROR INTERNO NO CONTROLADO: ", exception);
                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("error", "INTERNAL_SERVER_ERROR");
                response.put("message", "Ocurrió un error interno en el servidor.");

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }

        // =========================================================
        // 7. CONTROLAR INGRESO DE END POINT - HTTP 405
        // =========================================================
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
                        HttpRequestMethodNotSupportedException exception) {

                Map<String, Object> response = new HashMap<>();

                response.put("timestamp", LocalDateTime.now());
                response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
                response.put("error", "METHOD_NOT_ALLOWED");
                response.put(
                                "message",
                                "El método HTTP '" + exception.getMethod()
                                                + "' no está permitido para este endpoint.");

                return ResponseEntity
                                .status(HttpStatus.METHOD_NOT_ALLOWED)
                                .body(response);
        }
}
