package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Alerta;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.EstadoAlerta;
import com.example.gardenmonitor.repository.AlertaRepository;
import com.example.gardenmonitor.repository.DispositivoEsp32Repository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para gestionar alertas del sistema de monitorización.
 * <p>
 * Proporciona endpoints para operaciones CRUD de alertas, así como
 * consultas filtradas por dispositivo y por estado.
 * Las alertas se generan cuando los valores de sensores superan los umbrales
 * configurados en cada dispositivo.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private DispositivoEsp32Repository dispositivoRepository;

    /**
     * Obtiene todas las alertas del sistema.
     *
     * @return lista de todas las alertas
     */
    @GetMapping
    public List<Alerta> obtenerTodasLasAlertas() {
        return alertaRepository.findAll();
    }

    /**
     * Obtiene una alerta por su ID.
     *
     * @param id identificador de la alerta
     * @return la alerta encontrada
     * @throws ResponseStatusException si no se encuentra la alerta (404)
     */
    @GetMapping("/{id}")
    public Alerta obtenerAlertaPorId(@PathVariable("id") Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alerta no encontrada"));
    }

    /**
     * Obtiene todas las alertas de un dispositivo específico.
     *
     * @param dispositivoId identificador del dispositivo
     * @return lista de alertas del dispositivo
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @GetMapping("/dispositivo/{dispositivoId}")
    public List<Alerta> obtenerAlertasPorDispositivo(@PathVariable("dispositivoId") Long dispositivoId) {
        if (!dispositivoRepository.existsById(dispositivoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo no encontrado");
        }
        return alertaRepository.findByDispositivo_Id(dispositivoId);
    }

    /**
     * Obtiene todas las alertas con un estado determinado.
     *
     * @param estado estado de las alertas a filtrar (ACTIVA, RESUELTA, IGNORADA)
     * @return lista de alertas con el estado indicado
     */
    @GetMapping("/estado/{estado}")
    public List<Alerta> obtenerAlertasPorEstado(@PathVariable("estado") EstadoAlerta estado) {
        return alertaRepository.findByEstado(estado);
    }

    /**
     * Obtiene las alertas de un dispositivo filtradas por estado.
     *
     * @param dispositivoId identificador del dispositivo
     * @param estado        estado de las alertas a filtrar (ACTIVA, RESUELTA, IGNORADA)
     * @return lista de alertas del dispositivo con el estado indicado
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @GetMapping("/dispositivo/{dispositivoId}/estado/{estado}")
    public List<Alerta> obtenerAlertasPorDispositivoYEstado(
            @PathVariable("dispositivoId") Long dispositivoId,
            @PathVariable("estado") EstadoAlerta estado) {
        if (!dispositivoRepository.existsById(dispositivoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo no encontrado");
        }
        return alertaRepository.findByDispositivo_IdAndEstado(dispositivoId, estado);
    }

    /**
     * Crea una nueva alerta para un dispositivo.
     * <p>
     * Verifica que el dispositivo indicado exista antes de crear la alerta.
     * El estado inicial es ACTIVA y el timestamp se establece automáticamente.
     * </p>
     *
     * @param alerta datos de la alerta a crear (validado con @Valid)
     * @return la alerta creada
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alerta crearAlerta(@Valid @RequestBody Alerta alerta) {
        Long dispositivoId = alerta.getDispositivo() != null ? alerta.getDispositivo().getId() : null;
        if (dispositivoId == null || !dispositivoRepository.existsById(dispositivoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo no encontrado");
        }
        DispositivoEsp32 dispositivo = dispositivoRepository.findById(dispositivoId).get();
        alerta.setDispositivo(dispositivo);
        return alertaRepository.save(alerta);
    }

    /**
     * Actualiza una alerta existente.
     * <p>
     * Permite modificar el tipo, mensaje, estado y fecha de resolución.
     * El dispositivo asociado no se puede cambiar una vez creada la alerta.
     * </p>
     *
     * @param id      identificador de la alerta a actualizar
     * @param detalles datos actualizados de la alerta (validado con @Valid)
     * @return la alerta actualizada
     * @throws ResponseStatusException si no se encuentra la alerta (404)
     */
    @PutMapping("/{id}")
    public Alerta actualizarAlerta(
            @PathVariable("id") Long id,
            @Valid @RequestBody Alerta detalles) {

        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alerta no encontrada"));

        alerta.setTipoAlerta(detalles.getTipoAlerta());
        alerta.setMensaje(detalles.getMensaje());
        alerta.setEstado(detalles.getEstado());
        alerta.setFechaResolucion(detalles.getFechaResolucion());

        return alertaRepository.save(alerta);
    }

    /**
     * Elimina una alerta por su ID.
     * <p>
     * IMPORTANTE: Al eliminar una alerta, se eliminan en cascada todas las
     * notificaciones asociadas a ella.
     * </p>
     *
     * @param id identificador de la alerta a eliminar
     * @return la alerta eliminada
     * @throws ResponseStatusException si no se encuentra la alerta (404)
     */
    @DeleteMapping("/{id}")
    public Alerta eliminarAlerta(@PathVariable("id") Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alerta no encontrada"));
        alertaRepository.deleteById(id);
        return alerta;
    }
}
