package com.jara.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jara.employee_management.model.domain.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByIdAndActive(Long id, Boolean active);

    // busca por id y estado Para eliminar un resgistro
    List<Employee> findAllByActive(Boolean state);

    List<Employee> findAllByIdAndActive(Long id, Boolean active);

    // Estos nombres de los parametos tienen que ser igual que las las tablas de BD
    List<Employee> findAllByDniAndActive(String dni, Boolean active);
}
