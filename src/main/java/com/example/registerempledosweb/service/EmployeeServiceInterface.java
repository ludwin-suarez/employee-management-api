package com.example.registerempledosweb.service;

import com.example.registerempledosweb.model.domain.Employee;
import com.example.registerempledosweb.model.request.EmployeeRequest;
import com.example.registerempledosweb.model.response.EmployeeResponse;

import java.util.List;

public interface EmployeeServiceInterface {
    public List<EmployeeResponse> list();
    public List<EmployeeResponse> create(EmployeeRequest request);
    public List<EmployeeResponse> getById(Long id);
    public Employee updateWorkingStatus(Long id, int state);
    public Employee updateEndContractAndUpdateInactivoAutomatic(Long id,EmployeeRequest request);
    public String deleteLogico(Long id);
    public List<EmployeeResponse> getReportWorkingStatus(Integer workingstatus);
}
