package com.projects.school.reportesservice.dto;

import lombok.Data;

@Data
public class CalificacionDetalleDto {

    private String nocontrol;
    private String nombreAlumno;
    private String materia;
    private Integer idModulo;
    private Double cal1;
    private Double cal2;
    private Double cal3;
    private Double calFinal;

}
