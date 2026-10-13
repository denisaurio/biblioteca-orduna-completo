package com.biblioteca.repository;

import com.biblioteca.entity.Notificacion;
import com.biblioteca.entity.TipoNotificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    boolean existsByUsuarioIdAndTipoAndIdReferenciaAndFechaCreacionGreaterThanEqual(
            Long idUsuario, TipoNotificacion tipo, Long idReferencia, LocalDateTime desde);

    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDescIdDesc(Long idUsuario);

    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDescIdDesc(Long idUsuario);

    long countByUsuarioIdAndLeidaFalse(Long idUsuario);

    /** Vista del personal: todos los avisos, filtrables por tipo. */
    Page<Notificacion> findAllByOrderByFechaCreacionDescIdDesc(Pageable pageable);

    Page<Notificacion> findByTipoInOrderByFechaCreacionDescIdDesc(Collection<TipoNotificacion> tipos, Pageable pageable);
}
