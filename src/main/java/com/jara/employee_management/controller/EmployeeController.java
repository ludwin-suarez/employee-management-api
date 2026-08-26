package com.jara.employee_management.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

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
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente")
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
                        @ApiResponse(responseCode = "409", description = "El DNI del empleado ya existe")
        })
        @PostMapping("/create_employee")
        public List<EmployeeResponse> create(
                        @RequestBody @Valid EmployeeRequest request) {
                log.info("request: {}", request);
                return employeeService.create(request);
        }

        // GET /api/employee/{id}
        @Operation(summary = "Buscar empleado por ID", description = "Obtiene un empleado activo mediante su identificador.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado encontrado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El ID debe ser mayor que 0"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        })
        @GetMapping("/search_employee/{id}")
        public EmployeeResponse getById(
                        @Parameter(description = "Identificador único del empleado", example = "92", required = true) @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee) {
                return employeeService.getById(idEmployee);
        }

        /* 1 - "ACTIVE" ; 2 - "VACATION" ; 3 - "REST"; 4 - "INACTIVE" */
        // PATCH /api/employee/{id}/{code}
        @Operation(summary = "Actualizar estado del empleado", description = "Actualiza el estado de un empleado activo utilizando su ID y el código del nuevo estado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado encontrado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El ID debe ser mayor que 0"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        })
        @PatchMapping("update_status_employee/{id}/{code}")
        public EmployeeResponse updateEmployeeState(
                        @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee,
                        @Parameter(description = "Código del nuevo estado del empleado. Valores permitidos: ACTIVE, VACATION, REST, INACTIVE.", example = "VACATION", required = true) @PathVariable("code") String code) {
                return employeeService.updateWorkingStatus(idEmployee, code);
        }

        /*
         * Update: estado INACTIVO ingresando fecha de fin de contrato ejemplo:
         * { "contractenddate": "2023-06-25" }
         */

        // PUT /api/employee/{id}/{endDate}/{code}
        @Operation(summary = "Finalizar contrato de un empleado", description = "Registra la fecha de fin de contrato y cambia automáticamente al empleado a estado INACTIVE.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado encontrado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El ID debe ser mayor que 0"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        }) // FALTA PARA FECHA
        @PutMapping("end_contract_employee/{id}/{endDate}")
        public EmployeeResponse updateEndContract(
                        @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee,
                        @Parameter(description = "Fecha de finalización del contrato", example = "2026-12-31", required = true) @PathVariable("endDate") LocalDate dateEndContract) {
                return employeeService.updateEndContractAndUpdateInactivoAutomatic(idEmployee, dateEndContract);
        }

        // DELETE /api/employee/{id}
        @Operation(summary = "Eliminar lógicamente un empleado", description = "Desactiva lógicamente un empleado. El registro permanece almacenado en la base de datos, pero su campo active pasa a false.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Empleado eliminado correctamente"),
                        @ApiResponse(responseCode = "400", description = "El ID debe ser mayor que 0"),
                        @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        })
        @DeleteMapping("delete_employee/{id}") // Para este caso tenemos que crear un
        public String delete(
                        @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee) {
                return employeeService.deleteLogico(idEmployee);
        }

}
