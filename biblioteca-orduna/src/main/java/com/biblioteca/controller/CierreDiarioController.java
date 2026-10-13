package com.biblioteca.controller;

import com.biblioteca.dto.CierreDiarioDTO;
import com.biblioteca.dto.CierreDiarioResponseDTO;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.CierreDiarioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/** RF12 - Cierre diario de la biblioteca (bibliotecario/admin). */
@RestController
@RequestMapping("/api/cierre-diario")
public class CierreDiarioController {

    private final CierreDiarioService cierreDiarioService;

    public CierreDiarioController(CierreDiarioService cierreDiarioService) {
        this.cierreDiarioService = cierreDiarioService;
    }

    /**
     * RF12 - POST /api/cierre-diario  Body opcional: {"observaciones":"Sin novedad"}
     * Quien cierra es el usuario logueado (JWT). Exige que no haya turnos abiertos.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CierreDiarioResponseDTO generar(@Valid @RequestBody(required = false) CierreDiarioDTO datos,
                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return cierreDiarioService.generarCierreDiario(usuario.id(), datos);
    }

    /** GET /api/cierre-diario?desde=2026-10-01&hasta=2026-10-31 (por defecto: mes actual) */
    @GetMapping
    public List<CierreDiarioResponseDTO> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return cierreDiarioService.listarCierres(desde, hasta);
    }

    /** GET /api/cierre-diario/2026-10-01 */
    @GetMapping("/{fecha}")
    public CierreDiarioResponseDTO obtener(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return cierreDiarioService.obtenerCierre(fecha);
    }
}
