package com.biblioteca.controller;

import com.biblioteca.dto.SancionResponseDTO;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.SancionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Pantalla "Sanciones y bloqueos" (RF17). Bibliotecario/gerente. Para aplicar: POST /api/usuarios/{id}/suspender */
@RestController
@RequestMapping("/api/sanciones")
public class SancionController {

    private final SancionService sancionService;

    public SancionController(SancionService sancionService) {
        this.sancionService = sancionService;
    }

    /** GET /api/sanciones?activas=true */
    @GetMapping
    public List<SancionResponseDTO> listar(@RequestParam(defaultValue = "false") boolean activas) {
        return sancionService.listarSanciones(activas);
    }

    /** PATCH /api/sanciones/{idSancion}/levantar - finaliza la sanción antes de tiempo */
    @PatchMapping("/{idSancion}/levantar")
    public SancionResponseDTO levantar(@PathVariable Long idSancion,
                                       @AuthenticationPrincipal UsuarioAutenticado bibliotecario) {
        return sancionService.levantarSancion(idSancion, bibliotecario.id());
    }
}
