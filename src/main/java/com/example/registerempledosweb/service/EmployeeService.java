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
        //Encuentre por el id.. sino le encuentra devuelve null
        //Opcional.. clase jav a....Permite manejar nulos, el repository lo mete en un opcional y
        //te dja a tí para que lo manejes.. por eso el orElse
        //return customerRepository.findById(id).orElse(null);//Busca y trae sin importar el estado
        return employeeRepository.findByIdAndState(id,true).orElse(null);//Busca y trae que cumplen con id y estado true
    }

    //Actualizar un registro
    public Employee update(Long id,EmployeeRequest request){
        //por ese parametro nos envian los datos para insertar data ala BD y ademas tenemos
        //que consultar que exista enla BD con el id
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null) return null; //si existe lo vamos a actualizr
        employee.setDni(request.getDni());
        employee.setName(request.getName());
        employee.setLastname(request.getLastname());
        employee.setAge(request.getAge());
        employee.setSalary(request.getSalary());
        employee.setContractdate(request.getContractdate());
        employee.setDescription(request.getDescription());
        employee.setEmploymentstatus(request.getEmploymentstatus());
        employee.setState(request.isState());
        //log.info("employee:{}", employee);
        return employeeRepository.save(employee);//retorna el objeto actualizadoo
    }
    //Eliminar un registro Definitivamente
   /* public String deleteFisico(Long id){ //ELIMINA EL REGISTRO DEFINITIVAMENTE
        Employee employee =employeeRepository.findById(id).orElse(null);
        if(employee==null) return "El Registro no Existe!!";
        employeeRepository.deleteById(id);
        return "Registro Eliminado Definitivamente"+employee.getName();
    } */
    //Eliminar un REgistro de Manera Logica
    public String deleteLogico(Long id){ //Para ello tenemos que crear un
        /* Vammos a consultar a la BD, ya no se va a buscar por findById. por lo
         * que vamos a repository y vamos a crear un metodo que permita buscar por ID*/
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        //Aquí se busca con el metodo creado y solo se busca los que tienen estado true y exista el id
        if(employee==null) return "El Registro no Existe!!";
        //Ahora para actualizar tambien vamos a crear un metodo en Repositry  para actualizar
        employee.setState(false);//ACtualiza el estado
        employeeRepository.save(employee);//Guarda los datos actualizados
        return "Registro Eliminado Lógicamente"+employee.getName();
    }

}
