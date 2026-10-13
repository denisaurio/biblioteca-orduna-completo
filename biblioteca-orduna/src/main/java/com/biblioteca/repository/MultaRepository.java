package com.biblioteca.repository;

import com.biblioteca.entity.EstadoMulta;
import com.biblioteca.entity.Multa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MultaRepository extends JpaRepository<Multa, Long> {
    /** true si el usuario debe algo (multa PENDIENTE o PARCIAL). */
    boolean existsByUsuarioIdAndEstadoIn(Long idUsuario, Collection<EstadoMulta> estados);

    List<Multa> findByEstadoIn(Collection<EstadoMulta> estados);

    List<Multa> findByEstadoInOrderByFechaGeneracionDesc(Collection<EstadoMulta> estados);

    List<Multa> findAllByOrderByFechaGeneracionDesc();

    List<Multa> findByUsuarioIdOrderByFechaGeneracionDesc(Long idUsuario);

    List<Multa> findByFechaGeneracionBetweenOrderByFechaGeneracionAsc(LocalDateTime desde, LocalDateTime hasta);
}
