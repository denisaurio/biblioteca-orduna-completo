package com.biblioteca.controller;

import com.biblioteca.dto.LoginDTO;
import com.biblioteca.dto.LoginResponseDTO;
import com.biblioteca.entity.Rol;
import com.biblioteca.entity.Usuario;
import com.biblioteca.exception.CredencialesInvalidasException;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.security.JwtService;
import com.biblioteca.service.SancionService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/** RF05 - Inicio de sesión. Endpoint público. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SancionService sancionService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          JwtService jwtService, SancionService sancionService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.sancionService = sancionService;
    }

    /**
     * POST /api/auth/login
     * Body: {"email":"admin@biblioteca.pe","password":"admin123"}
     * 200: {"token":"eyJ...","tipo":"Bearer","expira_en_ms":28800000,"id_usuario":1,"nombre":"...","email":"...","rol":"GERENTE"}
     * 401: credenciales incorrectas o cuenta desactivada
     * 403: lector con sanción activa (el documento indica que el acceso queda restringido)
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginDTO datos) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(datos.email().trim())
                // Mismo mensaje si no existe el email o si la clave está mal (no revela qué emails existen)
                .filter(u -> passwordEncoder.matches(datos.password(), u.getPassword()))
                .orElseThrow(() -> new CredencialesInvalidasException("Email o contraseña incorrectos"));

        if (!usuario.isActivo()) {
            throw new CredencialesInvalidasException("La cuenta está desactivada. Comuníquese con la biblioteca");
        }
        if (usuario.getRol() == Rol.LECTOR) {
            sancionService.verificarSinSancion(usuario.getId());
        }

        return new LoginResponseDTO(jwtService.generarToken(usuario), "Bearer", jwtService.getExpiracionMs(),
                usuario.getId(), usuario.getNombreCompleto(), usuario.getEmail(), usuario.getRol().name());
    }
}
