package com.biblioteca.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad: API sin sesión (stateless) con JWT, CORS global y
 * permisos por rol. Toda la matriz de permisos está aquí, en un solo lugar.
 *
 * Matriz de permisos (roles del documento: LECTOR, BIBLIOTECARIO, GERENTE):
 *   Público .......................... login, registro de lector, GET catálogo (libros, autores,
 *                                      géneros) y GET /api/configuracion (nombre/horario)
 *   GERENTE .......................... PUT /api/configuracion, historial de configuración,
 *                                      POST /api/notificaciones/generar
 *   LECTOR ........................... POST /api/reservas
 *   Cualquier usuario logueado ....... /api/mi/** (mis préstamos, multas, reservas, panel),
 *                                      /api/usuarios/me, mis notificaciones, cancelar mi reserva
 *   BIBLIOTECARIO o GERENTE .......... todo lo demás de gestión: catálogo (escritura), ejemplares,
 *                                      préstamos, devoluciones, multas, reservas (listado/cola),
 *                                      usuarios, sanciones, turnos, reportes, cierre diario,
 *                                      dashboard y crear avisos
 */
@Configuration
public class SecurityConfig {

    private static final String LECTOR = "LECTOR";
    private static final String BIBLIOTECARIO = "BIBLIOTECARIO";
    private static final String GERENTE = "GERENTE";

    private final JwtService jwtService;
    private final SecurityErrorHandler errorHandler;

    @Value("${cors.allowed-origins}")
    private String origenesPermitidos;

    public SecurityConfig(JwtService jwtService, SecurityErrorHandler errorHandler) {
        this.jwtService = jwtService;
        this.errorHandler = errorHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)            // API REST con JWT: no usa cookies de sesión
                .cors(Customizer.withDefaults())                  // usa corsConfigurationSource()
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(errorHandler)  // 401
                        .accessDeniedHandler(errorHandler))      // 403
                .authorizeHttpRequests(auth -> auth
                        // ======== Público ========
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/libros/**", "/api/autores/**", "/api/generos/**",
                                "/api/configuracion").permitAll()

                        // ======== Solo GERENTE ========
                        .requestMatchers(HttpMethod.PUT, "/api/configuracion/**").hasRole(GERENTE)
                        .requestMatchers(HttpMethod.GET, "/api/configuracion/historial").hasRole(GERENTE)
                        .requestMatchers(HttpMethod.POST, "/api/notificaciones/generar").hasRole(GERENTE)

                        // ======== Solo LECTOR ========
                        .requestMatchers(HttpMethod.POST, "/api/reservas").hasRole(LECTOR)

                        // ======== Cualquier usuario logueado (datos propios) ========
                        .requestMatchers("/api/mi/**", "/api/usuarios/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notificaciones", "/api/notificaciones/no-leidas/total")
                        .authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/notificaciones/*/leida", "/api/notificaciones/leidas",
                                "/api/reservas/*/cancelar").authenticated()

                        // ======== BIBLIOTECARIO o GERENTE ========
                        .requestMatchers("/api/libros/**", "/api/autores/**", "/api/generos/**", "/api/ejemplares/**",
                                "/api/prestamos/**", "/api/devoluciones/**", "/api/multas/**", "/api/reservas/**",
                                "/api/usuarios/**", "/api/sanciones/**", "/api/turnos/**", "/api/reportes/**",
                                "/api/cierre-diario/**", "/api/dashboard/**", "/api/notificaciones/**")
                        .hasAnyRole(BIBLIOTECARIO, GERENTE)

                        // ======== Resto: basta con estar logueado ========
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** CORS global: permite que el frontend (otro puerto/dominio) consuma la API. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.stream(origenesPermitidos.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        // Para que el frontend pueda leer el nombre del archivo CSV descargado
        config.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Encriptador de contraseñas (BCrypt). Lo usan AuthController (login) y UsuarioService (registro).
     *
     * IMPORTANTE: este bean REEMPLAZA al de config/PasswordConfig.java.
     * Si todavía tienes ese archivo, BÓRRALO para evitar el error de beans duplicados
     * ("expected single matching bean but found 2").
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
