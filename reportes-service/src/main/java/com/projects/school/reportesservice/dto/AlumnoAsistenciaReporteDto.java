package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlumnoAsistenciaReporteDto {

    private String nocontrol;
    private String nombreAlumno;
    private List<String> asistencias;

}
