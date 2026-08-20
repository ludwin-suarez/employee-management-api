package com.jara.employee_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.jara.employee_management.exception.ResourceNotFoundException;
import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.domain.EmployeeStatus;
import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.model.statusenum.EmployeeWorkingStatus;
import com.jara.employee_management.repository.EmployeeRepository;
import com.jara.employee_management.repository.EmployeeStatusRepository;

import java.lang.Thread.State;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // se utiliza para que funcione el log.inf(); para ver los errores.
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeStatusRepository employeeStatusRepository;
    // private final EmployeeWorkingStatus employeeWorkingStatus;

    // ### METODOS DE BUSQUEDAS RAPIDAS
    public boolean existeCode(String code) {
        boolean est = employeeStatusRepository.existsByCode(code);
        return est;
    }

    public Long idEmployeeStatus(String code) {
        return employeeStatusRepository.findByCode(code)
                .map(EmployeeStatus::getId)
                .orElseThrow(() -> new ResourceNotFoundException("CODIGO NO ENCONTRADO: " + code));
    }

    public EmployeeStatus optionalEmployeeStatus(String code) {
        // 2. Buscar y retorna un objeto employeeStatus
        return employeeStatusRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("CODIGO NO ENCONTRADO: " + code));
    }

    // ############

    public List<EmployeeResponse> list() {
        // return customerRepository.findAll();//Busca todos sin Excepción
        return employeeRepository.findAllByState(true).stream().map(employee -> {
            return mapToResponse(employee);
        }).collect(Collectors.toList());
        // Busca y trae todos los de estado True
    }

    // Crear un registro de empleados //CORREGIDO 2626-08-20 //FALTA PROBAR
    public List<EmployeeResponse> create(EmployeeRequest request) {
        Employee employ = new Employee();
        employ.setDni(request.getDni());
        employ.setName(request.getName());
        employ.setLastname(request.getLastName());
        employ.setEmail(request.getEmail());
        employ.setBirthDate(request.getBirthDate());
        employ.setSalary(request.getSalary());
        employ.setHireDate(request.getHireDate());// esta vacio?
        employ.setEndDate(request.getEndDate());
        employ.setDescription(request.getDescription());
        employ.setStatusId(optionalEmployeeStatus(request.getCode()));
        employ.setActive(true);
        employ.setCreatedAt(LocalDateTime.now());
        employ.setUpdatedAt(LocalDateTime.now());
        // log.info("employ:{}", employ);
        employeeRepository.save(employ);
        return employeeRepository.findAllByDniAndState(employ.getDni(), employ.isActive()).stream().map(employee -> {
            return mapToResponse(employee);
        }).collect(Collectors.toList());
    }

    // Consultar un registro List<EmployeeResponse>
    public List<EmployeeResponse> getById(Long id) {

        Employee employeeTemp = employeeRepository.findByIdAndState(id, true).orElse(null);
        if (employeeTemp == null)
            return null;
        else {
            return employeeRepository.findAllByIdAndState(employeeTemp.getId(), true)
                    .stream().map(employee -> {
                        return mapToResponse(employee);
                    }).collect(Collectors.toList());
        }
    }

    // Actualizar un registro //UPDATE:2026-08-20
    public Employee updateWorkingStatus(Long id, String code) {
        Employee employee = employeeRepository.findByIdAndState(id, true).orElse(null);
        if (employee == null && existeCode(code) != true) // Si no hay id de empleado y estado no existe, retornar null
            return null;
        else {
            employee.setStatusId(optionalEmployeeStatus(code));
        }
        return employeeRepository.save(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {

        // EXPLICACION VIDEO: 2023-06-29- 1HR CON 15 MIN-- HAY 2 MANERAS DE LA 2DA FORMA
        // 2DA forma de usar el Enum -- usando array y map, filter
        StringBuilder stringBuilder = new StringBuilder(); // OJo el equal solo funciona con Integer no con int,

        // se tiene que cambiar de la clase ENUM a directo de dase datos
        Arrays.stream(EmployeeWorkingStatus.values())
                .filter(stateEnum -> stateEnum.getId().equals(employee.getStatusId()))
                .findFirst().ifPresent(stateEnum -> stringBuilder.append(stateEnum.getWorkingStatus()));

        return EmployeeResponse.builder()
                // .id(employee.getId())
                .dni(employee.getDni())
                .name(employee.getName())
                .lastname(employee.getLastname())
                .dateofbirth(employee.getBirthDate())
                .salary(employee.getSalary())
                .contractdate(employee.getContractdate())
                .contractenddate(employee.getContractenddate())
                .description(employee.getDescription())
                // Primera forma --- Haciendo switch() case...
                // .workingstatus(obtenerWorkingStatusInString(employee.getWorkingstatus()))//llamando
                // 1ra manera
                .workingstatus(stringBuilder.toString())// llamando a la 2da manera
                .build();
    }

    /* Actualizar: estado Inactivo ingresando fecha de fin de contrato */
    public Employee updateEndContractAndUpdateInactivoAutomatic(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findByIdAndState(id, true).orElse(null);
        Date fechaactual = new Date(System.currentTimeMillis());
        if (employee == null && request.getContractenddate().compareTo(fechaactual) < 0 &&
                employee.getContractdate().compareTo(request.getContractenddate()) > 0)
            return null; // si existe lo vamos a actualizr
        else {
            // NO va a poder ingresar fecha menor a fecha registro--
            // fecha ingresada comparada con la fecha actual: 1 es menor, 0 es igual, -1 es
            // mayor
            employee.setContractenddate(request.getContractenddate());
            employee.setWorkingstatus(4);
            return employeeRepository.save(employee);
        }
    }

    // 4-12-25-31-37-44
    // 4, 31, 38, 43 y 45
    // 12, 19, 25, 33, 35, 37 y 44.
    // Eliminar un REgistro de Manera Logica //UPDATE> 2026-08-20
    public String deleteLogico(Long id) {
        Employee employee = employeeRepository.findByIdAndState(id, true).orElse(null);
        if (employee == null)
            return "El Registro no Existe!!";
        employee.setActive(false);// ACtualiza el estado
        employeeRepository.save(employee);// Guarda los datos actualizados
        return "Registro Eliminado Lógicamente" + employee.getName();
    }

    // public
    public List<EmployeeResponse> getReportWorkingStatus(Integer workingstatus) {
        return employeeRepository.findAllByWorkingstatusAndState(workingstatus, true).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

}
