package com.jara.employee_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.argThat;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.exception.BusinessException;
import com.jara.employee_management.exception.ResourceNotFoundException;
import com.jara.employee_management.model.domain.Department;
import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.domain.EmployeeStatus;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.repository.DepartmentRepository;
import com.jara.employee_management.repository.EmployeeRepository;
import com.jara.employee_management.repository.EmployeeStatusRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
        @Mock
        private DepartmentRepository departmentRepository;

        @Mock
        private EmployeeRepository employeeRepository;

        @Mock
        private EmployeeStatusRepository employeeStatusRepository;

        @InjectMocks
        private EmployeeServiceImpl employeeService;

        // CONVENCION 3 AAA PARA ORG. LAS PRUEBA JUnit:
        // Arrange: Preparar, Act:Ejecutar, Assert: Comprobar
        // PRUEBAS DE METODO LISTAR EMPLEADOS
        @Test
        void debeListarEmpleadosActivos() {

                // Arrange
                when(employeeRepository.findAllByActive(true))
                                .thenReturn(java.util.List.of());// Devuelve list vacia.
                // ¿Qué ocurre cuando no existen empleados activos para listar?

                // Act
                var resultado = employeeService.list();

                // Assert
                assertNotNull(resultado);// El método employeeService.list() no debe devolver null.
                assertTrue(resultado.isEmpty());

                verify(employeeRepository).findAllByActive(true);// Verifica la llamada a repository
        }

        @Test
        void debeRetornarEmpleadosActivos() {

                // Arrange
                EmployeeStatus status = new EmployeeStatus();
                status.setId(1L);
                status.setCode("ACTIVE");
                status.setName("ACTIVO");

                Department department = new Department();
                department.setId(1L);
                department.setName("Tecnología");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setEmail("juan.suarez@gmail.com");
                employee.setBirthDate(LocalDate.of(1996, 8, 1));
                employee.setSalary(new BigDecimal("2500.00"));
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setEndDate(null);
                employee.setDescription("Asignado al área de Tecnología");
                employee.setEmployeeStatus(status);
                employee.setDepartment(department);
                employee.setActive(true);

                when(employeeRepository.findAllByActive(true))
                                .thenReturn(List.of(employee));

                // Act
                var resultado = employeeService.list();

                // Assert
                assertNotNull(resultado);
                assertFalse(resultado.isEmpty());// Empty es vacio
                assertEquals(1, resultado.size());

                EmployeeResponse response = resultado.get(0);

                assertEquals("70567890", response.getDni());
                assertEquals("Juan", response.getName());
                assertEquals("Suarez", response.getLastName());
                assertEquals("juan.suarez@gmail.com", response.getEmail());
                assertEquals(LocalDate.of(1996, 8, 1), response.getBirthDate());
                assertEquals(new BigDecimal("2500.00"), response.getSalary());
                assertEquals(LocalDate.of(2026, 8, 24), response.getHireDate());
                assertNull(response.getEndDate());
                assertEquals("Asignado al área de Tecnología", response.getDescription());

                assertNotNull(response.getEmployeeStatus());
                assertEquals(1L, response.getEmployeeStatus().getId());
                assertEquals("ACTIVE", response.getEmployeeStatus().getCode());
                assertEquals("ACTIVO", response.getEmployeeStatus().getName());

                assertNotNull(response.getDepartment());
                assertEquals(1L, response.getDepartment().getId());
                assertEquals("Tecnología", response.getDepartment().getName());

                assertTrue(response.getActive());

                verify(employeeRepository).findAllByActive(true);
        }

        // PRUEBAS PARA MÉTODO DE CREAR UN EMPLEADO
        @Test
        void debeCrearEmpleadoCorrectamente() {

                // Arrange
                EmployeeRequest request = new EmployeeRequest();
                request.setDni("70567890");
                request.setName("Juan");
                request.setLastName("Suarez");
                request.setEmail("juan.suarez@gmail.com");
                request.setBirthDate(LocalDate.of(1996, 8, 1));
                request.setSalary(new BigDecimal("2500.00"));
                request.setHireDate(LocalDate.of(2026, 8, 24));
                request.setEndDate(null);
                request.setDescription("Asignado al área de Tecnología");
                request.setCode("ACTIVE");
                request.setDepartmentId(1L);

                EmployeeStatus status = new EmployeeStatus();
                status.setId(1L);
                status.setCode("ACTIVE");
                status.setName("ACTIVO");

                Department department = new Department();
                department.setId(1L);
                department.setName("Tecnología");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setEmail("juan.suarez@gmail.com");
                employee.setBirthDate(LocalDate.of(1996, 8, 1));
                employee.setSalary(new BigDecimal("2500.00"));
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setEndDate(null);
                employee.setDescription("Asignado al área de Tecnología");
                employee.setEmployeeStatus(status);
                employee.setDepartment(department);
                employee.setActive(true);

                when(employeeRepository.existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList()))
                                .thenReturn(false);

                when(employeeStatusRepository.findByCode("ACTIVE"))
                                .thenReturn(Optional.of(status));

                when(departmentRepository.findByIdAndActive(1L, true))
                                .thenReturn(Optional.of(department));

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));

                // Act

                EmployeeResponse resultado = employeeService.create(request);

                // Assert

                assertNotNull(resultado);
                assertEquals("70567890", resultado.getDni());
                assertEquals("Juan", resultado.getName());
                assertEquals("Suarez", resultado.getLastName());
                assertEquals("juan.suarez@gmail.com", resultado.getEmail());
                assertEquals(LocalDate.of(1996, 8, 1), resultado.getBirthDate());
                assertEquals(new BigDecimal("2500.00"), resultado.getSalary());
                assertEquals(LocalDate.of(2026, 8, 24), resultado.getHireDate());
                assertNull(resultado.getEndDate());
                assertEquals("Asignado al área de Tecnología", resultado.getDescription());

                assertNotNull(resultado.getEmployeeStatus());
                assertEquals(1L, resultado.getEmployeeStatus().getId());
                assertEquals("ACTIVE", resultado.getEmployeeStatus().getCode());
                assertEquals("ACTIVO", resultado.getEmployeeStatus().getName());

                assertNotNull(resultado.getDepartment());
                assertEquals(1L, resultado.getDepartment().getId());
                assertEquals("Tecnología", resultado.getDepartment().getName());

                assertTrue(resultado.getActive());

                verify(employeeRepository).existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList());

                verify(employeeRepository).save(any(Employee.class));

                verify(employeeStatusRepository).findByCode("ACTIVE");

                verify(departmentRepository).findByIdAndActive(1L, true);

                verify(employeeRepository).findByDniAndActive("70567890", true);
        }

        @Test
        void debeLanzarExcepcionCuandoDniYaExiste() {

                EmployeeRequest request = new EmployeeRequest();
                request.setDni("70567890");

                when(employeeRepository.existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList()))
                                .thenReturn(true);

                // Act
                BusinessException exception = assertThrows(
                                BusinessException.class,
                                () -> employeeService.create(request));

                // Assert
                assertEquals(
                                "EL EMPLEADO YA SE ENCUENTRA REGISTRADO, SOLICITAR A SOPORTE TÉCNICO SU ACTIVACIÓN: HTTP:409?",
                                exception.getMessage());

                verify(employeeRepository).existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList());
        }

        @Test
        void debeLanzarExcepcionCuandoEstadoNoExiste() {

                EmployeeRequest request = new EmployeeRequest();
                request.setDni("70567890");
                request.setName("Juan");
                request.setLastName("Suarez");
                request.setEmail("juan.suarez@gmail.com");
                request.setBirthDate(LocalDate.of(1996, 8, 1));
                request.setSalary(new BigDecimal("2500.00"));
                request.setHireDate(LocalDate.of(2026, 8, 24));
                request.setEndDate(null);
                request.setDescription("Asignado al área de Tecnología");
                request.setCode("ACTIVE");
                request.setDepartmentId(1L);

                when(employeeRepository.existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList()))
                                .thenReturn(false);

                when(employeeStatusRepository.findByCode("ACTIVE"))
                                .thenReturn(java.util.Optional.empty());

                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.create(request));

                // Assert
                assertEquals("CODIGO NO ENCONTRADO: ACTIVE", exception.getMessage());

                verify(employeeRepository).existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList());

                verify(employeeStatusRepository).findByCode("ACTIVE");
        }

        @Test
        void debeLanzarExcepcionCuandoDepartamentoNoExiste() {

                // Arrange
                EmployeeRequest request = new EmployeeRequest();
                request.setDni("70567890");
                request.setName("Juan");
                request.setLastName("Suarez");
                request.setEmail("juan.suarez@gmail.com");
                request.setBirthDate(LocalDate.of(1996, 8, 1));
                request.setSalary(new BigDecimal("2500.00"));
                request.setHireDate(LocalDate.of(2026, 8, 24));
                request.setEndDate(null);
                request.setDescription("Asignado al área de Tecnología");
                request.setCode("ACTIVE");
                request.setDepartmentId(1L);

                EmployeeStatus status = new EmployeeStatus();
                status.setId(1L);
                status.setCode("ACTIVE");
                status.setName("ACTIVO");

                when(employeeRepository.existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList()))
                                .thenReturn(false);

                when(employeeStatusRepository.findByCode("ACTIVE"))
                                .thenReturn(Optional.of(status));

                when(departmentRepository.findByIdAndActive(1L, true))
                                .thenReturn(Optional.empty());

                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.create(request));

                // Assert
                assertEquals(
                                "ID DEPARTMENT NO ENCONTRADO: 1",
                                exception.getMessage());

                verify(employeeRepository).existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList());

                verify(employeeStatusRepository).findByCode("ACTIVE");

                verify(departmentRepository).findByIdAndActive(1L, true);
        }

        @Test
        void debeGuardarEmpleadoComoActivo() {

                // Arrange
                EmployeeRequest request = new EmployeeRequest();
                request.setDni("70567890");
                request.setName("Juan");
                request.setLastName("Suarez");
                request.setEmail("juan.suarez@gmail.com");
                request.setBirthDate(LocalDate.of(1996, 8, 1));
                request.setSalary(new BigDecimal("2500.00"));
                request.setHireDate(LocalDate.of(2026, 8, 24));
                request.setEndDate(null);
                request.setDescription("Asignado al área de Tecnología");
                request.setCode("ACTIVE");
                request.setDepartmentId(1L);

                EmployeeStatus status = new EmployeeStatus();
                status.setId(1L);
                status.setCode("ACTIVE");
                status.setName("ACTIVO");

                Department department = new Department();
                department.setId(1L);
                department.setName("Tecnología");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setEmail("juan.suarez@gmail.com");
                employee.setBirthDate(LocalDate.of(1996, 8, 1));
                employee.setSalary(new BigDecimal("2500.00"));
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setDescription("Asignado al área de Tecnología");
                employee.setEmployeeStatus(status);
                employee.setDepartment(department);
                employee.setActive(true);

                when(employeeRepository.existsByDniAndStatusIdIn(
                                eq("70567890"),
                                anyList()))
                                .thenReturn(false);

                when(employeeStatusRepository.findByCode("ACTIVE"))
                                .thenReturn(Optional.of(status));

                when(departmentRepository.findByIdAndActive(1L, true))
                                .thenReturn(Optional.of(department));

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));

                // Act
                employeeService.create(request);
                // Assert
                verify(employeeRepository).save(
                                argThat(employeeGuardado -> employeeGuardado.isActive()
                                                && employeeGuardado.getDni().equals("70567890")
                                                && employeeGuardado.getName().equals("Juan")
                                                && employeeGuardado.getLastName().equals("Suarez")
                                                && employeeGuardado.getEmail().equals("juan.suarez@gmail.com")));
        }

        // PRUEBAS PARA MÉTODO DE BUSCAR UN EMPLEADO POR DNI
        @Test
        void debeEncontrarEmpleadoPorDni() {

                // Arrange
                EmployeeStatus status = new EmployeeStatus();
                status.setId(1L);
                status.setCode("ACTIVE");
                status.setName("ACTIVO");

                Department department = new Department();
                department.setId(1L);
                department.setName("Tecnología");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setEmail("juan.suarez@gmail.com");
                employee.setBirthDate(LocalDate.of(1996, 8, 1));
                employee.setSalary(new BigDecimal("2500.00"));
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setEndDate(null);
                employee.setDescription("Asignado al área de Tecnología");
                employee.setEmployeeStatus(status);
                employee.setDepartment(department);
                employee.setActive(true);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                // Act
                EmployeeResponse resultado = employeeService.getByDni("70567890");
                // Assert
                assertNotNull(resultado);
                assertEquals("70567890", resultado.getDni());
                assertEquals("Juan", resultado.getName());
                assertEquals("Suarez", resultado.getLastName());
                assertEquals("juan.suarez@gmail.com", resultado.getEmail());
                assertNotNull(resultado.getEmployeeStatus());
                assertEquals(1L, resultado.getEmployeeStatus().getId());
                assertEquals("ACTIVE", resultado.getEmployeeStatus().getCode());
                assertNotNull(resultado.getDepartment());
                assertEquals(1L, resultado.getDepartment().getId());
                assertEquals("Tecnología", resultado.getDepartment().getName());
                assertTrue(resultado.getActive());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        @Test
        void debeLanzarExcepcionCuandoEmpleadoNoExistePorDni() {

                // Arrange
                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.empty());
                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.getByDni("70567890"));
                // Assert
                assertEquals(
                                "EMPLEADO NO ENCONTRADO CON DNI: 70567890",
                                exception.getMessage());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        // PRUEBAS ACTUALIZAR ESTADO DE UN EMPLEADO
        @Test
        void debeActualizarEstadoDelEmpleado() {

                // Arrange
                EmployeeStatus statusActual = new EmployeeStatus();
                statusActual.setId(1L);
                statusActual.setCode("ACTIVE");
                statusActual.setName("ACTIVO");

                EmployeeStatus nuevoStatus = new EmployeeStatus();
                nuevoStatus.setId(2L);
                nuevoStatus.setCode("VACATION");
                nuevoStatus.setName("DE VACACIONES");

                Department department = new Department();
                department.setId(1L);
                department.setName("Tecnología");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setEmail("juan.suarez@gmail.com");
                employee.setEmployeeStatus(statusActual);
                employee.setDepartment(department);
                employee.setActive(true);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                when(employeeStatusRepository.findByCode("VACATION"))
                                .thenReturn(Optional.of(nuevoStatus));
                when(employeeRepository.save(employee))
                                .thenReturn(employee);

                // Act
                EmployeeResponse resultado = employeeService.updateWorkingStatus(
                                "70567890",
                                "VACATION");

                // Assert
                assertNotNull(resultado);
                assertEquals("70567890", resultado.getDni());
                assertNotNull(resultado.getEmployeeStatus());

                assertEquals(2L, resultado.getEmployeeStatus().getId());
                assertEquals("VACATION", resultado.getEmployeeStatus().getCode());
                assertEquals("DE VACACIONES", resultado.getEmployeeStatus().getName());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
                verify(employeeStatusRepository)
                                .findByCode("VACATION");
                verify(employeeRepository)
                                .save(employee);
        }

        @Test
        void debeLanzarExcepcionCuandoEmpleadoNoExisteAlActualizarEstado() {

                // Arrange
                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.empty());
                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.updateWorkingStatus(
                                                "70567890",
                                                "VACATION"));

                // Assert
                assertEquals(
                                "EMPLEADO NO ENCONTRADO CON DNI: 70567890",
                                exception.getMessage());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        @Test
        void debeLanzarExcepcionCuandoEstadoNoExisteAlActualizar() {

                // Arrange
                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setActive(true);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                when(employeeStatusRepository.findByCode("VACATION"))
                                .thenReturn(Optional.empty());

                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.updateWorkingStatus(
                                                "70567890",
                                                "VACATION"));

                // Assert
                assertEquals(
                                "CODIGO NO ENCONTRADO: VACATION",
                                exception.getMessage());
                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
                verify(employeeStatusRepository)
                                .findByCode("VACATION");
        }

        // PRUEBAS PARA MÉTODO DE FINALIZACIÓN DE CONTRATO
        @Test
        void debeFinalizarContratoYActualizarEstadoInactivo() {

                // Arrange
                EmployeeStatus statusActual = new EmployeeStatus();
                statusActual.setId(1L);
                statusActual.setCode("ACTIVE");
                statusActual.setName("ACTIVO");

                EmployeeStatus statusInactivo = new EmployeeStatus();
                statusInactivo.setId(4L);
                statusInactivo.setCode("INACTIVE");
                statusInactivo.setName("INACTIVO");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setLastName("Suarez");
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setEmployeeStatus(statusActual);
                employee.setActive(true);

                LocalDate fechaFin = LocalDate.of(2026, 12, 31);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                when(employeeStatusRepository.findByCode("INACTIVE"))
                                .thenReturn(Optional.of(statusInactivo));
                when(employeeRepository.save(employee))
                                .thenReturn(employee);

                // Act
                EmployeeResponse resultado = employeeService.updateEndContractAndUpdateInactivoAutomatic(
                                "70567890",
                                fechaFin);

                // Assert
                assertNotNull(resultado);
                assertEquals("70567890", resultado.getDni());
                assertEquals(fechaFin, resultado.getEndDate());
                assertNotNull(resultado.getEmployeeStatus());
                assertEquals(4L, resultado.getEmployeeStatus().getId());
                assertEquals("INACTIVE", resultado.getEmployeeStatus().getCode());
                assertEquals("INACTIVO", resultado.getEmployeeStatus().getName());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
                verify(employeeStatusRepository)
                                .findByCode("INACTIVE");
                verify(employeeRepository)
                                .save(employee);
        }

        @Test
        void debeLanzarExcepcionCuandoFechaFinContratoEsNula() {

                // Arrange
                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setActive(true);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                // Act
                BusinessException exception = assertThrows(
                                BusinessException.class,
                                () -> employeeService.updateEndContractAndUpdateInactivoAutomatic(
                                                "70567890",
                                                null));

                // Assert
                assertEquals(
                                "LA FECHA DE FIN DE CONTRATO ES OBLIGATORIA",
                                exception.getMessage());
                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        @Test
        void debeLanzarExcepcionCuandoFechaFinEsAnteriorAFechaContratacion() {

                // Arrange
                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setHireDate(LocalDate.of(2026, 8, 24));
                employee.setActive(true);

                LocalDate fechaFin = LocalDate.of(2026, 8, 20);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));

                // Act
                BusinessException exception = assertThrows(
                                BusinessException.class,
                                () -> employeeService.updateEndContractAndUpdateInactivoAutomatic(
                                                "70567890",
                                                fechaFin));

                // Assert
                assertEquals(
                                "LA FECHA DE FIN DE CONTRATO NO PUEDE SER MENOR QUE LA FECHA DE CONTRATACIÓN",
                                exception.getMessage());
                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        // PRUEBAS PARA MÉTODO DE ELIMINACIÓN LÓGICA
        @Test
        void debeEliminarEmpleadoLogicamente() {

                // Arrange
                EmployeeStatus statusDelete = new EmployeeStatus();
                statusDelete.setId(5L);
                statusDelete.setCode("DELET");
                statusDelete.setName("ELIMINADO");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setActive(true);

                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.of(employee));
                when(employeeStatusRepository.findByCode("DELET"))
                                .thenReturn(Optional.of(statusDelete));

                // Act
                String resultado = employeeService.deleteLogico("70567890");

                // Assert
                assertEquals(
                                "Registro Eliminado Lógicamente, DNI :70567890 NOMBRE: Juan",
                                resultado);
                assertFalse(employee.isActive());
                assertEquals(
                                statusDelete,
                                employee.getEmployeeStatus());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
                verify(employeeStatusRepository)
                                .findByCode("DELET");
                verify(employeeRepository)
                                .save(employee);
        }

        @Test
        void debeLanzarExcepcionCuandoEmpleadoNoExisteAlEliminar() {

                // Arrange
                when(employeeRepository.findByDniAndActive("70567890", true))
                                .thenReturn(Optional.empty());

                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.deleteLogico("70567890"));

                // Assert
                assertEquals(
                                " EMPLEADO NO ENCONTRADO CON DNI: 70567890",
                                exception.getMessage());

                verify(employeeRepository)
                                .findByDniAndActive("70567890", true);
        }

        // PRUEBAS PARA METODO RE-ACTIVACIÓN DE EMPLEADO

        @Test
        void debeReactivarEmpleadoEliminadoLogicamente() {

                // Arrange
                EmployeeStatus statusDelete = new EmployeeStatus();
                statusDelete.setId(5L);
                statusDelete.setCode("DELET");
                statusDelete.setName("ELIMINADO");

                EmployeeStatus statusActive = new EmployeeStatus();
                statusActive.setId(1L);
                statusActive.setCode("ACTIVE");
                statusActive.setName("ACTIVO");

                Employee employee = new Employee();
                employee.setDni("70567890");
                employee.setName("Juan");
                employee.setActive(false);
                employee.setEmployeeStatus(statusDelete);
                employee.setHireDate(LocalDate.of(2025, 1, 10));
                employee.setEndDate(LocalDate.of(2025, 12, 31));

                when(employeeRepository.findByDniAndStatusCodeAndActive(
                                "70567890",
                                "DELET",
                                false))
                                .thenReturn(Optional.of(employee));
                when(employeeStatusRepository.findByCode("ACTIVE"))
                                .thenReturn(Optional.of(statusActive));
                when(employeeRepository.save(employee))
                                .thenReturn(employee);

                // Act
                String resultado = employeeService.activarEmployeeDeleteLogico("70567890");
                // Assert
                assertEquals(
                                "SE HA REACTIVADO EMPLEADO CON DNI: 70567890 NOMBRE: Juan",
                                resultado);
                assertTrue(employee.isActive());
                assertEquals(
                                statusActive,
                                employee.getEmployeeStatus());
                assertEquals(
                                LocalDate.now(),
                                employee.getHireDate());
                assertNull(employee.getEndDate());

                verify(employeeRepository)
                                .findByDniAndStatusCodeAndActive(
                                                "70567890",
                                                "DELET",
                                                false);

                verify(employeeStatusRepository)
                                .findByCode("ACTIVE");

                verify(employeeRepository)
                                .save(employee);
        }

        @Test
        void debeLanzarExcepcionCuandoEmpleadoNoExisteAlReactivar() {

                // Arrange
                when(employeeRepository.findByDniAndStatusCodeAndActive(
                                "70567890",
                                "DELET",
                                false))
                                .thenReturn(Optional.empty());
                // Act
                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> employeeService.activarEmployeeDeleteLogico(
                                                "70567890"));
                // Assert
                assertEquals(
                                " EMPLEADO NO ENCONTRADO CON DNI : 70567890",
                                exception.getMessage());
                verify(employeeRepository)
                                .findByDniAndStatusCodeAndActive(
                                                "70567890",
                                                "DELET",
                                                false);
        }
}
