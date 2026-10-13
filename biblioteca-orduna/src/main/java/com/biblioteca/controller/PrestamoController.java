package com.biblioteca.controller;

import com.biblioteca.dto.NuevoPrestamoDTO;
import com.biblioteca.dto.PrestamoResponseDTO;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Módulo Préstamos (RF06). Bibliotecario/gerente. */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    /**
     * RF06 - POST /api/prestamos (paso "Confirmar" del asistente)
     * Body: {"id_libro":1,"id_usuario_lector":3,"tipo_prestamo":"FECHA_FIJA"}  (o "id_ejemplar" en vez de "id_libro")
     * El bibliotecario que registra es el usuario logueado (JWT).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponseDTO registrar(@Valid @RequestBody NuevoPrestamoDTO datos,
                                         @AuthenticationPrincipal UsuarioAutenticado bibliotecario) {
        return prestamoService.registrarPrestamo(datos, bibliotecario.id());
    }

    /**
     * GET /api/prestamos?estado=ACTIVOS&q=ana
     * estado: ACTIVOS (por defecto), VENCIDOS, DEVUELTOS o TODOS. Lo usa la pantalla Devoluciones.
     */
    @GetMapping
    public List<PrestamoResponseDTO> listar(@RequestParam(required = false, defaultValue = "ACTIVOS") String estado,
                                            @RequestParam(required = false, defaultValue = "") String q) {
        return prestamoService.listarPrestamos(estado, q);
    }
}
