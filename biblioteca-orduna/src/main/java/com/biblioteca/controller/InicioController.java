package com.biblioteca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** GET / (público): sirve para comprobar en el navegador que la API está funcionando. */
@RestController
public class InicioController {

    @GetMapping("/")
    public Map<String, Object> inicio() {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("api", "Sistema de Biblioteca Orduña");
        r.put("estado", "funcionando");
        r.put("hora", LocalDateTime.now());
        r.put("login", "POST /api/auth/login");
        r.put("catalogo_publico", "GET /api/libros");
        return r;
    }
}
