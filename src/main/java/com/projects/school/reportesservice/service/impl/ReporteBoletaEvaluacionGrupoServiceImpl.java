package com.projects.school.reportesservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projects.school.reportesservice.client.AlumnoServiceClient;
import com.projects.school.reportesservice.dto.BoletaEvaluacionDto;
import com.projects.school.reportesservice.service.ReporteBoletaEvaluacionGrupoService;
import com.projects.school.reportesservice.util.ImageUtil;
import com.projects.school.reportesservice.util.PdfGeneratorUtil;

/**
 * Boletas de Evaluacion de TODOS los alumnos inscritos de un grupo en un
 * solo PDF, 2 boletas por hoja carta (pedido desde el hub de Reportes,
 * 2026-10-25) -- ver boleta-evaluacion-grupo-template.html.
 */
@Service
public class ReporteBoletaEvaluacionGrupoServiceImpl implements ReporteBoletaEvaluacionGrupoService {

    private static final String LOGO_RESOURCE = "static/images/logo.png";

    private final AlumnoServiceClient alumnoServiceClient;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final ImageUtil imageUtil;

    public ReporteBoletaEvaluacionGrupoServiceImpl(AlumnoServiceClient alumnoServiceClient,
            PdfGeneratorUtil pdfGeneratorUtil, ImageUtil imageUtil) {
        this.alumnoServiceClient = alumnoServiceClient;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    public byte[] generarReporte(Integer idGrupo) {
        List<BoletaEvaluacionDto> boletas = alumnoServiceClient.getBoletasEvaluacionGrupo(idGrupo);
        if (boletas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El grupo no existe o no tiene alumnos inscritos: " + idGrupo);
        }

        String logoBase64 = imageUtil.loadImageAsBase64(LOGO_RESOURCE);
        boletas.forEach(b -> b.setLogoBase64(logoBase64));

        Map<String, Object> variables = new HashMap<>();
        variables.put("boletas", boletas);

        return pdfGeneratorUtil.generatePdf("boleta-evaluacion-grupo-template", variables);
    }
}
