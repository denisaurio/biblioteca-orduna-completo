package com.biblioteca.controller;

import com.biblioteca.dto.*;
import com.biblioteca.entity.TipoNotificacion;
import com.biblioteca.security.UsuarioAutenticado;
import com.biblioteca.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** RF11 - Avisos y notificaciones. */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /** GET /api/notificaciones?no_leidas=true - mis avisos, más recientes primero (cualquier rol). */
    @GetMapping
    public List<NotificacionResponseDTO> listar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                @RequestParam(name = "no_leidas", defaultValue = "false") boolean soloNoLeidas) {
        return notificacionService.listarNotificaciones(usuario.id(), soloNoLeidas);
    }

    /** GET /api/notificaciones/no-leidas/total -> {"total": 3}  (campanita) */
    @GetMapping("/no-leidas/total")
    public Map<String, Long> contarNoLeidas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return Map.of("total", notificacionService.contarNoLeidas(usuario.id()));
    }

    /** PATCH /api/notificaciones/{idNotificacion}/leida */
    @PatchMapping("/{idNotificacion}/leida")
    public NotificacionResponseDTO marcarLeida(@PathVariable Long idNotificacion,
                                               @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return notificacionService.marcarComoLeida(idNotificacion, usuario.id());
    }

    /** PATCH /api/notificaciones/leidas - marcar todas como leídas -> {"actualizadas": 5} */
    @PatchMapping("/leidas")
    public Map<String, Integer> marcarTodas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return Map.of("actualizadas", notificacionService.marcarTodasComoLeidas(usuario.id()));
    }

    /**
     * RF11 - POST /api/notificaciones ("+ Crear aviso", bibliotecario/gerente)
     * Body: {"id_usuario":5,"mensaje":"..."}  — sin id_usuario se envía a todos los lectores.
     * Respuesta: {"enviados": 120}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Integer> crearAviso(@Valid @RequestBody CrearAvisoDTO datos,
                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return Map.of("enviados", notificacionService.crearAvisoAdministrativo(datos, usuario.id()));
    }

    /** GET /api/notificaciones/todas?tipo=ADMINISTRATIVO&pagina=0&tamanio=20 (bibliotecario/gerente) */
    @GetMapping("/todas")
    public PaginaDTO<NotificacionResponseDTO> todas(@RequestParam(required = false) TipoNotificacion tipo,
                                                    @RequestParam(defaultValue = "0") Integer pagina,
                                                    @RequestParam(defaultValue = "20") Integer tamanio) {
        return notificacionService.listarTodas(tipo, pagina, tamanio);
    }

    /** POST /api/notificaciones/generar (GERENTE) - ejecuta a mano la tarea diaria de avisos. */
    @PostMapping("/generar")
    public ResultadoAvisosDTO generar() {
        return notificacionService.generarAvisosAutomaticos();
    }
}
