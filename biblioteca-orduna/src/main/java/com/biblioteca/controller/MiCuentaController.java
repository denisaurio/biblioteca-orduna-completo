package com.biblioteca.controller;

import com.biblioteca.dto.*;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Portal del lector: "Mis préstamos y multas", "Mis reservas" e Inicio reducido.
 * Todo sale del usuario logueado (JWT); cualquier rol logueado puede usarlo.
 */
@RestController
@RequestMapping("/api/mi")
public class MiCuentaController {

    private final DashboardService dashboardService;
    private final PrestamoService prestamoService;
    private final DevolucionService devolucionService;
    private final ReservaService reservaService;
    private final SancionService sancionService;

    public MiCuentaController(DashboardService dashboardService, PrestamoService prestamoService,
                              DevolucionService devolucionService, ReservaService reservaService,
                              SancionService sancionService) {
        this.dashboardService = dashboardService;
        this.prestamoService = prestamoService;
        this.devolucionService = devolucionService;
        this.reservaService = reservaService;
        this.sancionService = sancionService;
    }

    /** GET /api/mi/panel - Inicio del lector */
    @GetMapping("/panel")
    public PanelLectorDTO panel(@AuthenticationPrincipal UsuarioAutenticado u) {
        return dashboardService.panelLector(u.id());
    }

    /** GET /api/mi/prestamos */
    @GetMapping("/prestamos")
    public List<PrestamoResponseDTO> prestamos(@AuthenticationPrincipal UsuarioAutenticado u) {
        return prestamoService.misPrestamos(u.id());
    }

    /** GET /api/mi/multas */
    @GetMapping("/multas")
    public List<MultaResponseDTO> multas(@AuthenticationPrincipal UsuarioAutenticado u) {
        return devolucionService.misMultas(u.id());
    }

    /** GET /api/mi/reservas */
    @GetMapping("/reservas")
    public List<ReservaResponseDTO> reservas(@AuthenticationPrincipal UsuarioAutenticado u) {
        return reservaService.misReservas(u.id());
    }

    /** GET /api/mi/sanciones */
    @GetMapping("/sanciones")
    public List<SancionResponseDTO> sanciones(@AuthenticationPrincipal UsuarioAutenticado u) {
        return sancionService.sancionesDeUsuario(u.id());
    }
}
