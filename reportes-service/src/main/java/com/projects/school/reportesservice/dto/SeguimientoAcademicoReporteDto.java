package com.projects.school.reportesservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoAcademicoReporteDto {

    private String nombreColegio;
    private String logoBase64;
    private String nombreAlumno;
    private String grupo;
    private String noControl;
    private String fechaReporte;
    private List<CuatrimestreReporteDto> cuatrimestres;
    private BigDecimal promedioFinal;

}
