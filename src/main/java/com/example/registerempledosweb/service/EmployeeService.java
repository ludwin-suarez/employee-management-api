package com.example.registerempledosweb.service;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j //se utiliza para que funcione el log.inf(); para ver los errores.
public class EmployeeService {
    private  final EmployeeRepository employeeRepository;

    public List<Employee> list(){
        //return  customerRepository.findAll();//Busca todos sin Excepción
        return  employeeRepository.findAllByState(true);
        //Busca y trae todos los de estado True
    }
    //Crear un registro
    public Employee create(EmployeeRequest request){
        //por ese parametro nos envian los datos para insertar data ala BD
        Employee employee =new Employee();
        employee.setDni(request.getDni());
        employee.setName(request.getName());
        employee.setLastname(request.getLastname());
        employee.setAge(request.getAge());
        employee.setSalary(request.getSalary());
        employee.setContractdate(request.getContractdate());
        employee.setContractenddate(request.getContractenddate());
        employee.setDescription(request.getDescription());
        employee.setEmploymentstatus(request.getEmploymentstatus());
        employee.setState(request.isState());
        // log.info("employee:{}", employee);
        return employeeRepository.save(employee);
    }
    //Consultar un registro
    public Employee getById(Long id){
        return employeeRepository.findByIdAndState(id,true).orElse(null);
        //Busca y trae que cumplen con id y estado true
    }

    //Actualizar un registro
    public Employee updateEmploymentStatus(Long id,int estate){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null && existeEstate(estate)!=true ) return null; //si existe lo vamos a actualizr
        else {
            switch (estate){

            case 1: employee.setEmploymentstatus("ACTIVO");break;
            case 2: employee.setEmploymentstatus("DE VACACIONES");break;
            case 3: employee.setEmploymentstatus("EN DESCANSO");break;
            case 4: employee.setEmploymentstatus("INACTIVO");break;
            default:  break;

                }
            }
        return employeeRepository.save(employee);//retorna el objeto actualizadoo
    }

    public boolean existeEstate(int state){
        boolean est=false;
        switch (state)
        {
            case 1: est=true;break;
            case 2: est=true;break;
            case 3: est=true;break;
            case 4: est=true;break;
            default:  est=false;break;
        }
        return est;
    }

    public Employee updateEndContract(Long id,EmployeeRequest request){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null) return null; //si existe lo vamos a actualizr
        employee.setContractenddate(request.getContractenddate());
        employee.setEmploymentstatus("INACTIVO");
        //log.info("employee:{}", employee);
        return employeeRepository.save(employee);
    }

    //Eliminar un REgistro de Manera Logica
    public String deleteLogico(Long id){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null) return "El Registro no Existe!!";
        employee.setState(false);//ACtualiza el estado
        employeeRepository.save(employee);//Guarda los datos actualizados
        return "Registro Eliminado Lógicamente"+employee.getName();
    }

}
