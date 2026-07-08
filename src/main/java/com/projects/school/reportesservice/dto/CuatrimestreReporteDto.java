package com.projects.school.reportesservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuatrimestreReporteDto {

    private String descripcioncurso;
    private List<MateriaSeguimientoDto> materias;
    private BigDecimal promedioCurso;
    private boolean mostrarCalifOrdinaria3;

}
