package com.jara.employee_management.service;

import com.jara.employee_management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.jara.employee_management.exception.ResourceNotFoundException;
import com.jara.employee_management.model.domain.Department;
import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.domain.EmployeeStatus;
import com.jara.employee_management.model.mapper.EmployeeMapper;
import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.repository.EmployeeRepository;
import com.jara.employee_management.repository.EmployeeStatusRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
//import java.util.stream.Stream;//toList() java 21, no necesita importar

@Service
@RequiredArgsConstructor
@Slf4j // se utiliza para que funcione el log.inf(); para ver los errores.
public class EmployeeServiceImpl implements EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeStatusRepository employeeStatusRepository;

    // ### METODOS DE BUSQUEDAS RAPIDAS
    private EmployeeStatus optionalEmployeeStatus(String code) {
        // 2. Buscar y retorna un objeto employeeStatus
        return employeeStatusRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("CODIGO NO ENCONTRADO: " + code));
    }

    private Department optionalDepartment(Long id, Boolean active) {
        return departmentRepository.findByIdAndActive(id, active)
                .orElseThrow(() -> new ResourceNotFoundException("ID DEPARTMENT NO ENCONTRADO: " + id));

    }

    // OK -- Devuelve lista de Empleados Activos //CORREGIDO 2026-08-21
    public List<EmployeeResponse> list() {
        return employeeRepository.findAllByActive(true).stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    // OK -- Crear un registro de empleados //CORREGIDO 2626-08-20 //FALTA PROBAR
    public List<EmployeeResponse> create(EmployeeRequest request) {
        Employee employ = EmployeeMapper.toEntity(request);
        employ.setEmployeeStatus(optionalEmployeeStatus(request.getCode()));
        employ.setDepartment(optionalDepartment(request.getDepartmentId(), true));
        // log.info("employ:{}", employ);??
        employeeRepository.save(employ);
        return employeeRepository.findAllByDniAndActive(employ.getDni(), employ.isActive()).stream()
                .map(EmployeeMapper::toResponse).collect(Collectors.toList());// java 8
    }

    // OK -- Consultar un registro List<EmployeeResponse>// CORREGIDO:2026-08-21
    public List<EmployeeResponse> getById(Long id) {
        Employee employeeTemp = employeeRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EMPLEADO NO ENCONTRADO CON ID: " + id));
        return employeeRepository.findAllByIdAndActive(employeeTemp.getId(), true)
                // .stream().map(EmployeeMapper::toResponse).collect(Collectors.toList());//java
                // 8-15
                .stream().map(EmployeeMapper::toResponse).toList();// java 16-21, funciona el toList()

    }

    // OK -- Actualizar status de un registro //UPDATE:2026-08-20
    public Employee updateWorkingStatus(Long id, String code) {
        Employee employee = employeeRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EMPLEADO NO ENCONTRADO CON ID: " + id));

        employee.setEmployeeStatus(optionalEmployeeStatus(code));// Codigo para delete
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    /*
     * OK -- UPDATE: END CONTRACT /INACTIVO STATUS/NO ACTIVO REVISAR HAY UN VACIO
     */
    public Employee updateEndContractAndUpdateInactivoAutomatic(Long id, LocalDate dateEndContract, String code) {
        Employee employee = employeeRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(" ID no encontrado " + id));

        if (dateEndContract == null) {
            throw new IllegalArgumentException(
                    "LA FECHA DE FIN DE CONTRATO ES OBLIGATORIA");
        }

        if (dateEndContract.isBefore(employee.getHireDate())) {
            throw new IllegalArgumentException(
                    "LA FECHA DE FIN DE CONTRATO NO PUEDE SER MENOR QUE LA FECHA DE CONTRATACIÓN");
        }

        EmployeeStatus status = optionalEmployeeStatus(code);
        employee.setEndDate(dateEndContract);
        employee.setActive(false);
        employee.setEmployeeStatus(status);
        return employeeRepository.save(employee);
    }

    // OK -- DELETE logically //UPDATE> 2026-08-20//2026-08-21
    public String deleteLogico(Long id) {
        Employee employee = employeeRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new ResourceNotFoundException(" ID no encontrado " + id));
        EmployeeStatus status = optionalEmployeeStatus("INACTIVE");// DELETE LOGICAMENTE
        employee.setActive(false);
        employee.setEmployeeStatus(status);
        employeeRepository.save(employee);
        return "Registro Eliminado Lógicamente" + employee.getName();
    }

}
