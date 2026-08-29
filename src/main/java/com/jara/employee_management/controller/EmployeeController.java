package com.jara.employee_management.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;//Describe qué hace el endpoint.
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;//Documenta Posible respuesta HTTP
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;//Cambia name standar al agrupar los EndPoint y descrip

@RestController
@Validated // Valida var de parametros y metodos de controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/employee")
@Tag(name = "Employee", description = "Operaciones CRUD para la gestión de empleados")
public class EmployeeController {

        private final EmployeeService employeeService; // inyección de dependencias

        // GET /api/employee
        @Operation(summary = "Listar empleados", description = "Obtiene todos los empleados que se encuentran activos.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @GetMapping("/list_employees")
        public List<EmployeeResponse> list() {
                return employeeService.list();
        }

        // POST /api/employee
        @Operation(summary = "Crear empleado", description = "Registra un nuevo empleado en el sistema.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado creado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
                        @ApiResponse(responseCode = "404", description = "Departamento o estado del empleado no encontrado"),
                        @ApiResponse(responseCode = "409", description = "El DNI del empleado ya existe"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @PostMapping("/create_employee")
        public EmployeeResponse create(
                        @RequestBody @Valid EmployeeRequest request) {
                log.info("request: {}", request);
                return employeeService.create(request);
        }

        // GET /api/employee/{dni}
        @Operation(summary = "Buscar empleado por DNI", description = "Obtiene un empleado activo mediante su DNI.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado encontrado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El DNI debe tener 8 digitos numericos"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @GetMapping("/search_employee/{dni}")
        public EmployeeResponse getByDni(
                        @Parameter(description = "DNI del empleado", example = "70567890", required = true) @PathVariable("dni") @Size(min = 8, max = 8, message = "EL DNI DEBE TENER UN TAMAÑO FIJO DE 8 CARACTERES") @Pattern(regexp = "\\d{8}", message = "EL DNI DEBE CONTENER EXACTAMENTE 8 DÍGITOS NUMÉRICOS") String dni) {
                return employeeService.getByDni(dni);
        }

        /* 1 - "ACTIVE" ; 2 - "VACATION" ; 3 - "REST"; 4 - "INACTIVE" */
        // PATCH /api/employee/{dni}/{code}
        @Operation(summary = "Actualizar estado del empleado", description = "Actualiza el estado de un empleado activo utilizando su DNI y el código del nuevo estado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Estado del empleado actualizado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El DNI debe tener 8 digitos numericos"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @PatchMapping("update_status_employee/{dni}/{code}")
        public EmployeeResponse updateEmployeeState(
                        @Parameter(description = "DNI del empleado", example = "70567890", required = true) @PathVariable("dni") @Size(min = 8, max = 8) @Pattern(regexp = "\\d{8}") String dni,
                        @Parameter(description = "Código del nuevo estado del empleado. Valores permitidos: ACTIVE, VACATION, REST, INACTIVE.", example = "VACATION", required = true) @PathVariable("code") @Pattern(regexp = "ACTIVE|VACATION|REST|INACTIVE", message = "EL ESTADO DEBE SER ACTIVE, VACATION, REST O INACTIVE") String code) {

                return employeeService.updateWorkingStatus(dni, code);
        }

        /*
         * Update: estado INACTIVO ingresando fecha de fin de contrato ejemplo:
         * { "contractenddate": "2023-06-25" }
         */

        // PUT /api/employee/{dni}/{endDate}/{code}
        @Operation(summary = "Finalizar contrato de un empleado", description = "Registra la fecha de fin de contrato y cambia automáticamente al empleado a estado INACTIVE.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Contrato del empleado finalizado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El DNI o la fecha proporcionada no son válidos"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @PutMapping("end_contract_employee/{dni}/{endDate}")
        public EmployeeResponse updateEndContract(
                        @Parameter(description = "DNI del empleado", example = "70567890", required = true) @PathVariable("dni") @Size(min = 8, max = 8) @Pattern(regexp = "\\d{8}") String dni,
                        @Parameter(description = "Fecha en la que finaliza el contrato del empleado. Formato: YYYY-MM-DD.", example = "2026-12-31", required = true) @PathVariable("endDate") LocalDate dateEndContract) {

                return employeeService.updateEndContractAndUpdateInactivoAutomatic(dni, dateEndContract);
        }

        // DELETE /api/employee/{id}
        @Operation(summary = "Eliminar lógicamente un empleado", description = "Desactiva lógicamente un empleado. El registro permanece almacenado en la base de datos, pero active pasa a false.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado eliminado lógicamente correctamente"),
                        @ApiResponse(responseCode = "400", description = "El DNI no tiene un formato válido"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @DeleteMapping("delete_employee/{dni}") // Para este caso tenemos que crear un
        public String delete(
                        @Parameter(description = "DNI del empleado a eliminar", example = "70567890", required = true) @PathVariable("dni") @Size(min = 8, max = 8) @Pattern(regexp = "\\d{8}") String dni) {
                return employeeService.deleteLogico(dni);
        }

        @Operation(summary = "Reactivar empleado", description = "Reactiva lógicamente un empleado previamente eliminado. Cambia active a true.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado reactivado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El DNI debe tener exactamente 8 dígitos numéricos"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno inesperado del servidor")
        })
        @PatchMapping("reactive_employee/{dni}")
        public String activeEmployee(
                        @Parameter(description = "DNI del empleado que será reactivado", example = "70567890", required = true) @PathVariable("dni") @Size(min = 8, max = 8) @Pattern(regexp = "\\d{8}") String dni) {
                return employeeService.activarEmployeeDeleteLogico(dni);
        }

}
