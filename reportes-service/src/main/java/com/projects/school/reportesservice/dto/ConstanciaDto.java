package com.projects.school.reportesservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Espejo plano de la respuesta de alumno-service
 * (GET /academico/constancia/{noControl}), mas logoBase64 que se completa
 * aqui mismo antes de pasarse al template -- mismo patron que
 * BoletaEvaluacionDto.
 */
@Data
@NoArgsConstructor
public class ConstanciaDto {

    private String noControlAlumno;
    private String nombreCompleto;
    private Integer genero;
    private String descripcionCurso;
    private String descripcionEspecialidad;
    private Integer modulo;
    private String fechaInicioPeriodo;
    private String fechaFinPeriodo;

    private String nombreInstitucion;
    private String cct;
    private String numAcuerdoIncorporacion;
    private String fechaAcuerdoIncorporacion;
    private String direccionInstitucion;
    private String municipioInstitucion;
    private String telefonoInstitucion;
    private String director;

    private String logoBase64;
}
