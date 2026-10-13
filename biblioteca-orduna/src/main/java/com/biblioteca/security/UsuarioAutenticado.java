package com.biblioteca.security;

import com.biblioteca.entity.Rol;

/**
 * Datos del usuario logueado, sacados del JWT.
 * En los controllers se recibe así:
 * <pre>
 *   public X metodo(@AuthenticationPrincipal UsuarioAutenticado usuario) {
 *       Long idBibliotecario = usuario.id();
 *   }
 * </pre>
 * Así los ids de bibliotecario / lector / administrador NUNCA vienen del body.
 */
public record UsuarioAutenticado(Long id, String email, String nombre, Rol rol) {
}
