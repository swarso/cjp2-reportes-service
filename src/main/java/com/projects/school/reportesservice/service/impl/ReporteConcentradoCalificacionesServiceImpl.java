package com.projects.school.reportesservice.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.AlumnoConcentradoReporteDto;
import com.projects.school.reportesservice.dto.CalificacionDetalleDto;
import com.projects.school.reportesservice.dto.ConcentradoCalificacionesReporteDto;
import com.projects.school.reportesservice.dto.GrupoWebDto;
import com.projects.school.reportesservice.service.ReporteConcentradoCalificacionesService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteConcentradoCalificacionesServiceImpl implements ReporteConcentradoCalificacionesService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    @Value("${colegio.nombre}")
    private String nombreColegio;

    public ReporteConcentradoCalificacionesServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(Integer idGrupo, Integer parcial) {
        if (parcial == null || parcial < 0 || parcial > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El parcial debe ser 1, 2, 3 (parciales) o 0 (calificación final).");
        }

        List<CalificacionDetalleDto> detalle = alumnoServiceClient.getCalificacionesGrupo(idGrupo);
        if (detalle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron calificaciones para el grupo indicado.");
        }

        // Materias distintas del grupo, ordenadas por tblMateria.FolioMateria ascendente (columnas del concentrado).
        Map<Integer, String> materiaPorId = new HashMap<>();
        Map<Integer, Integer> folioPorMateria = new HashMap<>();
        for (CalificacionDetalleDto d : detalle) {
            materiaPorId.putIfAbsent(d.getIdMateria(), d.getMateria());
            folioPorMateria.putIfAbsent(d.getIdMateria(), d.getFolioMateria());
        }
        List<Integer> idsMateria = materiaPorId.keySet().stream()
                .sorted(Comparator.comparing(id -> {
                    Integer folio = folioPorMateria.get(id);
                    return folio == null ? Integer.MAX_VALUE : folio;
                }))
                .collect(Collectors.toList());
        List<String> materias = idsMateria.stream().map(materiaPorId::get).collect(Collectors.toList());

        // Agrupa por alumno preservando el orden de llegada (la vista de origen ya
        // viene ordenada por apellido/nombre).
        LinkedHashMap<String, String> nombrePorAlumno = new LinkedHashMap<>();
        Map<String, Map<Integer, Double>> calPorAlumnoMateria = new HashMap<>();
        for (CalificacionDetalleDto d : detalle) {
            nombrePorAlumno.putIfAbsent(d.getNocontrol(), d.getNombreAlumno());
            Double cal = switch (parcial) {
                case 0 -> d.getCalFinal();
                case 1 -> d.getCal1();
                case 2 -> d.getCal2();
                case 3 -> d.getCal3();
                default -> null;
            };
            calPorAlumnoMateria
                    .computeIfAbsent(d.getNocontrol(), k -> new HashMap<>())
                    .put(d.getIdMateria(), cal);
        }

        List<AlumnoConcentradoReporteDto> alumnos = new ArrayList<>();
        for (Map.Entry<String, String> entry : nombrePorAlumno.entrySet()) {
            String nocontrol = entry.getKey();
            Map<Integer, Double> calMaterias = calPorAlumnoMateria.get(nocontrol);
            List<Double> calificaciones = idsMateria.stream()
                    .map(calMaterias::get)
                    .collect(Collectors.toList());
            alumnos.add(new AlumnoConcentradoReporteDto(nocontrol, entry.getValue(), calificaciones));
        }

        GrupoWebDto grupo = alumnoServiceClient.getGrupoWeb(idGrupo);
        String parcialLabel = switch (parcial) {
            case 0 -> "CALIFICACIÓN FINAL";
            case 1 -> "PRIMER PARCIAL";
            case 2 -> "SEGUNDO PARCIAL";
            default -> "TERCER PARCIAL";
        };

        ConcentradoCalificacionesReporteDto reporte = new ConcentradoCalificacionesReporteDto(
                nombreColegio,
                imageUtil.loadImageAsBase64(LOGO_RESOURCE),
                grupo != null ? grupo.getGrupoDescr() : null,
                grupo != null ? grupo.getCursoDescr() : null,
                grupo != null ? grupo.getPeriodoEscolarDescr() : null,
                parcialLabel,
                FechaUtil.hoyLarga(),
                materias,
                alumnos);

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", reporte);

        return pdfGeneratorUtil.generatePdf("concentrado-calificaciones-template", variables);
    }
}
