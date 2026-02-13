package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.CentroEducativo;
import com.example.gardenmonitor.model.Rol;
import com.example.gardenmonitor.model.Usuario;
import com.example.gardenmonitor.model.UsuarioCentro;
import com.example.gardenmonitor.repository.CentroEducativoRepository;
import com.example.gardenmonitor.repository.UsuarioCentroRepository;
import com.example.gardenmonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario-centro")
public class UsuarioCentroController {

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroEducativoRepository centroEducativoRepository;

    @GetMapping("/usuario/{usuarioId}")
    public List<UsuarioCentro> obtenerCentrosDeUsuario(@PathVariable("usuarioId") Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return usuarioCentroRepository.findByUsuarioId(usuarioId);
    }

    @GetMapping("/centro/{centroId}")
    public List<UsuarioCentro> obtenerCoordinadoresDeCentro(@PathVariable("centroId") Long centroId) {
        if (!centroEducativoRepository.existsById(centroId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro educativo no encontrado");
        }
        return usuarioCentroRepository.findByCentroEducativoId(centroId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioCentro asignarCoordinador(@RequestBody Map<String, Long> body) {
        Long usuarioId = body.get("usuarioId");
        Long centroId = body.get("centroId");

        if (usuarioId == null || centroId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requieren usuarioId y centroId");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        CentroEducativo centro = centroEducativoRepository.findById(centroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));

        if (usuario.getRol() != Rol.COORDINADOR) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Solo usuarios con rol COORDINADOR pueden ser asignados a centros");
        }

        if (usuarioCentroRepository.existsByUsuarioIdAndCentroEducativoId(usuarioId, centroId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El usuario ya está asignado a este centro");
        }

        UsuarioCentro usuarioCentro = new UsuarioCentro(usuario, centro);
        return usuarioCentroRepository.save(usuarioCentro);
    }

    @DeleteMapping("/{id}")
    public UsuarioCentro desasignarCoordinador(@PathVariable("id") Long id) {
        UsuarioCentro usuarioCentro = usuarioCentroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asignación no encontrada"));
        usuarioCentroRepository.deleteById(id);
        return usuarioCentro;
    }
}
