package com.projects.school.reportesservice.dto;

import lombok.Data;

@Data
public class GrupoWebDto {

    private Integer idGrupo;
    private Integer idPeriodoEscolar;
    private Short idModulo;
    private Integer idCurso;
    private String grupoDescr;
    private String periodoEscolarDescr;
    private String moduloDescr;
    private String cursoDescr;
}
