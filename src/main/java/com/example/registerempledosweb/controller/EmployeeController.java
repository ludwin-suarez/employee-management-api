package com.example.registerempledosweb.controller;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.service.EmployeeService;
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
    //@GetMapping("/lista")
    private final EmployeeService employeeService; //inyección de dependencias
    //Listar muchos registros
    @GetMapping()
    public List<Employee> list(){
        return employeeService.list();
    }

    @PostMapping()
    public Employee create(@RequestBody @Validated EmployeeRequest request){//aqui vamos a pedir al usuario data
        log.info("request: {}", request);
        return employeeService.create(request);
    }
    //Consultar un registro
    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Long idCustomer){
        return employeeService.getById(idCustomer);
    }
    //Actualizar un registro
    @PutMapping("/{id}/{estado}")
    public Employee updateEmployeeState(@PathVariable("id") Long idEmployee,@PathVariable("estado") int estado){
        return employeeService.updateEmploymentStatus(idEmployee,estado);
    }
    //Actualizar un registro
    @PutMapping("/{id}")
    public Employee updateEndContract(@PathVariable("id") Long idEmployee,@RequestBody EmployeeRequest request){
        return employeeService.updateEndContract(idEmployee,request);
    }
    //Eliminar un registro Lógicamente
    @DeleteMapping("/{id}")//Para este caso tenemos que crear un
    public String delete(@PathVariable("id") Long idEmployee){
        return employeeService.deleteLogico(idEmployee);
    }
}
