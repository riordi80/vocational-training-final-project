package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.dto.AuthResponse;
import com.example.gardenmonitor.dto.LoginRequest;
import com.example.gardenmonitor.dto.RegisterRequest;
import com.example.gardenmonitor.model.Rol;
import com.example.gardenmonitor.model.Usuario;
import com.example.gardenmonitor.model.UsuarioCentro;
import com.example.gardenmonitor.repository.UsuarioCentroRepository;
import com.example.gardenmonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

/**
 * Controlador REST para autenticación de usuarios.
 * <p>
 * Proporciona endpoints de login y registro de nuevos usuarios.
 * Actualmente las contraseñas se almacenan en texto plano.
 * BCrypt se añadirá en la Fase 15 junto con la implementación de JWT.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    /**
     * Autentica a un usuario con email y contraseña.
     * <p>
     * Devuelve los datos del usuario autenticado junto con los centros
     * educativos asignados (para coordinadores).
     * </p>
     *
     * @param request credenciales de acceso (email y password)
     * @return respuesta con los datos del usuario autenticado
     * @throws ResponseStatusException si las credenciales son incorrectas (401)
     *                                 o el usuario está inactivo (403)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        // Plain text comparison for now — BCrypt will be added with JWT
        if (!usuario.getPasswordHash().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no activo");
        }

        List<UsuarioCentro> asignaciones = usuarioCentroRepository.findByUsuarioId(usuario.getId());
        List<AuthResponse.CentroRef> centros = asignaciones.stream()
                .map(uc -> new AuthResponse.CentroRef(uc.getCentroEducativo().getId()))
                .toList();

        AuthResponse response = new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                centros
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * El nuevo usuario se crea con rol COORDINADOR por defecto y sin centros asignados.
     * Un administrador deberá asignarle centros posteriormente.
     * </p>
     *
     * @param request datos del nuevo usuario (nombre, email y password)
     * @return respuesta con los datos del usuario registrado
     * @throws ResponseStatusException si el email ya está registrado (409)
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                request.getPassword(), // Plain text for now — BCrypt with JWT
                Rol.COORDINADOR
        );

        usuario = usuarioRepository.save(usuario);

        AuthResponse response = new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                Collections.emptyList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
