package com.example.registerempledosweb.controller;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.model.response.EmployeeResponse;
import com.example.registerempledosweb.service.EmployeeServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    // @GetMapping("/lista")
    private final EmployeeServiceInterface employeeServiceInterface; // inyección de dependencias
    // Listar muchos registros

    @GetMapping()
    public List<EmployeeResponse> list() {
        return employeeServiceInterface.list();
    }

    @PostMapping()
    public List<EmployeeResponse> create(@RequestBody @Validated EmployeeRequest request) {// aqui vamos a pedir al
                                                                                           // usuario data
        log.info("request: {}", request);
        return employeeServiceInterface.create(request);
    }

    // Consultar un registro
    @GetMapping("/{id}")
    public List<EmployeeResponse> getById(@PathVariable("id") Long idCustomer) {
        return employeeServiceInterface.getById(idCustomer);
    }

    // Actualizar El estado de un empleado:
    /* 1 - "ACTIVO" ; 2 - "DE VACACIONES" ; 3 - "EN DESCANSO"; 4 - "INACTIVO" */
    @PatchMapping("/{id}/{estado}")
    public Employee updateEmployeeState(@PathVariable("id") Long idEmployee, @PathVariable("estado") int estado) {
        return employeeServiceInterface.updateWorkingStatus(idEmployee, estado);
    }

    // Actualizar un registro
    /*
     * Actualizar: estado Inactivo ingresando fecha de fin de contrato ejemplo:
     * { "contractenddate": "2023-06-25" }
     */
    @PutMapping("/{id}")
    public Employee updateEndContract(@PathVariable("id") Long idEmployee, @RequestBody EmployeeRequest request) {
        return employeeServiceInterface.updateEndContractAndUpdateInactivoAutomatic(idEmployee, request);
    }

    // Eliminar un registro Lógicamente
    @DeleteMapping("/{id}") // Para este caso tenemos que crear un
    public String delete(@PathVariable("id") Long idEmployee) {
        return employeeServiceInterface.deleteLogico(idEmployee);
    }

    @GetMapping("/report/{workingstatus}")
    public List<EmployeeResponse> reportWorkingStatus(@PathVariable("workingstatus") Integer workingstatus) {
        return employeeServiceInterface.getReportWorkingStatus(workingstatus);
    }

}
