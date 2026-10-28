package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConcentradoCalificacionesReporteDto {

    private String nombreColegio;
    private String logoBase64;
    private String descripcionGrupo;
    private String descripcionCurso;
    private String periodoEscolar;
    private String parcialLabel;
    private String fechaReporte;
    private List<String> materias;
    private List<AlumnoConcentradoReporteDto> alumnos;

}
