package com.biblioteca.repository;

import com.biblioteca.entity.Rol;
import com.biblioteca.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByDni(String dni);
    boolean existsByEmailIgnoreCase(String email);
    /** Login (RF05). */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    long countByRol(Rol rol);

    /** Pantalla Usuarios: búsqueda por nombre, apellido, DNI o email, con filtro opcional de rol. */
    @Query("""
            SELECT u FROM Usuario u
            WHERE (:rol IS NULL OR u.rol = :rol)
              AND (:q = ''
                   OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR u.dni LIKE CONCAT('%', :q, '%')
                   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<Usuario> buscar(@Param("q") String q, @Param("rol") Rol rol, Pageable pageable);

    java.util.List<Usuario> findByRolAndActivoTrue(Rol rol);
}
