package com.projects.school.reportesservice.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.projects.school.reportesservice.dto.AlumnoDatosDto;
import com.projects.school.reportesservice.dto.AsistenciaDetalleDto;
import com.projects.school.reportesservice.dto.BuscarDetalleRequestDto;
import com.projects.school.reportesservice.dto.CalificacionDetalleDto;
import com.projects.school.reportesservice.dto.CuatrimestreKardexDto;
import com.projects.school.reportesservice.dto.PageWrapper;
import com.projects.school.reportesservice.dto.ResponseDto;

@Component
public class AlumnoServiceClient {

    private static final String BASE_URL = "http://ALUMNO-SERVICE";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AlumnoServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AlumnoDatosDto getAlumnoByNumControl(String numControl) {
        ResponseDto response = restTemplate.getForObject(
                BASE_URL + "/datos/alumno/numcontrol/{numControl}", ResponseDto.class, numControl);

        if (response == null || response.getData() == null) {
            return null;
        }
        return objectMapper.convertValue(response.getData(), AlumnoDatosDto.class);
    }

    public List<CuatrimestreKardexDto> getKardexByNumControl(String numControl) {
        ResponseDto response = restTemplate.getForObject(
                BASE_URL + "/academico/curso/kardex/alumno/{numControl}", ResponseDto.class, numControl);

        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, CuatrimestreKardexDto.class);
        return objectMapper.convertValue(response.getData(), listType);
    }

    public List<CalificacionDetalleDto> getCalificacionesDetalle(Integer idGrupo, Integer idMateria) {
        BuscarDetalleRequestDto body = new BuscarDetalleRequestDto(idGrupo, idMateria, 0, 100000);
        ResponseDto response = restTemplate.postForObject(
                BASE_URL + "/api/calificaciones/buscar-detalle", body, ResponseDto.class);

        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }
        PageWrapper<CalificacionDetalleDto> page = objectMapper.convertValue(response.getData(),
                objectMapper.getTypeFactory().constructParametricType(PageWrapper.class, CalificacionDetalleDto.class));
        return page.getContent() != null ? page.getContent() : Collections.emptyList();
    }

    public List<AsistenciaDetalleDto> getAsistenciaCompleta(Integer idGrupo, Integer idMateria) {
        ResponseDto response = restTemplate.getForObject(
                BASE_URL + "/api/asistencia/grupo/{idGrupo}/materia/{idMateria}/detalle-completo",
                ResponseDto.class, idGrupo, idMateria);

        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, AsistenciaDetalleDto.class);
        return objectMapper.convertValue(response.getData(), listType);
    }
}
