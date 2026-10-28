package com.projects.school.reportesservice.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.ConstanciaDto;
import com.projects.school.reportesservice.service.ReporteConstanciaService;
import com.projects.school.reportesservice.util.FechaUtil;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteConstanciaServiceImpl implements ReporteConstanciaService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    public ReporteConstanciaServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(String noControl) {
        ConstanciaDto constancia = alumnoServiceClient.getConstancia(noControl);
        if (constancia == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró información escolar para el alumno: " + noControl);
        }
        constancia.setLogoBase64(imageUtil.loadImageAsBase64(LOGO_RESOURCE));

        String fechaInicioPeriodo = constancia.getFechaInicioPeriodo() != null
                ? FechaUtil.formatear(LocalDate.parse(constancia.getFechaInicioPeriodo())) : "";
        String fechaFinPeriodo = constancia.getFechaFinPeriodo() != null
                ? FechaUtil.formatear(LocalDate.parse(constancia.getFechaFinPeriodo())) : "";

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", constancia);
        variables.put("fechaHoy", FechaUtil.hoyLarga());
        variables.put("fechaInicioPeriodo", fechaInicioPeriodo);
        variables.put("fechaFinPeriodo", fechaFinPeriodo);

        return pdfGeneratorUtil.generatePdf("constancia-template", variables);
    }
}
