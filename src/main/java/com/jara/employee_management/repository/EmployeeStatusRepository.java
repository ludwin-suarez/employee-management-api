package com.jara.employee_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jara.employee_management.model.domain.EmployeeStatus;

public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Long> {

    Optional<EmployeeStatus> findByCode(String code);

    // EmployeeStatus findByCode

    boolean existsByCode(String code);

    // Integer employeeStatusByCode(String code);

}
