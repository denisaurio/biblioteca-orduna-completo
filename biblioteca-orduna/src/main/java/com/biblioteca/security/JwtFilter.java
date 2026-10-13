package com.biblioteca.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Se ejecuta en CADA petición:
 * 1. Lee el header "Authorization: Bearer &lt;token&gt;".
 * 2. Valida el token con JwtService.
 * 3. Si es válido, guarda al usuario en el SecurityContext con su rol como
 *    autoridad (ROLE_LECTOR, ROLE_BIBLIOTECARIO o ROLE_GERENTE).
 *    SecurityConfig usa esas autoridades para decidir qué endpoints puede usar.
 * 4. Si no hay token, sigue como anónimo (solo podrá entrar a los endpoints públicos).
 * 5. Si el token es inválido/expirado, deja el motivo en la petición para que
 *    SecurityErrorHandler responda 401 con un mensaje claro.
 *
 * No es @Component a propósito: se crea en SecurityConfig para que no se registre dos veces.
 */
public class JwtFilter extends OncePerRequestFilter {

    public static final String ATRIBUTO_ERROR_TOKEN = "jwt_error";
    private static final String PREFIJO = "Bearer ";

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(PREFIJO)) {
            String token = header.substring(PREFIJO.length()).trim();
            try {
                UsuarioAutenticado usuario = jwtService.validarToken(token);
                var autoridades = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol().name()));
                var auth = new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (ExpiredJwtException e) {
                SecurityContextHolder.clearContext();
                request.setAttribute(ATRIBUTO_ERROR_TOKEN, "El token expiró. Inicie sesión nuevamente");
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                request.setAttribute(ATRIBUTO_ERROR_TOKEN, "Token inválido");
            }
        }
        chain.doFilter(request, response);
    }
}
