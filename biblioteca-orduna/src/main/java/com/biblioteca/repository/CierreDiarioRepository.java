package com.biblioteca.repository;

import com.biblioteca.entity.CierreDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CierreDiarioRepository extends JpaRepository<CierreDiario, Long> {
    boolean existsByFecha(LocalDate fecha);

    Optional<CierreDiario> findByFecha(LocalDate fecha);

    List<CierreDiario> findByFechaBetweenOrderByFechaDesc(LocalDate desde, LocalDate hasta);
}
