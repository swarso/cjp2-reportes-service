package com.projects.school.reportesservice.service;

public interface ReporteListaCalificacionesTareasService {

    byte[] generarReporte(Integer idGrupo, Integer idMateria, String descripcionGrupo, String periodoEscolar);

}
