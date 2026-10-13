package com.biblioteca.repository;

import com.biblioteca.entity.Ejemplar;
import com.biblioteca.entity.EstadoEjemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    boolean existsByCodigoInventario(String codigoInventario);
    long countByLibroIdAndEstado(Long idLibro, EstadoEjemplar estado);
    long countByLibroId(Long idLibro);
    long countByEstado(EstadoEjemplar estado);

    /** Primer ejemplar disponible de un libro (préstamo eligiendo solo el libro). */
    Optional<Ejemplar> findFirstByLibroIdAndEstadoOrderByIdAsc(Long idLibro, EstadoEjemplar estado);

    /** Pantalla Ejemplares: filtros opcionales por libro, estado y texto (código, ubicación o título). */
    @Query("""
            SELECT e FROM Ejemplar e JOIN e.libro l
            WHERE (:idLibro IS NULL OR l.id = :idLibro)
              AND (:estado IS NULL OR e.estado = :estado)
              AND (:q = ''
                   OR LOWER(e.codigoInventario) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(e.ubicacion) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY l.titulo, e.codigoInventario
            """)
    List<Ejemplar> buscar(@Param("idLibro") Long idLibro, @Param("estado") EstadoEjemplar estado,
                          @Param("q") String q);
}
