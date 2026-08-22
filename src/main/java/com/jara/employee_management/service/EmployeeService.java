package com.jara.employee_management.service;

import java.time.LocalDate;
import java.util.List;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;

public interface EmployeeService {

    public List<EmployeeResponse> list();

    public List<EmployeeResponse> create(EmployeeRequest request);

    public List<EmployeeResponse> getById(Long id);

    public EmployeeResponse updateWorkingStatus(Long id, String code);

    public EmployeeResponse updateEndContractAndUpdateInactivoAutomatic(Long id, LocalDate dateEndContract);

    public String deleteLogico(Long id);

}
