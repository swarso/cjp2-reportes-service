package com.projects.school.reportesservice.service;

public interface ReporteListaAsistenciaService {

    byte[] generarReporte(Integer idGrupo, Integer idMateria, String descripcionMateria,
            String descripcionGrupo, String periodoEscolar);

}
