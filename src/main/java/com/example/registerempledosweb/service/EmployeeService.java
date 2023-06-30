package com.example.registerempledosweb.service;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.model.response.EmployeeResponse;
import com.example.registerempledosweb.model.statusenum.EmployeeWorkingStatus;
import com.example.registerempledosweb.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j //se utiliza para que funcione el log.inf(); para ver los errores.
public class EmployeeService implements EmployeeServiceInterface{
    private  final EmployeeRepository employeeRepository;
    //private  final EmployeeWorkingStatus employeeWorkingStatus;

    public List<EmployeeResponse> list(){
        //return  customerRepository.findAll();//Busca todos sin Excepción
        return  employeeRepository.findAllByState(true).stream().map(employee -> {
                    return mapToResponse(employee);
        }).collect(Collectors.toList());
        //Busca y trae todos los de estado True
    }
    //Crear un registro
    public List<EmployeeResponse> create(EmployeeRequest request){
        //por ese parametro nos envian los datos para insertar data ala BD
        Employee employ =new Employee();
        employ.setDni(request.getDni());
        employ.setName(request.getName());
        employ.setLastname(request.getLastname());
        employ.setDateofbirth(request.getDateofbirth());
        employ.setSalary(request.getSalary());
        employ.setContractdate(request.getContractdate());
        employ.setContractenddate(request.getContractenddate());
        employ.setDescription(request.getDescription());
        employ.setWorkingstatus(request.getWorkingstatus());//comprobar y obtener el valor antes de insertar
        employ.setState(true);
        // log.info("employ:{}", employ);
        employeeRepository.save(employ);
        return employeeRepository.findAllByDniAndState(employ.getDni(),employ.isState()).stream().map(employee -> {
            return mapToResponse(employee);
        }).collect(Collectors.toList());
    }
    //Consultar un registro  List<EmployeeResponse>
    public List<EmployeeResponse> getById(Long id){

        Employee employeeTemp=employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employeeTemp==null) return null; //si existe lo vamos a actualizr
        else{
          return  employeeRepository.findAllByIdAndState(employeeTemp.getId(),true).stream().map(employee -> {
              return mapToResponse(employee);
          }).collect(Collectors.toList());
        } //Busca y trae que cumplen con id y estado true
    }

    //Actualizar un registro
    /* 1 - "ACTIVO" ; 2 - "DE VACACIONES" ; 3 - "EN DESCANSO"; 4 - "INACTIVO"*/
    public Employee updateWorkingStatus(Long id, int state){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null && existeEstate(state)!=true ) return null;
        else {
            switch (state){

            case 1: employee.setWorkingstatus(1);break;
            case 2: employee.setWorkingstatus(2);break;
            case 3: employee.setWorkingstatus(3);break;
            case 4: employee.setWorkingstatus(4);break;
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

    private EmployeeResponse mapToResponse(Employee employee) {

        //EXPLICACION VIDEO: 2023-06-29- 1HR CON 15 MIN-- HAY 2 MANERAS DE LA 2DA FORMA
        //2DA forma de usar el Enum -- usando array y map, filter
        StringBuilder stringBuilder=new StringBuilder(); //OJo el equal solo funciona con Integer no con int,
        Arrays.stream(EmployeeWorkingStatus.values()).filter(stateEnum->stateEnum.getId().equals(employee.getWorkingstatus()))
                .findFirst().ifPresent(stateEnum->stringBuilder.append(stateEnum.getWorkingStatus()));

        return EmployeeResponse.builder()
               // .id(employee.getId())
                .dni(employee.getDni())
                .name(employee.getName())
                .lastname(employee.getLastname())
                .dateofbirth(employee.getDateofbirth())
                .salary(employee.getSalary())
                .contractdate(employee.getContractdate())
                .contractenddate(employee.getContractenddate())
                .description(employee.getDescription())
            // Primera forma --- Haciendo switch() case...
              //  .workingstatus(obtenerWorkingStatusInString(employee.getWorkingstatus()))//llamando 1ra manera
                .workingstatus(stringBuilder.toString())//llamando a la 2da manera
                .build();
    }

    private String obtenerWorkingStatusInString(int workingstatus){
        String status="";
        switch (workingstatus)
        {
            case 1: status=EmployeeWorkingStatus.ACTIVE.getWorkingStatus();break;
            case 2: status=EmployeeWorkingStatus.ON_VACATION.getWorkingStatus();break;
            case 3: status=EmployeeWorkingStatus.AT_REST.getWorkingStatus();break;
            case 4: status=EmployeeWorkingStatus.INACTIVE.getWorkingStatus();break;
            default: status="";break;
        }
      return status;
    }

    /* Actualizar: estado Inactivo ingresando fecha de fin de contrato*/
    public Employee updateEndContractAndUpdateInactivoAutomatic(Long id,EmployeeRequest request){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        Date fechaactual = new Date(System.currentTimeMillis());
        if(employee==null &&request.getContractenddate().compareTo(fechaactual)< 0 &&
                employee.getContractdate().compareTo(request.getContractenddate())>0) return null; //si existe lo vamos a actualizr
        else {
               //NO va a poder ingresar fecha menor a fecha registro--
            // fecha ingresada comparada con la fecha actual: 1 es menor, 0 es igual, -1 es mayor
                employee.setContractenddate(request.getContractenddate());
                employee.setWorkingstatus(4);
            return employeeRepository.save(employee);
        }
    }
    //4-12-25-31-37-44
// 4, 31, 38, 43 y 45
   //  12, 19, 25, 33, 35, 37 y 44.
    //Eliminar un REgistro de Manera Logica
    public String deleteLogico(Long id){
        Employee employee =employeeRepository.findByIdAndState(id,true).orElse(null);
        if(employee==null) return "El Registro no Existe!!";
        employee.setState(false);//ACtualiza el estado
        employeeRepository.save(employee);//Guarda los datos actualizados
        return "Registro Eliminado Lógicamente"+employee.getName();
    }

    //public
    public List<EmployeeResponse> getReportWorkingStatus(Integer workingstatus){
                return employeeRepository.findAllByWorkingstatusAndState(workingstatus,true).stream().
                        map(this::mapToResponse).collect(Collectors.toList());
    }

}
