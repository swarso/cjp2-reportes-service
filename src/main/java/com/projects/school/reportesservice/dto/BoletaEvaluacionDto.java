package com.projects.school.reportesservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Espejo plano de la respuesta de alumno-service
 * (GET /academico/boleta-evaluacion/{noControl}), mas logoBase64 que se
 * completa aqui mismo (no viene de alumno-service) antes de pasarse al
 * template. Ver BoletaEvaluacionServiceImpl / BoletaEvaluacionDto en
 * alumno-service para el detalle de cada campo.
 */
@Data
@NoArgsConstructor
public class BoletaEvaluacionDto {

    private String noControlAlumno;
    private String nombreCompleto;
    private String descripcionGrupo;
    private String descripcionTurno;
    private String descripcionPeriodoEscolar;
    private String descripcionCurso;
    private String claveEspecialidad;
    private String descripcionEspecialidad;
    private String descripcionGeneracion;
    private Integer modulo;

    private String nombreInstitucion;
    private String cct;
    private String modalidad;
    private String numAcuerdoIncorporacion;
    private String fechaAcuerdoIncorporacion;
    private String director;

    private List<BoletaMateriaDto> materias;

    private String logoBase64;
}
