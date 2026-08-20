package com.jara.employee_management.model.statusenum;

import lombok.Getter;

//NO SE VA A USAR ÉSTA CLASE, SE TIENE QUE ELIMINAR PORQUE YA ESTÁ EN LA BASE DE DATOS
@Getter
public enum EmployeeWorkingStatus {
    ACTIVE(1, "VIGENTE"),
    ON_VACATION(2, "DE VACACIONES"),
    AT_REST(3, "EN DESACANSO"),
    INACTIVE(4, "INACTIVO");

    private Integer id; // Poner TYPE Dato Integer, caso contrario el equal no va a funcionar
    private String workingStatus;

    EmployeeWorkingStatus(Integer id, String workingStatus) {
        this.id = id;
        this.workingStatus = workingStatus;
    }
}
