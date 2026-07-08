package com.projects.school.reportesservice.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.projects.school.reportesservice.dto.AlumnoAsistenciaReporteDto;
import com.projects.school.reportesservice.dto.AsistenciaDetalleDto;
import com.projects.school.reportesservice.dto.AsistenciaReporteDto;
import com.projects.school.reportesservice.service.ReporteListaAsistenciaService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteListaAsistenciaServiceImpl implements ReporteListaAsistenciaService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";
    private static final DateTimeFormatter FECHA_COLUMNA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    @Value("${colegio.nombre}")
    private String nombreColegio;

    public ReporteListaAsistenciaServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(Integer idGrupo, Integer idMateria, String descripcionMateria,
            String descripcionGrupo, String periodoEscolar) {
        List<AsistenciaDetalleDto> detalle = alumnoServiceClient.getAsistenciaCompleta(idGrupo, idMateria);
        if (detalle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron registros de asistencia para el grupo y materia indicados.");
        }

        List<LocalDate> fechas = detalle.stream()
                .map(d -> LocalDate.parse(d.getFecha()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        Map<String, String> nombrePorAlumno = new LinkedHashMap<>();
        Map<String, Map<LocalDate, Short>> asistenciaPorAlumno = new HashMap<>();
        for (AsistenciaDetalleDto d : detalle) {
            nombrePorAlumno.putIfAbsent(d.getNocontrol(), d.getNombreAlumno());
            asistenciaPorAlumno
                    .computeIfAbsent(d.getNocontrol(), k -> new HashMap<>())
                    .put(LocalDate.parse(d.getFecha()), d.getAsistio());
        }

        List<String> nocontrolesOrdenados = nombrePorAlumno.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> fechasFormateadas = fechas.stream()
                .map(f -> f.format(FECHA_COLUMNA_FORMATTER))
                .collect(Collectors.toList());

        List<AlumnoAsistenciaReporteDto> alumnos = new ArrayList<>();
        for (String nocontrol : nocontrolesOrdenados) {
            Map<LocalDate, Short> porFecha = asistenciaPorAlumno.get(nocontrol);
            List<String> asistencias = new ArrayList<>();
            for (LocalDate fecha : fechas) {
                Short asistio = porFecha.get(fecha);
                asistencias.add(asistio == null ? "—" : (asistio == 1 ? "Sí" : "No"));
            }
            alumnos.add(new AlumnoAsistenciaReporteDto(nocontrol, nombrePorAlumno.get(nocontrol), asistencias));
        }

        AsistenciaReporteDto reporte = new AsistenciaReporteDto(
                nombreColegio,
                imageUtil.loadImageAsBase64(LOGO_RESOURCE),
                descripcionMateria,
                descripcionGrupo,
                periodoEscolar,
                FechaUtil.hoyLarga(),
                fechasFormateadas,
                alumnos);

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", reporte);

        return pdfGeneratorUtil.generatePdf("lista-asistencia-template", variables);
    }
}
