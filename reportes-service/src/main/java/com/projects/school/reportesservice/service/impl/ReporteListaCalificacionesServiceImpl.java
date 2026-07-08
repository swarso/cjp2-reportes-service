package com.projects.school.reportesservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.AlumnoCalificacionReporteDto;
import com.projects.school.reportesservice.dto.CalificacionDetalleDto;
import com.projects.school.reportesservice.dto.ListaCalificacionesReporteDto;
import com.projects.school.reportesservice.service.ReporteListaCalificacionesService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteListaCalificacionesServiceImpl implements ReporteListaCalificacionesService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    @Value("${colegio.nombre}")
    private String nombreColegio;

    public ReporteListaCalificacionesServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(Integer idGrupo, Integer idMateria, String descripcionGrupo, String periodoEscolar) {
        List<CalificacionDetalleDto> detalle = alumnoServiceClient.getCalificacionesDetalle(idGrupo, idMateria);
        if (detalle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron calificaciones para el grupo y materia indicados.");
        }

        boolean mostrarCalifOrdinaria3 = detalle.stream()
                .anyMatch(d -> d.getIdModulo() != null && (d.getIdModulo() == 2 || d.getIdModulo() == 5));

        List<AlumnoCalificacionReporteDto> alumnos = new ArrayList<>();
        for (CalificacionDetalleDto d : detalle) {
            alumnos.add(new AlumnoCalificacionReporteDto(
                    d.getNocontrol(), d.getNombreAlumno(), d.getCal1(), d.getCal2(), d.getCal3(), d.getCalFinal()));
        }

        ListaCalificacionesReporteDto reporte = new ListaCalificacionesReporteDto(
                nombreColegio,
                imageUtil.loadImageAsBase64(LOGO_RESOURCE),
                detalle.get(0).getMateria(),
                descripcionGrupo,
                periodoEscolar,
                FechaUtil.hoyLarga(),
                mostrarCalifOrdinaria3,
                alumnos);

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", reporte);

        return pdfGeneratorUtil.generatePdf("lista-calificaciones-template", variables);
    }
}
