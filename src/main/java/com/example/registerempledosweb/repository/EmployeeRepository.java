package com.example.registerempledosweb.repository;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByIdAndState(Long id, Boolean state);
    //busca por id y estado Para eliminar un resgistro

    List<Employee> findAllByState(Boolean state);
    List<Employee> findAllByIdAndState(Long id,Boolean state);

    //para traer por estado:
    List<Employee> findAllByWorkingstatusAndState(Integer workingstatus,Boolean state);
    //Estos nombres de los parametos tienen que ser igual que las las tablas de BD
    List<Employee> findAllByDniAndState(String dni,Boolean state);
}
