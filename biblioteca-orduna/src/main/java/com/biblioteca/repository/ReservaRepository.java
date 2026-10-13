package com.biblioteca.repository;

import com.biblioteca.entity.EstadoReserva;
import com.biblioteca.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByLibroIdAndLectorIdAndEstadoIn(Long idLibro, Long idLector, Collection<EstadoReserva> estados);

    /** Primero de la cola FIFO de un libro. */
    Optional<Reserva> findFirstByLibroIdAndEstadoOrderByFechaReservaAscIdAsc(Long idLibro, EstadoReserva estado);

    Optional<Reserva> findFirstByLibroIdAndLectorIdAndEstado(Long idLibro, Long idLector, EstadoReserva estado);

    Optional<Reserva> findFirstByEjemplarAsignadoIdAndEstado(Long idEjemplar, EstadoReserva estado);

    long countByEstado(EstadoReserva estado);

    long countByLibroIdAndEstadoAndFechaReservaLessThanEqual(Long idLibro, EstadoReserva estado, LocalDateTime fecha);

    /** Cola de un libro en orden de llegada. */
    List<Reserva> findByLibroIdAndEstadoInOrderByFechaReservaAscIdAsc(Long idLibro, Collection<EstadoReserva> estados);

    List<Reserva> findByEstadoInOrderByFechaReservaAscIdAsc(Collection<EstadoReserva> estados);

    List<Reserva> findAllByOrderByFechaReservaDesc();

    List<Reserva> findByLectorIdOrderByFechaReservaDesc(Long idLector);

    /** NOTIFICADAS cuyo plazo de retiro ya pasó. */
    List<Reserva> findByEstadoAndFechaLimiteRetiroBefore(EstadoReserva estado, LocalDate fecha);
}
