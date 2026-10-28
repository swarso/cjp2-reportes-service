package com.projects.school.reportesservice.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.AlumnoDatosDto;
import com.projects.school.reportesservice.dto.CuatrimestreKardexDto;
import com.projects.school.reportesservice.dto.CuatrimestreReporteDto;
import com.projects.school.reportesservice.dto.MateriaSeguimientoDto;
import com.projects.school.reportesservice.dto.SeguimientoAcademicoReporteDto;
import com.projects.school.reportesservice.service.ReporteSeguimientoAcademicoService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteSeguimientoAcademicoServiceImpl implements ReporteSeguimientoAcademicoService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    @Value("${colegio.nombre}")
    private String nombreColegio;

    public ReporteSeguimientoAcademicoServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(String noControl) {
        AlumnoDatosDto alumno = alumnoServiceClient.getAlumnoByNumControl(noControl);
        if (alumno == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró el alumno con no. control: " + noControl);
        }

        List<CuatrimestreKardexDto> kardex = alumnoServiceClient.getKardexByNumControl(noControl);
        List<CuatrimestreReporteDto> cuatrimestres = new ArrayList<>();
        List<BigDecimal> promediosCuatrimestre = new ArrayList<>();

        for (CuatrimestreKardexDto cuatrimestre : kardex) {
            List<MateriaSeguimientoDto> materias = cuatrimestre.getCurso() != null
                    ? cuatrimestre.getCurso() : List.of();
            BigDecimal promedioCurso = promedioMaterias(materias);
            if (promedioCurso != null) {
                promediosCuatrimestre.add(promedioCurso);
            }
            cuatrimestres.add(new CuatrimestreReporteDto(
                    cuatrimestre.getDescripcioncurso(), materias, promedioCurso,
                    mostrarCalifOrdinaria3(materias)));
        }

        BigDecimal promedioFinal = promedioValores(promediosCuatrimestre);

        String nombreAlumno = String.join(" ",
                nullToEmpty(alumno.getApaternopersona()),
                nullToEmpty(alumno.getAmaternopersona()),
                nullToEmpty(alumno.getNombrepersona())).trim().replaceAll("\\s+", " ");

        SeguimientoAcademicoReporteDto reporte = new SeguimientoAcademicoReporteDto(
                nombreColegio,
                imageUtil.loadImageAsBase64(LOGO_RESOURCE),
                nombreAlumno,
                alumno.getDescripciongrupo(),
                alumno.getNocontrolalumno(),
                FechaUtil.hoyLarga(),
                cuatrimestres,
                promedioFinal);

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", reporte);

        return pdfGeneratorUtil.generatePdf("seguimiento-academico-template", variables);
    }

    private boolean mostrarCalifOrdinaria3(List<MateriaSeguimientoDto> materias) {
        for (MateriaSeguimientoDto materia : materias) {
            Integer modulo = materia.getModulo();
            if (modulo != null && (modulo == 2 || modulo == 5)) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal promedioMaterias(List<MateriaSeguimientoDto> materias) {
        List<BigDecimal> calificaciones = new ArrayList<>();
        for (MateriaSeguimientoDto materia : materias) {
            if (materia.getCalfinal() != null) {
                calificaciones.add(materia.getCalfinal());
            }
        }
        return promedioValores(calificaciones);
    }

    private BigDecimal promedioValores(List<BigDecimal> valores) {
        if (valores.isEmpty()) {
            return null;
        }
        BigDecimal suma = BigDecimal.ZERO;
        for (BigDecimal valor : valores) {
            suma = suma.add(valor);
        }
        return suma.divide(BigDecimal.valueOf(valores.size()), 2, RoundingMode.HALF_UP);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
