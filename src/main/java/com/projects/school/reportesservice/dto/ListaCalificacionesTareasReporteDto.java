package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListaCalificacionesTareasReporteDto {

    private String nombreColegio;
    private String logoBase64;
    private String descripcionMateria;
    private String descripcionGrupo;
    private String periodoEscolar;
    private String fechaReporte;
    private boolean mostrarCalifOrdinaria3;
    private String nombreDocente;
    private List<AlumnoCalificacionTareasReporteDto> alumnos;

}
