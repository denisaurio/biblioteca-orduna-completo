package com.biblioteca.controller;

import com.biblioteca.dto.ReporteInventarioDTO;
import com.biblioteca.dto.ReporteMultasDTO;
import com.biblioteca.dto.ReportePrestamosDTO;
import com.biblioteca.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * RF10 - Reportes estadísticos (bibliotecario/admin).
 * Fechas en formato yyyy-MM-dd. Si no se envían: desde el 1 del mes actual hasta hoy.
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private static final MediaType CSV = new MediaType("text", "csv", StandardCharsets.UTF_8);

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // ---------------- JSON (para pantallas y gráficos) ----------------

    /** GET /api/reportes/inventario */
    @GetMapping("/inventario")
    public ReporteInventarioDTO inventario() {
        return reporteService.reporteInventario();
    }

    /** GET /api/reportes/prestamos?desde=2026-10-01&hasta=2026-10-31 */
    @GetMapping("/prestamos")
    public ReportePrestamosDTO prestamos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reporteService.reportePrestamos(desde, hasta);
    }

    /** GET /api/reportes/multas?desde=2026-10-01&hasta=2026-10-31 */
    @GetMapping("/multas")
    public ReporteMultasDTO multas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reporteService.reporteMultas(desde, hasta);
    }

    // ---------------- Descarga CSV (se abre en Excel) ----------------

    /** GET /api/reportes/inventario/csv -> descarga reporte_inventario_2026-10-01.csv */
    @GetMapping("/inventario/csv")
    public ResponseEntity<byte[]> inventarioCsv() {
        return descargar(reporteService.exportarInventarioCsv(), "reporte_inventario_" + LocalDate.now() + ".csv");
    }

    /** GET /api/reportes/prestamos/csv?desde=&hasta= -> descarga reporte_prestamos_....csv */
    @GetMapping("/prestamos/csv")
    public ResponseEntity<byte[]> prestamosCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return descargar(reporteService.exportarPrestamosCsv(desde, hasta), nombre("prestamos", desde, hasta));
    }

    /** GET /api/reportes/multas/csv?desde=&hasta= -> descarga reporte_multas_....csv */
    @GetMapping("/multas/csv")
    public ResponseEntity<byte[]> multasCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return descargar(reporteService.exportarMultasCsv(desde, hasta), nombre("multas", desde, hasta));
    }

    /** Arma la respuesta con Content-Disposition: attachment; filename="..." */
    private static ResponseEntity<byte[]> descargar(byte[] contenido, String archivo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(archivo).build().toString())
                .contentType(CSV)
                .contentLength(contenido.length)
                .body(contenido);
    }

    private static String nombre(String tipo, LocalDate desde, LocalDate hasta) {
        LocalDate h = hasta != null ? hasta : LocalDate.now();
        LocalDate d = desde != null ? desde : h.withDayOfMonth(1);
        return "reporte_" + tipo + "_" + d + "_a_" + h + ".csv";
    }
}
