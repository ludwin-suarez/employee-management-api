package com.example.registerempledosweb.controller;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //Crear un registro
    //@RequestBody : Sirve para que reconozca al modelo con todas sus variables como
    // parte del body de la solicitud de lo que andamos enviando
    @PostMapping()
    public Employee create(@RequestBody EmployeeRequest request){//aqui vamos a pedir al usuario data
        log.info("request: {}", request);
        return employeeService.create(request);
    }
    //Consultar un registro
    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") Long idCustomer){
        return employeeService.getById(idCustomer);
    }
/* @GetMapping("/{id}") //TAMBIEN FUNCIONA ASÍ
    public Customer getById(@PathVariable("id") Long id){
       return customerService.getById(id);
    }*/

    //Actualizar un registro
    @PutMapping("/{id}")
    public Employee update(@PathVariable("id") Long idEmployee,@RequestBody EmployeeRequest request){
        return employeeService.update(idEmployee,request);

    }
    /*//Eliminar un registro Definitivamente
    @DeleteMapping
    public String delete(@PathVariable("id") Long idCustomer){
       return customerService.deleteFisico(idCustomer);
    }*/

    //Eliminar un registro Lógicamente
    @DeleteMapping//Para este caso tenemos que crear un
    public String delete(@PathVariable("id") Long idEmployee){
        return employeeService.deleteLogico(idEmployee);
    }
}
