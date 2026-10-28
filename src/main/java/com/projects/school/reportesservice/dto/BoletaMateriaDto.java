package com.projects.school.reportesservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoletaMateriaDto {
    private String claveMateria;
    private String descripcionMateria;
    private Double cal1;
    private Double cal2;
    private Double cal3;
    private Integer faltas;
    private Double calFinal;
}
