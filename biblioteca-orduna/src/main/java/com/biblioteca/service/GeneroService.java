package com.biblioteca.service;

import com.biblioteca.dto.GeneroResponseDTO;
import com.biblioteca.dto.RegistroGeneroDTO;
import com.biblioteca.entity.Genero;
import com.biblioteca.exception.RecursoDuplicadoException;
import com.biblioteca.repository.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    /** Registrar género (Novela, Historia, Ciencia...). RecursoDuplicadoException si ya existe. */
    @Transactional
    public GeneroResponseDTO registrarGenero(RegistroGeneroDTO datos) {
        String nombre = datos.nombre().trim();
        if (generoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new RecursoDuplicadoException("Ya existe el género '" + nombre + "'");
        }
        Genero g = new Genero();
        g.setNombre(nombre);
        return aDTO(generoRepository.save(g));
    }

    @Transactional(readOnly = true)
    public List<GeneroResponseDTO> listarGeneros() {
        return generoRepository.findAll().stream().map(GeneroService::aDTO)
                .sorted(Comparator.comparing(GeneroResponseDTO::nombre)).toList();
    }

    static GeneroResponseDTO aDTO(Genero g) {
        return new GeneroResponseDTO(g.getId(), g.getNombre());
    }
}
