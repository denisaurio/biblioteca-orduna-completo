package com.biblioteca.repository;

import com.biblioteca.entity.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SancionRepository extends JpaRepository<Sancion, Long> {
    /** Sanción activa: no levantada y con fecha_fin hoy o después. */
    boolean existsByUsuarioIdAndFechaFinGreaterThanEqualAndFechaLevantamientoIsNull(Long idUsuario, LocalDate hoy);

    Optional<Sancion> findFirstByUsuarioIdAndFechaFinGreaterThanEqualAndFechaLevantamientoIsNullOrderByFechaFinDesc(
            Long idUsuario, LocalDate hoy);

    List<Sancion> findAllByOrderByFechaInicioDesc();

    List<Sancion> findByFechaFinGreaterThanEqualAndFechaLevantamientoIsNullOrderByFechaFinAsc(LocalDate hoy);

    List<Sancion> findByUsuarioIdOrderByFechaInicioDesc(Long idUsuario);
}
