package com.biblioteca.controller;

import com.biblioteca.dto.AutorResponseDTO;
import com.biblioteca.dto.LibroResponseDTO;
import com.biblioteca.dto.RegistroAutorDTO;
import com.biblioteca.service.AutorService;
import com.biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Módulo Autores (RF03). GET público; alta/edición/eliminación bibliotecario/gerente. */
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;
    private final LibroService libroService;

    public AutorController(AutorService autorService, LibroService libroService) {
        this.autorService = autorService;
        this.libroService = libroService;
    }

    /** RF03 - POST /api/autores  Body: {"nombre":"Ciro Alegría","nacionalidad":"Peruana","biografia":"..."} */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutorResponseDTO registrar(@Valid @RequestBody RegistroAutorDTO datos) {
        return autorService.registrarAutor(datos);
    }

    /** PUT /api/autores/{idAutor} */
    @PutMapping("/{idAutor}")
    public AutorResponseDTO actualizar(@PathVariable Long idAutor, @Valid @RequestBody RegistroAutorDTO datos) {
        return autorService.actualizarAutor(idAutor, datos);
    }

    /** DELETE /api/autores/{idAutor} - 400 si tiene libros asociados. */
    @DeleteMapping("/{idAutor}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idAutor) {
        autorService.eliminarAutor(idAutor);
    }

    @GetMapping
    public List<AutorResponseDTO> listar() {
        return autorService.listarAutores();
    }

    @GetMapping("/{idAutor}")
    public AutorResponseDTO obtener(@PathVariable Long idAutor) {
        return autorService.obtenerAutor(idAutor);
    }

    /** GET /api/autores/{idAutor}/libros - "Ver libros" */
    @GetMapping("/{idAutor}/libros")
    public List<LibroResponseDTO> libros(@PathVariable Long idAutor) {
        return libroService.librosDeAutor(idAutor);
    }
}
