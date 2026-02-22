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

/**
 * Controlador REST para gestionar la relación N:M entre usuarios y centros educativos.
 * <p>
 * Proporciona endpoints para asignar y desasignar coordinadores a centros educativos,
 * así como para consultar las asignaciones existentes por usuario o por centro.
 * Solo usuarios con rol COORDINADOR pueden ser asignados a centros.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/usuario-centro")
public class UsuarioCentroController {

    @Autowired
    private UsuarioCentroRepository usuarioCentroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CentroEducativoRepository centroEducativoRepository;

    /**
     * Obtiene todos los centros educativos asignados a un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return lista de asignaciones del usuario
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<UsuarioCentro> obtenerCentrosDeUsuario(@PathVariable("usuarioId") Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return usuarioCentroRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Obtiene todos los coordinadores asignados a un centro educativo.
     *
     * @param centroId identificador del centro educativo
     * @return lista de asignaciones del centro
     * @throws ResponseStatusException si no se encuentra el centro (404)
     */
    @GetMapping("/centro/{centroId}")
    public List<UsuarioCentro> obtenerCoordinadoresDeCentro(@PathVariable("centroId") Long centroId) {
        if (!centroEducativoRepository.existsById(centroId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro educativo no encontrado");
        }
        return usuarioCentroRepository.findByCentroEducativoId(centroId);
    }

    /**
     * Asigna un coordinador a un centro educativo.
     * <p>
     * El cuerpo de la petición debe contener los campos {@code usuarioId} y {@code centroId}.
     * Solo usuarios con rol COORDINADOR pueden ser asignados.
     * No se permiten asignaciones duplicadas (mismo usuario y centro).
     * </p>
     *
     * @param body mapa con los campos usuarioId y centroId
     * @return la asignación creada
     * @throws ResponseStatusException si faltan campos (400), el usuario o centro no existen (404),
     *                                 el usuario no es COORDINADOR (400), o la asignación ya existe (409)
     */
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

    /**
     * Actualiza una asignación existente de usuario a centro.
     * <p>
     * Permite modificar los campos {@code activo}, {@code usuarioId} y {@code centroId}.
     * Solo se actualizan los campos presentes en el cuerpo de la petición.
     * </p>
     *
     * @param id   identificador de la asignación a actualizar
     * @param body mapa con los campos a actualizar
     * @return la asignación actualizada
     * @throws ResponseStatusException si no se encuentra la asignación (404),
     *                                 el usuario o centro no existen (404),
     *                                 el usuario no es COORDINADOR (400),
     *                                 o la asignación resultante ya existe (409)
     */
    @PutMapping("/{id}")
    public UsuarioCentro actualizarAsignacion(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> body) {

        UsuarioCentro usuarioCentro = usuarioCentroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asignación no encontrada"));

        if (body.containsKey("activo")) {
            if (!(body.get("activo") instanceof Boolean)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "El campo 'activo' debe ser true o false");
            }
            usuarioCentro.setActivo((Boolean) body.get("activo"));
        }

        if (body.containsKey("usuarioId")) {
            Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            if (usuario.getRol() != Rol.COORDINADOR) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Solo usuarios con rol COORDINADOR pueden ser asignados a centros");
            }
            Long centroId = usuarioCentro.getCentroEducativo().getId();
            if (!usuarioId.equals(usuarioCentro.getUsuario().getId())
                    && usuarioCentroRepository.existsByUsuarioIdAndCentroEducativoId(usuarioId, centroId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "El usuario ya está asignado a este centro");
            }
            usuarioCentro.setUsuario(usuario);
        }

        if (body.containsKey("centroId")) {
            Long centroId = Long.valueOf(body.get("centroId").toString());
            CentroEducativo centro = centroEducativoRepository.findById(centroId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));
            Long usuarioId = usuarioCentro.getUsuario().getId();
            if (!centroId.equals(usuarioCentro.getCentroEducativo().getId())
                    && usuarioCentroRepository.existsByUsuarioIdAndCentroEducativoId(usuarioId, centroId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "El usuario ya está asignado a este centro");
            }
            usuarioCentro.setCentroEducativo(centro);
        }

        return usuarioCentroRepository.save(usuarioCentro);
    }

    /**
     * Elimina una asignación de coordinador a centro por su ID.
     *
     * @param id identificador de la asignación a eliminar
     * @return la asignación eliminada
     * @throws ResponseStatusException si no se encuentra la asignación (404)
     */
    @DeleteMapping("/{id}")
    public UsuarioCentro desasignarCoordinador(@PathVariable("id") Long id) {
        UsuarioCentro usuarioCentro = usuarioCentroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Asignación no encontrada"));
        usuarioCentroRepository.deleteById(id);
        return usuarioCentro;
    }
}
