package com.jara.employee_management.service;

import com.jara.employee_management.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jara.employee_management.exception.BusinessException;
import com.jara.employee_management.exception.ResourceNotFoundException;
import com.jara.employee_management.exception.DuplicateResourceException;
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
//import java.util.stream.Collectors;//java 8-15
//import java.util.stream.Stream;//toList() java 21, no necesita importar

@Service
@RequiredArgsConstructor
@Slf4j // se utiliza para que funcione el log.inf(); para ver los errores.
public class EmployeeServiceImpl implements EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeStatusRepository employeeStatusRepository;

    // =========================================================
    // MÉTODOS AUXILIARES
    private EmployeeStatus optionalEmployeeStatus(String code) {
        // 2. Buscar y retorna un objeto employeeStatus
        return employeeStatusRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("CODIGO NO ENCONTRADO: " + code));
    }

    private Department optionalDepartment(Long id, Boolean active) {
        return departmentRepository.findByIdAndActive(id, active)
                .orElseThrow(() -> new ResourceNotFoundException("ID DEPARTMENT NO ENCONTRADO: " + id));

    }

    private boolean searchDni(String dni) {
        List<Long> estadosValidos = List.of(1L, 2L, 3L, 4L, 5L);
        return employeeRepository.existsByDniAndStatusIdIn(dni, estadosValidos);

    }
    // =========================================================

    // LIST EMPLOYEES
    @Transactional(readOnly = true)
    @Override
    public List<EmployeeResponse> list() {
        return employeeRepository.findAllByActive(true).stream()
                .map(EmployeeMapper::toResponse).toList();
        // .collect(Collectors.toList());
    }

    // CREATE EMPLOYEE
    @Transactional
    @Override
    public EmployeeResponse create(EmployeeRequest request) {

        if (searchDni(request.getDni()) == true) {
            throw new DuplicateResourceException(
                    "EL EMPLEADO YA SE ENCUENTRA REGISTRADO, SOLICITAR A SOPORTE TÉCNICO SU REACTIVACIÓN");
        }

        Employee employ = EmployeeMapper.toEntity(request);
        employ.setEmployeeStatus(optionalEmployeeStatus(request.getCode()));
        employ.setDepartment(optionalDepartment(request.getDepartmentId(), true));
        employeeRepository.save(employ);
        return employeeRepository.findByDniAndActive(employ.getDni(), employ.isActive())
                .map(EmployeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EMPLEADO NO ENCONTRADO CON DNI: " + employ.getDni()));
    }

    // SEARCH EMPLOYEE FOR ID
    @Transactional(readOnly = true) // work with @ManyToOne(fetch = FetchType.LAZY)
    @Override // Caso contrario se cierra la sesion antes de mapear la data
    public EmployeeResponse getByDni(String dni) {
        return employeeRepository.findByDniAndActive(dni, true).map(EmployeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EMPLEADO NO ENCONTRADO CON DNI: " + dni));

        // .stream().map(EmployeeMapper::toResponse).collect(Collectors.toList());//java
        // 8-15
        // .stream().map(EmployeeMapper::toResponse).toList();// java 16-21, funciona el
        // toList()

    }

    // UPDATE STATUS EMPLOYEE, SEARCH FOR ID AND CODE
    @Transactional()
    @Override
    public EmployeeResponse updateWorkingStatus(String dni, String code) {
        Employee employee = employeeRepository.findByDniAndActive(dni, true)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "EMPLEADO NO ENCONTRADO CON DNI: " + dni));

        employee.setEmployeeStatus(optionalEmployeeStatus(code));// Codigo para delete
        employee.setUpdatedAt(LocalDateTime.now());

        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    // UPDATE CONTRACT END DATE POR DNI AND ENDDATE
    @Transactional()
    @Override
    public EmployeeResponse updateEndContractAndUpdateInactivoAutomatic(String dni, LocalDate dateEndContract) {
        Employee employee = employeeRepository.findByDniAndActive(dni, true)
                .orElseThrow(() -> new ResourceNotFoundException("EMPLEADO NO ENCONTRADO CON DNI: " + dni));

        if (dateEndContract == null) {
            throw new BusinessException(
                    "LA FECHA DE FIN DE CONTRATO ES OBLIGATORIA");
        }

        if (dateEndContract.isBefore(employee.getHireDate())) {
            throw new BusinessException(
                    "LA FECHA DE FIN DE CONTRATO NO PUEDE SER MENOR QUE LA FECHA DE CONTRATACIÓN");
            // throw new IllegalArgumentException(
            // "LA FECHA DE FIN NO PUEDE SER MENOR QUE LA FECHA DE CONTRATACIÓN");
        }

        ;
        employee.setEndDate(dateEndContract);
        // employee.setActive(false);
        employee.setEmployeeStatus(optionalEmployeeStatus("INACTIVE"));
        employee.setUpdatedAt(LocalDateTime.now());
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    // DELETE LOGICALLY
    @Override
    public String deleteLogico(String dni) {
        Employee employee = employeeRepository.findByDniAndActive(dni, true)
                .orElseThrow(() -> new ResourceNotFoundException(" EMPLEADO NO ENCONTRADO CON DNI: " + dni));
        EmployeeStatus status = optionalEmployeeStatus("DELET");// DELETE LOGICAMENTE
        employee.setActive(false);
        employee.setEmployeeStatus(status);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        return "Registro Eliminado Lógicamente, DNI :" + employee.getDni() + " NOMBRE: " + employee.getName();
    }

    @Transactional
    @Override
    public String activarEmployeeDeleteLogico(String dni) {
        Employee employee = employeeRepository.findByDniAndStatusCodeAndActive(dni, "DELET", false)
                .orElseThrow(() -> new ResourceNotFoundException(" EMPLEADO NO ENCONTRADO CON DNI : " + dni));
        EmployeeStatus status = optionalEmployeeStatus("ACTIVE");
        employee.setActive(true);
        employee.setEmployeeStatus(status);
        employee.setHireDate(LocalDate.now());
        employee.setEndDate(null);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        return "SE HA REACTIVADO EMPLEADO CON DNI: " + employee.getDni() + " NOMBRE: " + employee.getName();
    }

}
