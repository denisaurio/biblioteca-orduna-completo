package com.biblioteca.repository;

import com.biblioteca.entity.EstadoTurno;
import com.biblioteca.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    long countByEstado(EstadoTurno estado);

    Optional<Turno> findFirstByBibliotecarioIdAndEstado(Long idBibliotecario, EstadoTurno estado);

    /** Bitácora completa (gerente). */
    List<Turno> findByFechaAperturaBetweenOrderByFechaAperturaDesc(LocalDateTime desde, LocalDateTime hasta);

    /** Bitácora de un bibliotecario. */
    List<Turno> findByBibliotecarioIdAndFechaAperturaBetweenOrderByFechaAperturaDesc(
            Long idBibliotecario, LocalDateTime desde, LocalDateTime hasta);
}
