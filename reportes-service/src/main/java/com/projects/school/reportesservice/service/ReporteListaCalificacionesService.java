package com.projects.school.reportesservice.service;

public interface ReporteListaCalificacionesService {

    byte[] generarReporte(Integer idGrupo, Integer idMateria, String descripcionGrupo, String periodoEscolar);

}
