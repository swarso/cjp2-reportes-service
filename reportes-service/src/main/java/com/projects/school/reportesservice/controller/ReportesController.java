package com.projects.school.reportesservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projects.school.reportesservice.service.ReporteListaAsistenciaService;
import com.projects.school.reportesservice.service.ReporteListaCalificacionesService;
import com.projects.school.reportesservice.service.ReporteSeguimientoAcademicoService;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    private final ReporteSeguimientoAcademicoService reporteSeguimientoAcademicoService;
    private final ReporteListaCalificacionesService reporteListaCalificacionesService;
    private final ReporteListaAsistenciaService reporteListaAsistenciaService;

    public ReportesController(ReporteSeguimientoAcademicoService reporteSeguimientoAcademicoService,
            ReporteListaCalificacionesService reporteListaCalificacionesService,
            ReporteListaAsistenciaService reporteListaAsistenciaService) {
        this.reporteSeguimientoAcademicoService = reporteSeguimientoAcademicoService;
        this.reporteListaCalificacionesService = reporteListaCalificacionesService;
        this.reporteListaAsistenciaService = reporteListaAsistenciaService;
    }

    @GetMapping(value = "/seguimiento-academico/{noControl}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteSeguimientoAcademico(@PathVariable String noControl) {
        byte[] pdf = reporteSeguimientoAcademicoService.generarReporte(noControl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=seguimiento-academico-" + noControl + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/lista-calificaciones/{idGrupo}/{idMateria}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteListaCalificaciones(
            @PathVariable Integer idGrupo,
            @PathVariable Integer idMateria,
            @RequestParam(required = false) String grupo,
            @RequestParam(required = false) String periodo) {
        byte[] pdf = reporteListaCalificacionesService.generarReporte(idGrupo, idMateria, grupo, periodo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=lista-calificaciones-" + idGrupo + "-" + idMateria + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/lista-asistencia/{idGrupo}/{idMateria}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteListaAsistencia(
            @PathVariable Integer idGrupo,
            @PathVariable Integer idMateria,
            @RequestParam(required = false) String materia,
            @RequestParam(required = false) String grupo,
            @RequestParam(required = false) String periodo) {
        byte[] pdf = reporteListaAsistenciaService.generarReporte(idGrupo, idMateria, materia, grupo, periodo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=lista-asistencia-" + idGrupo + "-" + idMateria + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
