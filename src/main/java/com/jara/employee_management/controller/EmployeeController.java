package com.jara.employee_management.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    // @GetMapping("/lista")
    private final EmployeeService employeeServiceInterface; // inyección de dependencias

    // OK -- Listar muchos registros
    @GetMapping()
    public List<EmployeeResponse> list() {
        return employeeServiceInterface.list();
    }

    // OK -- Create Employee
    @PostMapping()
    public List<EmployeeResponse> create(@RequestBody @Validated EmployeeRequest request) {// aqui vamos a pedir al
                                                                                           // usuario data
        log.info("request: {}", request);
        return employeeServiceInterface.create(request);
    }

    // OK --Search an Employee
    @GetMapping("/{id}")
    public List<EmployeeResponse> getById(@PathVariable("id") Long idCustomer) {
        return employeeServiceInterface.getById(idCustomer);
    }

    // OK-- Actualizar El estado de un empleado:
    /* 1 - "ACTIVE" ; 2 - "VACATION" ; 3 - "REST"; 4 - "INACTIVE" */
    @PatchMapping("/{id}/{code}")
    public Employee updateEmployeeState(@PathVariable("id") Long idEmployee, @PathVariable("code") String code) {
        return employeeServiceInterface.updateWorkingStatus(idEmployee, code);
    }

    /*
     * Update: estado INACTIVO ingresando fecha de fin de contrato ejemplo:
     * { "contractenddate": "2023-06-25" }
     */
    @PutMapping("/{id}/{endDate}/{code}")
    public Employee updateEndContract(@PathVariable("id") Long idEmployee,
            @PathVariable("endDate") LocalDate dateEndContract, @PathVariable("code") String code) {
        return employeeServiceInterface.updateEndContractAndUpdateInactivoAutomatic(idEmployee, dateEndContract, code);
    }

    // Delete an employee Lógicamente
    @DeleteMapping("/{id}") // Para este caso tenemos que crear un
    public String delete(@PathVariable("id") Long idEmployee) {
        return employeeServiceInterface.deleteLogico(idEmployee);
    }

}
