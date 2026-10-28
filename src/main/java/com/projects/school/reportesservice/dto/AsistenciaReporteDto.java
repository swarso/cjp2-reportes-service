package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AsistenciaReporteDto {

    //Prueba de comentario
    private String nombreColegio;
    private String logoBase64;
    private String descripcionMateria;
    private String descripcionGrupo;
    private String periodoEscolar;
    private String fechaReporte;
    private List<String> fechasSesiones;
    private List<AlumnoAsistenciaReporteDto> alumnos;

}
