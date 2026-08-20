package com.jara.employee_management.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeStatusResponse {

    private Long id;
    private String name;

}
