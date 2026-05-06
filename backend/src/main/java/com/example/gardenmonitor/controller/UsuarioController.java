package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Rol;
import com.example.gardenmonitor.model.Usuario;
import com.example.gardenmonitor.repository.UsuarioCentroRepository;
import com.example.gardenmonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar usuarios del sistema.
 * <p>
 * Proporciona endpoints para operaciones CRUD de usuarios, así como
 * consultas filtradas por rol y estado (activo/inactivo).
 * También incluye endpoints PATCH para activar y desactivar usuarios
 * sin necesidad de enviar el objeto completo.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return lista de todos los usuarios
     */
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable("id") Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    /**
     * Obtiene un usuario por su dirección de email.
     *
     * @param email dirección de email del usuario
     * @return el usuario encontrado
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @GetMapping("/email/{email}")
    public Usuario obtenerUsuarioPorEmail(@PathVariable("email") String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    /**
     * Obtiene todos los usuarios activos.
     *
     * @return lista de usuarios con activo = true
     */
    @GetMapping("/activos")
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivo(true);
    }

    /**
     * Obtiene todos los usuarios inactivos.
     *
     * @return lista de usuarios con activo = false
     */
    @GetMapping("/inactivos")
    public List<Usuario> obtenerUsuariosInactivos() {
        return usuarioRepository.findByActivo(false);
    }

    /**
     * Obtiene todos los usuarios con un rol determinado.
     *
     * @param rol rol a filtrar (ADMIN o COORDINADOR)
     * @return lista de usuarios con el rol indicado
     */
    @GetMapping("/rol/{rol}")
    public List<Usuario> obtenerUsuariosPorRol(@PathVariable("rol") Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * <p>
     * Valida que el email no esté ya registrado.
     * </p>
     *
     * @param usuario datos del usuario a crear
     * @return el usuario creado
     * @throws ResponseStatusException si el email ya está registrado (409)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado");
        }
        Rol rol = body.containsKey("rol") ? Rol.valueOf(body.get("rol")) : Rol.COORDINADOR;
        String rawPassword = body.containsKey("password") ? body.get("password") : body.get("passwordHash");
        Usuario usuario = new Usuario(
                body.get("nombre"),
                email,
                passwordEncoder.encode(rawPassword),
                rol
        );
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     * <p>
     * Valida que el nuevo email no esté en uso por otro usuario (si se cambia el email).
     * Solo actualiza la contraseña si se proporciona un nuevo valor no vacío.
     * </p>
     *
     * @param id             identificador del usuario a actualizar
     * @param detallesUsuario datos actualizados del usuario
     * @return el usuario actualizado
     * @throws ResponseStatusException si no se encuentra el usuario (404) o el email ya existe (409)
     */
    @PutMapping("/{id}")
    @Transactional
    public Usuario actualizarUsuario(@PathVariable("id") Long id,
                                      @RequestBody Usuario detallesUsuario) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!usuario.getEmail().equals(detallesUsuario.getEmail())
                && usuarioRepository.existsByEmail(detallesUsuario.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado");
        }

        // Si el rol cambia de COORDINADOR a ADMIN, limpiar asignaciones de centros
        if (detallesUsuario.getRol() == Rol.ADMIN && usuario.getRol() != Rol.ADMIN) {
            usuarioCentroRepository.deleteByUsuarioId(id);
        }

        usuario.setNombre(detallesUsuario.getNombre());
        usuario.setEmail(detallesUsuario.getEmail());
        usuario.setRol(detallesUsuario.getRol());
        usuario.setActivo(detallesUsuario.getActivo());

        if (detallesUsuario.getPasswordHash() != null
                && !detallesUsuario.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(detallesUsuario.getPasswordHash()));
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario a eliminar
     * @return el usuario eliminado
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @DeleteMapping("/{id}")
    @Transactional
    public Usuario eliminarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioCentroRepository.deleteByUsuarioId(id);
        usuarioRepository.deleteById(id);
        return usuario;
    }

    /**
     * Desactiva un usuario (activo = false).
     * <p>
     * El usuario desactivado no podrá autenticarse en el sistema.
     * </p>
     *
     * @param id identificador del usuario a desactivar
     * @return el usuario desactivado
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @PatchMapping("/{id}/desactivar")
    public Usuario desactivarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }

    /**
     * Activa un usuario (activo = true).
     * <p>
     * Permite reactivar un usuario que había sido desactivado previamente.
     * </p>
     *
     * @param id identificador del usuario a activar
     * @return el usuario activado
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @PatchMapping("/{id}/activar")
    public Usuario activarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
}
