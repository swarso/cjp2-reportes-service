package com.projects.school.reportesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuscarDetalleRequestDto {

    private Integer idGrupo;
    private Integer idMateria;
    private int page;
    private int size;

}
