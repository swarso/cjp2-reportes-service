package com.projects.school.reportesservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projects.school.reportesservice.service.ReporteBoletaEvaluacionGrupoService;
import com.projects.school.reportesservice.service.ReporteBoletaEvaluacionService;
import com.projects.school.reportesservice.service.ReporteConcentradoCalificacionesService;
import com.projects.school.reportesservice.service.ReporteConstanciaService;
import com.projects.school.reportesservice.service.ReporteListaAsistenciaService;
import com.projects.school.reportesservice.service.ReporteListaCalificacionesService;
import com.projects.school.reportesservice.service.ReporteListaCalificacionesTareasService;
import com.projects.school.reportesservice.service.ReporteSeguimientoAcademicoService;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    private final ReporteSeguimientoAcademicoService reporteSeguimientoAcademicoService;
    private final ReporteListaCalificacionesService reporteListaCalificacionesService;
    private final ReporteListaCalificacionesTareasService reporteListaCalificacionesTareasService;
    private final ReporteListaAsistenciaService reporteListaAsistenciaService;
    private final ReporteConcentradoCalificacionesService reporteConcentradoCalificacionesService;
    private final ReporteBoletaEvaluacionService reporteBoletaEvaluacionService;
    private final ReporteBoletaEvaluacionGrupoService reporteBoletaEvaluacionGrupoService;
    private final ReporteConstanciaService reporteConstanciaService;

    public ReportesController(ReporteSeguimientoAcademicoService reporteSeguimientoAcademicoService,
            ReporteListaCalificacionesService reporteListaCalificacionesService,
            ReporteListaCalificacionesTareasService reporteListaCalificacionesTareasService,
            ReporteListaAsistenciaService reporteListaAsistenciaService,
            ReporteConcentradoCalificacionesService reporteConcentradoCalificacionesService,
            ReporteBoletaEvaluacionService reporteBoletaEvaluacionService,
            ReporteBoletaEvaluacionGrupoService reporteBoletaEvaluacionGrupoService,
            ReporteConstanciaService reporteConstanciaService) {
        this.reporteSeguimientoAcademicoService = reporteSeguimientoAcademicoService;
        this.reporteListaCalificacionesService = reporteListaCalificacionesService;
        this.reporteListaCalificacionesTareasService = reporteListaCalificacionesTareasService;
        this.reporteListaAsistenciaService = reporteListaAsistenciaService;
        this.reporteConcentradoCalificacionesService = reporteConcentradoCalificacionesService;
        this.reporteBoletaEvaluacionService = reporteBoletaEvaluacionService;
        this.reporteBoletaEvaluacionGrupoService = reporteBoletaEvaluacionGrupoService;
        this.reporteConstanciaService = reporteConstanciaService;
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

    @GetMapping(value = "/boleta-evaluacion/{noControl}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteBoletaEvaluacion(@PathVariable String noControl) {
        byte[] pdf = reporteBoletaEvaluacionService.generarReporte(noControl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=boleta-evaluacion-" + noControl + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/constancia/{noControl}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteConstancia(@PathVariable String noControl) {
        byte[] pdf = reporteConstanciaService.generarReporte(noControl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=constancia-" + noControl + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/boleta-evaluacion-grupo/{idGrupo}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteBoletaEvaluacionGrupo(@PathVariable Integer idGrupo) {
        byte[] pdf = reporteBoletaEvaluacionGrupoService.generarReporte(idGrupo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=boleta-evaluacion-grupo-" + idGrupo + ".pdf")
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

    @GetMapping(value = "/lista-calificaciones-tareas/{idGrupo}/{idMateria}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteListaCalificacionesTareas(
            @PathVariable Integer idGrupo,
            @PathVariable Integer idMateria,
            @RequestParam(required = false) String grupo,
            @RequestParam(required = false) String periodo) {
        byte[] pdf = reporteListaCalificacionesTareasService.generarReporte(idGrupo, idMateria, grupo, periodo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=lista-calificaciones-tareas-" + idGrupo + "-" + idMateria + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/concentrado-calificaciones/{idGrupo}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getReporteConcentradoCalificaciones(
            @PathVariable Integer idGrupo,
            @RequestParam Integer parcial) {
        byte[] pdf = reporteConcentradoCalificacionesService.generarReporte(idGrupo, parcial);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=concentrado-calificaciones-" + idGrupo + ".pdf")
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
