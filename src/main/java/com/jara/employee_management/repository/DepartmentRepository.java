package com.jara.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jara.employee_management.model.domain.Department;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByIdAndActive(Long id, Boolean active);

}
