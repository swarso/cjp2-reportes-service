package com.projects.school.reportesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlumnoCalificacionReporteDto {

    private String nocontrol;
    private String nombreAlumno;
    private Double cal1;
    private Double cal2;
    private Double cal3;
    private Double calFinal;

}
