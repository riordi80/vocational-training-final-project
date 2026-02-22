package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Alerta;
import com.example.gardenmonitor.model.Notificacion;
import com.example.gardenmonitor.model.Usuario;
import com.example.gardenmonitor.repository.AlertaRepository;
import com.example.gardenmonitor.repository.NotificacionRepository;
import com.example.gardenmonitor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar notificaciones de usuarios.
 * <p>
 * Proporciona endpoints para operaciones CRUD de notificaciones.
 * Una notificación vincula una alerta con un usuario destinatario,
 * permitiendo rastrear qué usuarios han sido informados de cada alerta
 * y si la han leído.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    /**
     * Obtiene todas las notificaciones del sistema.
     *
     * @return lista de todas las notificaciones
     */
    @GetMapping
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return notificacionRepository.findAll();
    }

    /**
     * Obtiene una notificación por su ID.
     *
     * @param id identificador de la notificación
     * @return la notificación encontrada
     * @throws ResponseStatusException si no se encuentra la notificación (404)
     */
    @GetMapping("/{id}")
    public Notificacion obtenerNotificacionPorId(@PathVariable("id") Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notificación no encontrada"));
    }

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     *
     * @param usuarioId identificador del usuario
     * @return lista de notificaciones del usuario (leídas y no leídas)
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<Notificacion> obtenerNotificacionesPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return notificacionRepository.findByUsuario_Id(usuarioId);
    }

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     *
     * @param usuarioId identificador del usuario
     * @return lista de notificaciones no leídas del usuario
     * @throws ResponseStatusException si no se encuentra el usuario (404)
     */
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public List<Notificacion> obtenerNoLeidasPorUsuario(@PathVariable("usuarioId") Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return notificacionRepository.findByUsuario_IdAndLeida(usuarioId, false);
    }

    /**
     * Crea una nueva notificación vinculando un usuario con una alerta.
     * <p>
     * El cuerpo de la petición debe contener los campos {@code usuarioId} y {@code alertaId}.
     * La notificación se crea como no leída.
     * </p>
     *
     * @param body mapa con los campos usuarioId y alertaId
     * @return la notificación creada
     * @throws ResponseStatusException si faltan campos (400), o no se encuentra
     *                                 el usuario (404) o la alerta (404)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notificacion crearNotificacion(@RequestBody Map<String, Long> body) {
        Long usuarioId = body.get("usuarioId");
        Long alertaId = body.get("alertaId");

        if (usuarioId == null || alertaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Se requieren usuarioId y alertaId");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alerta no encontrada"));

        return notificacionRepository.save(new Notificacion(usuario, alerta));
    }

    /**
     * Actualiza una notificación existente.
     * <p>
     * Actualmente solo permite modificar el campo {@code leida}.
     * El usuario y la alerta asociados no son modificables una vez creada la notificación.
     * </p>
     *
     * @param id   identificador de la notificación a actualizar
     * @param body mapa con el campo leida (true/false)
     * @return la notificación actualizada
     * @throws ResponseStatusException si no se encuentra la notificación (404)
     *                                 o el valor de leida no es booleano (400)
     */
    @PutMapping("/{id}")
    public Notificacion actualizarNotificacion(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> body) {

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notificación no encontrada"));

        if (body.containsKey("leida")) {
            if (!(body.get("leida") instanceof Boolean)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "El campo 'leida' debe ser true o false");
            }
            notificacion.setLeida((Boolean) body.get("leida"));
        }

        return notificacionRepository.save(notificacion);
    }

    /**
     * Elimina una notificación por su ID.
     *
     * @param id identificador de la notificación a eliminar
     * @return la notificación eliminada
     * @throws ResponseStatusException si no se encuentra la notificación (404)
     */
    @DeleteMapping("/{id}")
    public Notificacion eliminarNotificacion(@PathVariable("id") Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notificación no encontrada"));
        notificacionRepository.deleteById(id);
        return notificacion;
    }
}
