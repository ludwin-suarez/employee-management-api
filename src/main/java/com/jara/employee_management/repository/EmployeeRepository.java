package com.jara.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jara.employee_management.model.domain.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Optional<Employee> findById(Long id);

    Optional<Employee> findByIdAndActive(Long id, boolean active);

    // busca por id y estado Para eliminar un resgistro
    List<Employee> findAllByActive(Boolean state);

    List<Employee> findAllByIdAndActive(Long id, Boolean active);

    // Estos nombres de los parametos tienen que ser igual que las las tablas de BD
    List<Employee> findAllByDniAndActive(String dni, Boolean active);

    @Query("""
            SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
            FROM Employee e
            WHERE e.dni = :dni
              AND e.employeeStatus.id IN :statusIds
            """)
    boolean existsByDniAndStatusIdIn(
            @Param("dni") String dni,
            @Param("statusIds") List<Long> statusIds);
}
