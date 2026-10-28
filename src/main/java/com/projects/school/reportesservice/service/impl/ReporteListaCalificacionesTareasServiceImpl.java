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
import com.projects.school.reportesservice.dto.AlumnoCalificacionTareasReporteDto;
import com.projects.school.reportesservice.dto.CalificacionDetalleDto;
import com.projects.school.reportesservice.dto.DocenteDto;
import com.projects.school.reportesservice.dto.ListaCalificacionesTareasReporteDto;
import com.projects.school.reportesservice.service.ReporteListaCalificacionesTareasService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteListaCalificacionesTareasServiceImpl implements ReporteListaCalificacionesTareasService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    @Value("${colegio.nombre}")
    private String nombreColegio;

    public ReporteListaCalificacionesTareasServiceImpl(AlumnoServiceClient alumnoServiceClient,
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

        List<AlumnoCalificacionTareasReporteDto> alumnos = new ArrayList<>();
        for (CalificacionDetalleDto d : detalle) {
            alumnos.add(new AlumnoCalificacionTareasReporteDto(
                    d.getNocontrol(), d.getNombreAlumno(),
                    d.getCal1(), d.getTa1(), d.getTe1(),
                    d.getCal2(), d.getTa2(), d.getTe2(),
                    d.getCal3(), d.getTa3(), d.getTe3(),
                    d.getCalFinal(), d.getFaltas()));
        }

        DocenteDto docente = alumnoServiceClient.getDocenteByGrupoAndMateria(idGrupo, idMateria);
        String nombreDocente = docente == null ? null : String.join(" ",
                nullToEmpty(docente.getApaternoPersona()),
                nullToEmpty(docente.getAmaternoPersona()),
                nullToEmpty(docente.getNombrePersona())).trim().replaceAll("\\s+", " ");

        ListaCalificacionesTareasReporteDto reporte = new ListaCalificacionesTareasReporteDto(
                nombreColegio,
                imageUtil.loadImageAsBase64(LOGO_RESOURCE),
                detalle.get(0).getMateria(),
                descripcionGrupo,
                periodoEscolar,
                FechaUtil.hoyLarga(),
                mostrarCalifOrdinaria3,
                nombreDocente,
                alumnos);

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", reporte);

        return pdfGeneratorUtil.generatePdf("lista-calificaciones-tareas-template", variables);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
