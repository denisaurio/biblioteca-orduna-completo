package com.biblioteca.repository;

import com.biblioteca.entity.EstadoPrestamo;
import com.biblioteca.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByLectorIdAndEstado(Long idLector, EstadoPrestamo estado);

    long countByEstado(EstadoPrestamo estado);

    /** Vencidos = activos de fecha fija con fecha anterior (los ABIERTOS tienen fecha null y no cuentan). */
    long countByEstadoAndFechaVencimientoBefore(EstadoPrestamo estado, LocalDate fecha);

    long countByFechaPrestamoBetween(LocalDateTime desde, LocalDateTime hasta);

    boolean existsByLectorIdAndEjemplarLibroIdAndEstado(Long idLector, Long idLibro, EstadoPrestamo estado);

    /** Préstamos activos que vencen exactamente en la fecha dada (aviso previo). */
    List<Prestamo> findByEstadoAndFechaVencimiento(EstadoPrestamo estado, LocalDate fecha);

    /** Préstamos activos ya vencidos. */
    List<Prestamo> findByEstadoAndFechaVencimientoBefore(EstadoPrestamo estado, LocalDate fecha);

    List<Prestamo> findByFechaPrestamoBetweenOrderByFechaPrestamoAsc(LocalDateTime desde, LocalDateTime hasta);

    List<Prestamo> findByEstadoOrderByFechaVencimientoAsc(EstadoPrestamo estado);

    List<Prestamo> findByLectorIdOrderByFechaPrestamoDesc(Long idLector);

    /** Libros más prestados de la historia: [id_libro, cantidad]. */
    @Query("SELECT p.ejemplar.libro.id, COUNT(p) FROM Prestamo p GROUP BY p.ejemplar.libro.id ORDER BY COUNT(p) DESC")
    List<Object[]> contarPorLibro();
}
