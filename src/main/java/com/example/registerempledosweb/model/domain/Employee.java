package com.example.registerempledosweb.model.domain;


import lombok.Data;
import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="employee")
@Data
public class Employee implements Serializable {
private  static  final long SerialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="dni",length =8 )
    private String dni;

    @Column(name="name")
    private String name;

    @Column(name="lastname")
    private String lastname;

    @Column(name="age")
    private int age;//edad

    @Column(name="salary")
    private BigDecimal salary;
    @Column(name="contractdate")
    private Date contractdate;
    @Column(name="contractenddate")
    private Date contractenddate;
    @Column(name="description")
    private String description;
    @Column(name="employmentstatus")
    private String employmentstatus;

    @Column(name="state")
    private boolean state;
}
