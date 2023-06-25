package com.example.registerempledosweb.service;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;

import java.util.List;

public interface EmployeeServiceInterface {
    public List<Employee> list();
    public Employee create(EmployeeRequest request);
    public Employee getById(Long id);
    public Employee updateEmploymentStatus(Long id,int estate);
    public Employee updateEndContract(Long id,EmployeeRequest request);
    public String deleteLogico(Long id);
}
