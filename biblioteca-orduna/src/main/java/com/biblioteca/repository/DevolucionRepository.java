package com.biblioteca.repository;

import com.biblioteca.entity.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
    boolean existsByPrestamoId(Long idPrestamo);

    long countByFechaDevolucionBetween(LocalDateTime desde, LocalDateTime hasta);

    List<Devolucion> findByPrestamoIdIn(Collection<Long> idsPrestamo);
}
