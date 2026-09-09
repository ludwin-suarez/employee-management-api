package com.jara.employee_management.controller;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;
import com.jara.employee_management.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private EmployeeService employeeService;

        // ============================================================
        // 1. GET /api/employee/list_employees
        // ============================================================
        @Test
        void debeListarEmpleadosYRetornar200() throws Exception {

                // Arrange
                EmployeeResponse employee = EmployeeResponse.builder()
                                .dni("70567890")
                                .name("Juan")
                                .lastName("Suarez Lopez")
                                .email("juan.suarez@gmail.com")
                                .birthDate(LocalDate.of(1996, 8, 1))
                                .salary(new BigDecimal("2500.00"))
                                .hireDate(LocalDate.of(2026, 8, 24))
                                .description("Asignado al área de Recursos Humanos")
                                .active(true)
                                .build();

                when(employeeService.list())
                                .thenReturn(List.of(employee));

                // Act & Assert
                mockMvc.perform(
                                get("/api/employee/list_employees"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].dni").value("70567890"))
                                .andExpect(jsonPath("$[0].name").value("Juan"))
                                .andExpect(jsonPath("$[0].lastName").value("Suarez Lopez"))
                                .andExpect(jsonPath("$[0].email").value("juan.suarez@gmail.com"));

                verify(employeeService).list();
        }

        // ============================================================
        // 2. POST /api/employee/create_employee
        // ============================================================
        @Test
        void debeCrearEmpleadoCuandoLosDatosSonValidos() throws Exception {

                // Arrange
                String json = """
                                {
                                    "dni": "70567890",
                                    "name": "Juan Campos",
                                    "lastName": "Suarez Lopez",
                                    "email": "juan.suarez@gmail.com",
                                    "birthDate": "1996-08-01",
                                    "salary": 2500.00,
                                    "hireDate": "2026-08-24",
                                    "endDate": null,
                                    "description": "Asignado al área de Recursos Humanos",
                                    "code": "ACTIVE",
                                    "departmentId": 1
                                }
                                """;
                EmployeeResponse response = EmployeeResponse.builder()
                                .dni("70567890")
                                .name("Juan Campos")
                                .lastName("Suarez Lopez")
                                .email("juan.suarez@gmail.com")
                                .birthDate(LocalDate.of(1996, 8, 1))
                                .salary(new BigDecimal("2500.00"))
                                .hireDate(LocalDate.of(2026, 8, 24))
                                .description("Asignado al área de Recursos Humanos")
                                .active(true)
                                .build();

                when(employeeService.create(any(EmployeeRequest.class)))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(
                                post("/api/employee/create_employee")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(json))
                                .andExpect(status().isOk())
                                .andExpect(content()
                                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.dni")
                                                .value("70567890"))
                                .andExpect(jsonPath("$.name")
                                                .value("Juan Campos"))
                                .andExpect(jsonPath("$.lastName")
                                                .value("Suarez Lopez"));

                verify(employeeService)
                                .create(any(EmployeeRequest.class));
        }

        // ============================================================
        // 3. POST /api/employee/create_employee
        // VALIDACIÓN DEL DNI
        // ============================================================
        @Test
        void debeRetornar400CuandoDniNoTieneOchoDigitos() throws Exception {

                // Arrange
                String json = """
                                {
                                    "dni": "123",
                                    "name": "Juan Campos",
                                    "lastName": "Suarez Lopez",
                                    "email": "juan.suarez@gmail.com",
                                    "birthDate": "1996-08-01",
                                    "salary": 2500.00,
                                    "hireDate": "2026-08-24",
                                    "endDate": null,
                                    "description": "Asignado al área de Recursos Humanos",
                                    "code": "ACTIVE",
                                    "departmentId": 1
                                }
                                """;
                // Act & Assert
                mockMvc.perform(
                                post("/api/employee/create_employee")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(json))
                                .andExpect(status().isBadRequest());

                // El Service no debe ejecutarse
                verifyNoInteractions(employeeService);
        }

        // ============================================================
        // 4. GET /api/employee/search_employee/{dni}
        // ============================================================
        @Test
        void debeBuscarEmpleadoPorDniYRetornar200() throws Exception {

                // Arrange
                String dni = "70567890";
                EmployeeResponse response = EmployeeResponse.builder()
                                .dni(dni)
                                .name("Juan Campos")
                                .lastName("Suarez Lopez")
                                .email("juan.suarez@gmail.com")
                                .salary(new BigDecimal("2500.00"))
                                .active(true)
                                .build();

                when(employeeService.getByDni(dni))
                                .thenReturn(response);
                // Act & Assert
                mockMvc.perform(get("/api/employee/search_employee/{dni}", dni))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.dni").value(dni))
                                .andExpect(jsonPath("$.name").value("Juan Campos"));

                verify(employeeService)
                                .getByDni(dni);
        }

        // ============================================================
        // 5. GET /api/employee/search_employee/{dni}
        // DNI INVÁLIDO
        // ============================================================
        @Test
        void debeRetornar400CuandoDniNoTieneOchoCaracteres() throws Exception {

                // Arrange
                String dniInvalido = "123";

                // Act & Assert
                mockMvc.perform(
                                get("/api/employee/search_employee/{dni}",
                                                dniInvalido))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(employeeService);
        }

        // ============================================================
        // 6. PATCH /api/employee/update_status_employee/{dni}/{code}
        // ============================================================
        @Test
        void debeActualizarEstadoDelEmpleado() throws Exception {

                // Arrange
                String dni = "70567890";
                String code = "VACATION";

                EmployeeResponse response = EmployeeResponse.builder()
                                .dni(dni)
                                .name("Juan Campos")
                                .lastName("Suarez Lopez")
                                .active(true)
                                .build();

                when(employeeService.updateWorkingStatus(dni, code))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(
                                patch(
                                                "/api/employee/update_status_employee/{dni}/{code}",
                                                dni,
                                                code))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.dni")
                                                .value(dni));

                verify(employeeService)
                                .updateWorkingStatus(dni, code);
        }

        // ============================================================
        // 7. PUT /api/employee/end_contract_employee/{dni}/{endDate}
        // ============================================================
        @Test
        void debeFinalizarContratoDelEmpleado() throws Exception {

                // Arrange
                String dni = "70567890";
                LocalDate endDate = LocalDate.of(2026, 12, 31);

                EmployeeResponse response = EmployeeResponse.builder()
                                .dni(dni)
                                .name("Juan Campos")
                                .lastName("Suarez Lopez")
                                .endDate(endDate)
                                .active(false)
                                .build();

                when(employeeService
                                .updateEndContractAndUpdateInactivoAutomatic(
                                                dni,
                                                endDate))
                                .thenReturn(response);

                // Act & Assert
                mockMvc.perform(
                                put(
                                                "/api/employee/end_contract_employee/{dni}/{endDate}",
                                                dni,
                                                endDate))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.dni")
                                                .value(dni))
                                .andExpect(jsonPath("$.endDate")
                                                .value("2026-12-31"));

                verify(employeeService)
                                .updateEndContractAndUpdateInactivoAutomatic(
                                                dni,
                                                endDate);
        }

        // ============================================================
        // 8. DELETE /api/employee/delete_employee/{dni}
        // ============================================================
        @Test
        void debeEliminarLogicamenteEmpleado() throws Exception {

                // Arrange
                String dni = "70567890";
                when(employeeService.deleteLogico(dni))
                                .thenReturn("Empleado eliminado correctamente");

                // Act & Assert
                mockMvc.perform(
                                delete(
                                                "/api/employee/delete_employee/{dni}",
                                                dni))
                                .andExpect(status().isOk())
                                .andExpect(content()
                                                .string("Empleado eliminado correctamente"));

                verify(employeeService)
                                .deleteLogico(dni);
        }

        // ============================================================
        // 9. PATCH /api/employee/reactive_employee/{dni}
        // ============================================================
        @Test
        void debeReactivarEmpleado() throws Exception {

                // Arrange
                String dni = "70567890";
                when(employeeService.activarEmployeeDeleteLogico(dni))
                                .thenReturn("Empleado reactivado correctamente");

                // Act & Assert
                mockMvc.perform(
                                patch(
                                                "/api/employee/reactive_employee/{dni}",
                                                dni))
                                .andExpect(status().isOk())
                                .andExpect(content()
                                                .string("Empleado reactivado correctamente"));

                verify(employeeService)
                                .activarEmployeeDeleteLogico(dni);
        }

        // ============================================================
        // 10. VALIDACIÓN DEL DNI EN DELETE
        // ============================================================

        @Test
        void debeRetornar400AlEliminarCuandoDniEsInvalido() throws Exception {

                // Arrange
                String dniInvalido = "123";

                // Act & Assert
                mockMvc.perform(
                                delete(
                                                "/api/employee/delete_employee/{dni}",
                                                dniInvalido))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(employeeService);
        }

}