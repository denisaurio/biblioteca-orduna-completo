package com.biblioteca.controller;

import com.biblioteca.dto.LibroResponseDTO;
import com.biblioteca.dto.RegistroLibroDTO;
import com.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Módulo Catálogo - Libros (RF01, RF02). GET público; POST/PUT bibliotecario/gerente. */
@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /** RF01 - POST /api/libros */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroResponseDTO registrar(@Valid @RequestBody RegistroLibroDTO datos) {
        return libroService.registrarLibro(datos);
    }

    /** PUT /api/libros/{idLibro} - editar */
    @PutMapping("/{idLibro}")
    public LibroResponseDTO actualizar(@PathVariable Long idLibro, @Valid @RequestBody RegistroLibroDTO datos) {
        return libroService.actualizarLibro(idLibro, datos);
    }

    /** RF02 - GET /api/libros/buscar?q=ciro  (por título, ISBN, autor o género) */
    @GetMapping("/buscar")
    public List<LibroResponseDTO> buscar(@RequestParam(name = "q", required = false, defaultValue = "") String criterio) {
        return libroService.buscarLibros(criterio);
    }

    /** GET /api/libros - todo el catálogo */
    @GetMapping
    public List<LibroResponseDTO> listar() {
        return libroService.buscarLibros("");
    }

    /** GET /api/libros/destacados - para el Inicio (más prestados) */
    @GetMapping("/destacados")
    public List<LibroResponseDTO> destacados() {
        return libroService.destacados(6);
    }

    /** GET /api/libros/{idLibro} - "Ver detalles" */
    @GetMapping("/{idLibro}")
    public LibroResponseDTO obtener(@PathVariable Long idLibro) {
        return libroService.obtenerLibro(idLibro);
    }
}
