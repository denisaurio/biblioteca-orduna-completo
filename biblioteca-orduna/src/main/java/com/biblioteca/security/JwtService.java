package com.biblioteca.security;

import com.biblioteca.entity.Rol;
import com.biblioteca.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Genera y valida los JWT (HS256).
 * El token guarda: sub = email, id, nombre y rol del usuario.
 */
@Service
public class JwtService {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_NOMBRE = "nombre";
    private static final String CLAIM_ROL = "rol";

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${jwt.secret}") String secreto,
                      @Value("${jwt.expiration-ms}") long expiracionMs) {
        if (secreto.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("jwt.secret debe tener al menos 32 caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    /** Crea el token para un usuario que acaba de hacer login. */
    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim(CLAIM_ID, usuario.getId())
                .claim(CLAIM_NOMBRE, usuario.getNombreCompleto())
                .claim(CLAIM_ROL, usuario.getRol().name())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    /**
     * Valida firma y expiración y devuelve el usuario del token.
     * @throws JwtException si el token es inválido, fue alterado o expiró.
     */
    public UsuarioAutenticado validarToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object id = claims.get(CLAIM_ID);
        Object rol = claims.get(CLAIM_ROL);
        if (!(id instanceof Number) || rol == null) {
            throw new JwtException("El token no contiene id o rol");
        }
        return new UsuarioAutenticado(((Number) id).longValue(), claims.getSubject(),
                claims.get(CLAIM_NOMBRE, String.class), Rol.valueOf(rol.toString()));
    }

    public long getExpiracionMs() {
        return expiracionMs;
    }
}
