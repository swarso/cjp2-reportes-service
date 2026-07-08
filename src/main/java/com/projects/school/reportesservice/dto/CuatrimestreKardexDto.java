package com.projects.school.reportesservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class CuatrimestreKardexDto {

    private String descripcioncurso;
    private List<MateriaSeguimientoDto> curso;

}
