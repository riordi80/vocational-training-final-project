package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Rol;
import com.example.gardenmonitor.model.Usuario;
import com.example.gardenmonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable("id") Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @GetMapping("/email/{email}")
    public Usuario obtenerUsuarioPorEmail(@PathVariable("email") String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @GetMapping("/activos")
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivo(true);
    }

    @GetMapping("/inactivos")
    public List<Usuario> obtenerUsuariosInactivos() {
        return usuarioRepository.findByActivo(false);
    }

    @GetMapping("/rol/{rol}")
    public List<Usuario> obtenerUsuariosPorRol(@PathVariable("rol") Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable("id") Long id, 
                                      @RequestBody Usuario detallesUsuario) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Verificar si el nuevo email ya existe (y no es del mismo usuario)
        if (!usuario.getEmail().equals(detallesUsuario.getEmail()) 
                && usuarioRepository.existsByEmail(detallesUsuario.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado");
        }

        usuario.setNombre(detallesUsuario.getNombre());
        usuario.setEmail(detallesUsuario.getEmail());
        usuario.setRol(detallesUsuario.getRol());
        usuario.setActivo(detallesUsuario.getActivo());
        
        // Solo actualizar password si se proporciona uno nuevo
        if (detallesUsuario.getPasswordHash() != null 
                && !detallesUsuario.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(detallesUsuario.getPasswordHash());
        }

        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public Usuario eliminarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioRepository.deleteById(id);
        return usuario;
    }

    @PatchMapping("/{id}/desactivar")
    public Usuario desactivarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }

    @PatchMapping("/{id}/activar")
    public Usuario activarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
}