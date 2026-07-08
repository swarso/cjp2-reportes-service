package com.projects.school.reportesservice.dto;

import lombok.Data;

@Data
public class ResponseDto {

    Boolean success;
    String mensaje;
    String error;
    Object data;

}
