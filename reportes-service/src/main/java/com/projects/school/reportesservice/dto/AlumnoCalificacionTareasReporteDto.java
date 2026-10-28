package com.projects.school.reportesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlumnoCalificacionTareasReporteDto {

    private String nocontrol;
    private String nombreAlumno;
    private Double cal1;
    private Integer ta1;
    private Integer te1;
    private Double cal2;
    private Integer ta2;
    private Integer te2;
    private Double cal3;
    private Integer ta3;
    private Integer te3;
    private Double calFinal;
    private Integer faltas;

}
