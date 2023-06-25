package com.example.registerempledosweb.model.request;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class EmployeeRequest {
    private String dni;
    private String name;
    private String lastname;
    private int age;//edad
    private BigDecimal salary;
    private Date contractdate;
    private Date contractenddate;
    private String description;
    private String employmentstatus;
    private boolean state;
}
