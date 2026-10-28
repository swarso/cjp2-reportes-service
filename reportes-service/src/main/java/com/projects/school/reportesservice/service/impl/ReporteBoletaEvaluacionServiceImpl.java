package com.projects.school.reportesservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.BoletaEvaluacionDto;
import com.projects.school.reportesservice.service.ReporteBoletaEvaluacionService;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

@Service
public class ReporteBoletaEvaluacionServiceImpl implements ReporteBoletaEvaluacionService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    public ReporteBoletaEvaluacionServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(String noControl) {
        BoletaEvaluacionDto boleta = alumnoServiceClient.getBoletaEvaluacion(noControl);
        if (boleta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró información escolar para el alumno: " + noControl);
        }
        boleta.setLogoBase64(imageUtil.loadImageAsBase64(LOGO_RESOURCE));

        Map<String, Object> variables = new HashMap<>();
        variables.put("reporte", boleta);

        return pdfGeneratorUtil.generatePdf("boleta-evaluacion-template", variables);
    }
}
