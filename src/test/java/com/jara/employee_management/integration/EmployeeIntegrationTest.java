package com.jara.employee_management.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.jara.employee_management.model.domain.Department;
import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.domain.EmployeeStatus;
import com.jara.employee_management.repository.DepartmentRepository;
import com.jara.employee_management.repository.EmployeeRepository;
import com.jara.employee_management.repository.EmployeeStatusRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeStatusRepository employeeStatusRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;

    private EmployeeStatus activeStatus;
    private EmployeeStatus vacationStatus;
    private EmployeeStatus restStatus;
    private EmployeeStatus inactiveStatus;
    private EmployeeStatus deleteStatus;

    private final String DNI = "70567890";

    @BeforeEach
    void prepararDatos() {

        /*
         * Primero se elimina Employee porque tiene FK hacia Department y
         * EmployeeStatus.
         */
        employeeRepository.deleteAll();

        departmentRepository.deleteAll();
        employeeStatusRepository.deleteAll();

        // =====================================================
        // DEPARTAMENTO
        // =====================================================

        department = new Department();
        department.setName("Tecnología");
        department.setDescription("Área de Tecnología");
        department.setActive(true);

        department = departmentRepository.save(department);

        // =====================================================
        // ESTADOS
        // =====================================================

        activeStatus = crearStatus(
                "ACTIVE",
                "ACTIVO",
                "Empleado activo");

        vacationStatus = crearStatus(
                "VACATION",
                "DE VACACIONES",
                "Empleado de vacaciones");

        restStatus = crearStatus(
                "REST",
                "EN DESCANSO",
                "Empleado en descanso");

        inactiveStatus = crearStatus(
                "INACTIVE",
                "INACTIVO",
                "Empleado inactivo");

        deleteStatus = crearStatus(
                "DELET",
                "ELIMINADO",
                "Empleado eliminado lógicamente");

        // =====================================================
        // EMPLEADO
        // =====================================================

        Employee employee = new Employee();

        employee.setDni(DNI);
        employee.setName("Juan");
        employee.setLastName("Suarez");
        employee.setEmail("juan.suarez@gmail.com");
        employee.setBirthDate(LocalDate.of(1996, 8, 1));
        employee.setSalary(new BigDecimal("2500.00"));
        employee.setHireDate(LocalDate.of(2026, 8, 24));
        employee.setEndDate(null);
        employee.setDescription("Asignado al área de Tecnología");
        employee.setEmployeeStatus(activeStatus);
        employee.setDepartment(department);
        employee.setActive(true);

        employeeRepository.save(employee);
    }

    private EmployeeStatus crearStatus(
            String code,
            String name,
            String description) {

        EmployeeStatus status = new EmployeeStatus();

        status.setCode(code);
        status.setName(name);
        status.setDescription(description);

        return employeeStatusRepository.save(status);
    }

    // =========================================================
    // 1. GET - BUSCAR EMPLEADO POR DNI
    // =========================================================

    @Test
    void debeBuscarEmpleadoPorDni() throws Exception {

        mockMvc.perform(
                get("/api/employee/search_employee/{dni}", DNI))

                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.dni").value(DNI))
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Suarez"))
                .andExpect(jsonPath("$.email").value("juan.suarez@gmail.com"))
                .andExpect(jsonPath("$.employeeStatus.code").value("ACTIVE"))
                .andExpect(jsonPath("$.department.name").value("Tecnología"))
                .andExpect(jsonPath("$.active")
                        .value(true));
    }

    // =========================================================
    // 2. GET - DNI NO ENCONTRADO
    // =========================================================

    @Test
    void debeRetornar404CuandoEmpleadoNoExiste() throws Exception {

        mockMvc.perform(
                get(
                        "/api/employee/search_employee/{dni}",
                        "99999999"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"));
    }

    // =========================================================
    // 3. POST - CREAR EMPLEADO
    // =========================================================

    @Test
    void debeCrearEmpleadoCorrectamente() throws Exception {

        String nuevoDni = "80012345";

        String json = """
                {
                    "dni": "80012345",
                    "name": "Maria",
                    "lastName": "Lopez Torres",
                    "email": "maria.lopez@gmail.com",
                    "birthDate": "1995-05-15",
                    "salary": 2600.00,
                    "hireDate": "2026-08-24",
                    "endDate": null,
                    "description": "Asignada al área de Tecnología",
                    "code": "ACTIVE",
                    "departmentId": 1
                }
                """;

        mockMvc.perform(
                post("/api/employee/create_employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(nuevoDni))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria.lopez@gmail.com"))
                .andExpect(jsonPath("$.employeeStatus.code").value("ACTIVE"))
                .andExpect(jsonPath("$.department.name").value("Tecnología"))
                .andExpect(jsonPath("$.active").value(true));
    }

    // =========================================================
    // 4. POST - VALIDACIÓN DNI
    // =========================================================

    @Test
    void debeRetornar400CuandoDniEsInvalido() throws Exception {

        String json = """
                {
                    "dni": "123",
                    "name": "Maria",
                    "lastName": "Lopez Torres",
                    "email": "maria.lopez@gmail.com",
                    "birthDate": "1995-05-15",
                    "salary": 2600.00,
                    "hireDate": "2026-08-24",
                    "endDate": null,
                    "description": "Asignada al área de Tecnología",
                    "code": "ACTIVE",
                    "departmentId": 1
                }
                """;

        mockMvc.perform(
                post("/api/employee/create_employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    // =========================================================
    // 5. POST - DNI DUPLICADO
    // =========================================================

    @Test
    void debeRetornar409CuandoDniYaExiste() throws Exception {

        String json = """
                {
                    "dni": "70567890",
                    "name": "Otro",
                    "lastName": "Empleado",
                    "email": "otro@gmail.com",
                    "birthDate": "1990-01-01",
                    "salary": 3000.00,
                    "hireDate": "2026-08-24",
                    "endDate": null,
                    "description": "Intento de duplicado",
                    "code": "ACTIVE",
                    "departmentId": 1
                }
                """;

        mockMvc.perform(
                post("/api/employee/create_employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("DUPLICATE_RESOURCE"));
    }

    // =========================================================
    // 6. POST - DEPARTAMENTO NO EXISTE
    // =========================================================

    @Test
    void debeRetornar404CuandoDepartamentoNoExiste() throws Exception {

        String json = """
                {
                    "dni": "80012345",
                    "name": "Maria",
                    "lastName": "Lopez Torres",
                    "email": "maria.lopez@gmail.com",
                    "birthDate": "1995-05-15",
                    "salary": 2600.00,
                    "hireDate": "2026-08-24",
                    "endDate": null,
                    "description": "Área desconocida",
                    "code": "ACTIVE",
                    "departmentId": 999
                }
                """;

        mockMvc.perform(
                post("/api/employee/create_employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // =========================================================
    // 7. POST - ESTADO NO EXISTE
    // =========================================================

    @Test
    void debeRetornar404CuandoEstadoNoExiste() throws Exception {

        String json = """
                {
                    "dni": "80012345",
                    "name": "Maria",
                    "lastName": "Lopez Torres",
                    "email": "maria.lopez@gmail.com",
                    "birthDate": "1995-05-15",
                    "salary": 2600.00,
                    "hireDate": "2026-08-24",
                    "endDate": null,
                    "description": "Estado desconocido",
                    "code": "UNKNOWN",
                    "departmentId": 1
                }
                """;

        mockMvc.perform(
                post("/api/employee/create_employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // =========================================================
    // 8. PATCH - ACTUALIZAR ESTADO
    // =========================================================

    @Test
    void debeActualizarEstadoDelEmpleado() throws Exception {

        mockMvc.perform(
                patch(
                        "/api/employee/update_status_employee/{dni}/{code}", DNI, "VACATION"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI))
                .andExpect(jsonPath("$.employeeStatus.code").value("VACATION"))
                .andExpect(jsonPath("$.employeeStatus.name").value("DE VACACIONES"));
    }

    // =========================================================
    // 9. PATCH - ESTADO NO EXISTE
    // =========================================================

    @Test
    void debeRetornar404CuandoEstadoNoExisteAlActualizar() throws Exception {

        mockMvc.perform(
                patch(
                        "/api/employee/update_status_employee/{dni}/{code}",
                        DNI,
                        "UNKNOWN"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // =========================================================
    // 10. PUT - FINALIZAR CONTRATO
    // =========================================================

    @Test
    void debeFinalizarContratoCorrectamente() throws Exception {

        mockMvc.perform(
                put(
                        "/api/employee/end_contract_employee/{dni}/{endDate}",
                        DNI,
                        "2026-12-31"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value(DNI))
                .andExpect(jsonPath("$.endDate").value("2026-12-31"))
                .andExpect(jsonPath("$.employeeStatus.code").value("INACTIVE"))
                .andExpect(jsonPath("$.active").value(true));
    }

    // =========================================================
    // 11. PUT - FECHA ANTERIOR A CONTRATACIÓN
    // =========================================================

    @Test
    void debeRetornar400CuandoFechaFinEsAnteriorAContratacion()
            throws Exception {

        mockMvc.perform(
                put(
                        "/api/employee/end_contract_employee/{dni}/{endDate}",
                        DNI,
                        "2026-08-01"))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BUSINESS_ERROR"));
    }

    // =========================================================
    // 12. DELETE - ELIMINACIÓN LÓGICA
    // =========================================================
    @Test
    void debeEliminarEmpleadoLogicamente() throws Exception {

        mockMvc.perform(
                delete(
                        "/api/employee/delete_employee/{dni}",
                        DNI))

                .andExpect(status().isOk())
                .andExpect(content()
                        .string(
                                "Registro Eliminado Lógicamente, DNI :"
                                        + DNI + " NOMBRE: Juan"));

        // Verificación adicional directamente en la BD H2.
        Employee employee = employeeRepository
                .findByDniAndActive(DNI, false)
                .orElseThrow();

        assertFalse(employee.isActive());
        assertEquals("DELET", employee.getEmployeeStatus().getCode());
    }

    // =========================================================
    // 13. PATCH - REACTIVAR EMPLEADO
    // =========================================================

    @Test
    void debeReactivarEmpleado() throws Exception {

        /*
         * Primero dejamos al empleado eliminado lógicamente.
         */
        Employee employee = employeeRepository
                .findByDniAndActive(DNI, true)
                .orElseThrow();

        employee.setActive(false);
        employee.setEmployeeStatus(deleteStatus);
        employee.setEndDate(LocalDate.of(2026, 8, 30));

        employeeRepository.save(employee);

        mockMvc.perform(
                patch(
                        "/api/employee/reactive_employee/{dni}",
                        DNI))

                .andExpect(status().isOk())
                .andExpect(content()
                        .string("SE HA REACTIVADO EMPLEADO CON DNI: "
                                + DNI + " NOMBRE: Juan"));

        Employee employeeReactivated = employeeRepository
                .findByDniAndActive(DNI, true)
                .orElseThrow();

        assertTrue(employeeReactivated.isActive());
        assertEquals("ACTIVE", employeeReactivated.getEmployeeStatus().getCode());
        assertNull(employeeReactivated.getEndDate());
    }

    // =========================================================
    // 14. PATCH - REACTIVAR EMPLEADO NO EXISTENTE
    // =========================================================

    @Test
    void debeRetornar404CuandoEmpleadoNoExisteAlReactivar()
            throws Exception {

        mockMvc.perform(
                patch(
                        "/api/employee/reactive_employee/{dni}",
                        "99999999"))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404));
    }
}