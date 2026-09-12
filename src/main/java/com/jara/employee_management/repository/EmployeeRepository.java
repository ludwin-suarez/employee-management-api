package com.jara.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jara.employee_management.model.domain.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByDniAndActive(String dni, boolean active);

  // busca por id y estado Para eliminar un resgistro
  List<Employee> findAllByActive(Boolean active);

  /*
   * // Se actualizó la búsqueda de DNI - éste metod. validaba al principio
   * 
   * @Query("""
   * SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
   * FROM Employee e
   * WHERE e.dni = :dni
   * AND e.employeeStatus.id IN :statusIds
   * """)
   * boolean existsByDniAndStatusIdIn(@Param("dni") String
   * dni, @Param("statusIds") List<Long> statusIds);
   */
  @Query("""
      SELECT e FROM Employee e
      WHERE e.dni = :dni
      AND e.active=:active
        AND e.employeeStatus.code =:code
      """)
  Optional<Employee> findByDniAndStatusCodeAndActive(@Param("dni") String dni, @Param("code") String code,
      @Param("active") Boolean active);

  boolean existsByDni(String dni);
}
