package com.biblioteca.controller;

import com.biblioteca.dto.GeneroResponseDTO;
import com.biblioteca.dto.RegistroGeneroDTO;
import com.biblioteca.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Módulo Catálogo - Géneros (tabla puente libros_generos). GET público; POST bibliotecario/admin. */
@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GeneroResponseDTO registrar(@Valid @RequestBody RegistroGeneroDTO datos) {
        return generoService.registrarGenero(datos);
    }

    @GetMapping
    public List<GeneroResponseDTO> listar() {
        return generoService.listarGeneros();
    }
}
