package com.biblioteca.repository;

import com.biblioteca.entity.ConfiguracionMulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfiguracionMultaRepository extends JpaRepository<ConfiguracionMulta, Long> {
    /** La fila vigente. */
    Optional<ConfiguracionMulta> findFirstByFechaVigenciaHastaIsNullOrderByIdDesc();

    List<ConfiguracionMulta> findAllByOrderByFechaVigenciaDesdeDesc();
}
