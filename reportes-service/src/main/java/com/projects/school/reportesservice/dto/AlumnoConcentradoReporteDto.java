package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlumnoConcentradoReporteDto {

    private String nocontrol;
    private String nombreAlumno;
    // Una calificacion por materia, en el mismo orden que
    // ConcentradoCalificacionesReporteDto.materias
    private List<Double> calificaciones;

}
