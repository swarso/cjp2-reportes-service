package com.projects.school.reportesservice.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MateriaSeguimientoDto {

    private String descripcionmateria;
    private BigDecimal calordinaria1;
    private BigDecimal calordinaria2;
    private BigDecimal calordinaria3;
    private BigDecimal calextra1;
    private BigDecimal calextra2;
    private BigDecimal calfinal;
    private Long totalfaltas;
    private Integer modulo;

}
