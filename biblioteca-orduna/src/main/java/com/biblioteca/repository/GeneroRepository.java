package com.biblioteca.repository;

import com.biblioteca.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
