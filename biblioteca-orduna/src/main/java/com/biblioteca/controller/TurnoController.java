package com.biblioteca.controller;

import com.biblioteca.dto.CierreTurnoDTO;
import com.biblioteca.dto.TurnoResponseDTO;
import com.biblioteca.entity.Rol;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/** Bitácora de turnos (RF16). Bibliotecario/gerente; cada uno abre y cierra SU turno. */
@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    /** RF16 - POST /api/turnos/abrir (sin body; el bibliotecario sale del JWT) */
    @PostMapping("/abrir")
    @ResponseStatus(HttpStatus.CREATED)
    public TurnoResponseDTO abrir(@AuthenticationPrincipal UsuarioAutenticado bibliotecario) {
        return turnoService.abrirTurno(bibliotecario.id());
    }

    /** RF16 - POST /api/turnos/cerrar  Body opcional: {"observaciones":"..."} */
    @PostMapping("/cerrar")
    public TurnoResponseDTO cerrar(@Valid @RequestBody(required = false) CierreTurnoDTO datos,
                                   @AuthenticationPrincipal UsuarioAutenticado bibliotecario) {
        return turnoService.cerrarTurno(bibliotecario.id(), datos);
    }

    /** GET /api/turnos/actual - mi turno abierto (204 si no tengo) */
    @GetMapping("/actual")
    public ResponseEntity<TurnoResponseDTO> actual(@AuthenticationPrincipal UsuarioAutenticado bibliotecario) {
        TurnoResponseDTO t = turnoService.turnoActual(bibliotecario.id());
        return t == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(t);
    }

    /**
     * GET /api/turnos?desde=2026-10-01&hasta=2026-10-31&id_bibliotecario=2&tipo=BIBLIOTECARIO
     * El gerente ve a todos; el bibliotecario solo sus turnos (los filtros de persona se ignoran).
     */
    @GetMapping
    public List<TurnoResponseDTO> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(name = "id_bibliotecario", required = false) Long idBibliotecario,
            @RequestParam(required = false) Rol tipo,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return turnoService.listarTurnos(desde, hasta, idBibliotecario, tipo, usuario.id());
    }
}
