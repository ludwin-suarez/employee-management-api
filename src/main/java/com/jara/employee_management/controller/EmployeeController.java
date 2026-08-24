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

@RestController
@Validated // Valida var de parametros y metodos de controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService; // inyección de dependencias

    // GET /api/employee
    @GetMapping("/listar")
    public List<EmployeeResponse> list() {
        return employeeService.list();
    }

    // POST /api/employee
    @PostMapping()
    public List<EmployeeResponse> create(
            @RequestBody @Valid EmployeeRequest request) {
        log.info("request: {}", request);
        return employeeService.create(request);
    }

    // GET /api/employee/{id}
    @GetMapping("/{id}")
    public EmployeeResponse getById(
            @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee) {
        return employeeService.getById(idEmployee);
    }

    /* 1 - "ACTIVE" ; 2 - "VACATION" ; 3 - "REST"; 4 - "INACTIVE" */
    // PATCH /api/employee/{id}/{code}
    @PatchMapping("/{id}/{code}")
    public EmployeeResponse updateEmployeeState(
            @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee,
            @PathVariable String code) {
        return employeeService.updateWorkingStatus(idEmployee, code);
    }

    /*
     * Update: estado INACTIVO ingresando fecha de fin de contrato ejemplo:
     * { "contractenddate": "2023-06-25" }
     */

    // PUT /api/employee/{id}/{endDate}/{code}
    @PutMapping("/{id}/{endDate}")
    public EmployeeResponse updateEndContract(
            @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee,
            @PathVariable("endDate") LocalDate dateEndContract) {
        return employeeService.updateEndContractAndUpdateInactivoAutomatic(idEmployee, dateEndContract);
    }

    // DELETE /api/employee/{id}
    @DeleteMapping("/{id}") // Para este caso tenemos que crear un
    public String delete(
            @PathVariable("id") @Positive(message = "EL ID DEL EMPLEADO DEBE SER MAYOR QUE 0") Long idEmployee) {
        return employeeService.deleteLogico(idEmployee);
    }

}
