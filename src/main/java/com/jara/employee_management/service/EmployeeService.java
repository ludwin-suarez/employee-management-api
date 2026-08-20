package com.jara.employee_management.service;

import java.util.List;

import com.jara.employee_management.model.domain.Employee;
import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;

public interface EmployeeService {
    public List<EmployeeResponse> list();

    public List<EmployeeResponse> create(EmployeeRequest request);

    public List<EmployeeResponse> getById(Long id);

    public Employee updateWorkingStatus(Long id, int state);

    public Employee updateEndContractAndUpdateInactivoAutomatic(Long id, EmployeeRequest request);

    public String deleteLogico(Long id);

    public List<EmployeeResponse> getReportWorkingStatus(Integer workingstatus);
}
