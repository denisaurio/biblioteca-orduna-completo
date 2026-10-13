package com.biblioteca.controller;

import com.biblioteca.dto.ConfiguracionDTO;
import com.biblioteca.dto.VigenciaMultaDTO;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.ConfiguracionService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Configuración del sistema (RF13). */
@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    /** GET /api/configuracion - público (el login y el menú muestran el nombre y horario de la biblioteca). */
    @GetMapping
    public ConfiguracionDTO obtener() {
        return configuracionService.obtenerConfiguracion();
    }

    /**
     * RF13 - PUT /api/configuracion (solo GERENTE). Solo cambia los campos enviados.
     * Ej: {"tarifa_multa_diaria":0.50,"dias_max":10,"nombre_biblioteca":"Biblioteca Orduña","aviso_nuevo_libro":false}
     */
    @PutMapping
    public ConfiguracionDTO actualizar(@Valid @RequestBody ConfiguracionDTO datos,
                                       @AuthenticationPrincipal UsuarioAutenticado gerente) {
        return configuracionService.actualizarConfiguracion(datos, gerente.id());
    }

    /** GET /api/configuracion/historial - versiones de tarifa y días (solo GERENTE) */
    @GetMapping("/historial")
    public List<VigenciaMultaDTO> historial() {
        return configuracionService.historial();
    }
}
