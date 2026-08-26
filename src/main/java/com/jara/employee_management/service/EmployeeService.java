package com.jara.employee_management.service;

import java.time.LocalDate;
import java.util.List;

import com.jara.employee_management.model.request.EmployeeRequest;
import com.jara.employee_management.model.response.EmployeeResponse;

public interface EmployeeService {

    public List<EmployeeResponse> list();

    public EmployeeResponse create(EmployeeRequest request);

    public EmployeeResponse getByDni(String dni);

    public EmployeeResponse updateWorkingStatus(String dni, String code);

    public EmployeeResponse updateEndContractAndUpdateInactivoAutomatic(String dni, LocalDate dateEndContract);

    public String deleteLogico(String dni);

    public String activarEmployeeDeleteLogico(String dni);

}
