package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.repository.DispositivoEsp32Repository;
import com.example.gardenmonitor.repository.CentroEducativoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para gestionar dispositivos ESP32.
 * <p>
 * Proporciona endpoints para operaciones CRUD de dispositivos ESP32,
 * que son los sensores IoT encargados de monitorizar los árboles.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoEsp32Controller {

    @Autowired
    private DispositivoEsp32Repository dispositivoRepository;

    @Autowired
    private CentroEducativoRepository centroRepository;

    /**
     * Obtiene todos los dispositivos ESP32 registrados.
     *
     * @return lista de todos los dispositivos
     */
    @GetMapping
    public List<DispositivoEsp32> obtenerTodosLosDispositivos() {
        return dispositivoRepository.findAll();
    }

    /**
     * Obtiene un dispositivo ESP32 por su ID.
     *
     * @param id identificador del dispositivo
     * @return el dispositivo encontrado
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @GetMapping("/{id}")
    public DispositivoEsp32 obtenerDispositivoPorId(@PathVariable("id") Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));
    }

    /**
     * Obtiene todos los dispositivos ESP32 activos.
     *
     * @return lista de dispositivos con estado activo = true
     */
    @GetMapping("/activos")
    public List<DispositivoEsp32> obtenerDispositivosActivos() {
        return dispositivoRepository.findByActivo(true);
    }

    /**
     * Obtiene todos los dispositivos ESP32 de un centro educativo.
     *
     * @param centroId identificador del centro educativo
     * @return lista de dispositivos del centro
     * @throws ResponseStatusException si no se encuentra el centro (404)
     */
    @GetMapping("/centro/{centroId}")
    public List<DispositivoEsp32> obtenerDispositivosPorCentro(@PathVariable("centroId") Long centroId) {
        if (!centroRepository.existsById(centroId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Centro educativo no encontrado");
        }
        return dispositivoRepository.findByCentroEducativo_Id(centroId);
    }

    /**
     * Registra un nuevo dispositivo ESP32.
     * <p>
     * Valida que no exista otro dispositivo con la misma dirección MAC.
     * </p>
     *
     * @param dispositivo dispositivo a registrar (validado con @Valid)
     * @return el dispositivo registrado
     * @throws ResponseStatusException si ya existe un dispositivo con esa MAC (409)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DispositivoEsp32 crearDispositivo(@Valid @RequestBody DispositivoEsp32 dispositivo) {
        if (dispositivoRepository.existsByMacAddress(dispositivo.getMacAddress())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un dispositivo con esa dirección MAC");
        }
        return dispositivoRepository.save(dispositivo);
    }

    /**
     * Actualiza un dispositivo ESP32 existente.
     * <p>
     * Valida que la nueva dirección MAC no esté ya registrada en otro dispositivo.
     * La relación con el centro se gestiona mediante el campo centroEducativo del cuerpo de la petición.
     * </p>
     *
     * @param id      identificador del dispositivo a actualizar
     * @param detalles datos actualizados del dispositivo (validado con @Valid)
     * @return el dispositivo actualizado
     * @throws ResponseStatusException si no se encuentra el dispositivo (404) o si la MAC ya existe (409)
     */
    @PutMapping("/{id}")
    public DispositivoEsp32 actualizarDispositivo(
            @PathVariable("id") Long id,
            @Valid @RequestBody DispositivoEsp32 detalles) {

        DispositivoEsp32 dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));

        if (!dispositivo.getMacAddress().equals(detalles.getMacAddress())
                && dispositivoRepository.existsByMacAddress(detalles.getMacAddress())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un dispositivo con esa dirección MAC");
        }

        dispositivo.setMacAddress(detalles.getMacAddress());
        dispositivo.setActivo(detalles.isActivo());
        dispositivo.setUltimaConexion(detalles.getUltimaConexion());
        dispositivo.setFrecuenciaLecturaSeg(detalles.getFrecuenciaLecturaSeg());
        dispositivo.setUmbralTempMin(detalles.getUmbralTempMin());
        dispositivo.setUmbralTempMax(detalles.getUmbralTempMax());
        dispositivo.setUmbralHumedadAmbienteMin(detalles.getUmbralHumedadAmbienteMin());
        dispositivo.setUmbralHumedadAmbienteMax(detalles.getUmbralHumedadAmbienteMax());
        dispositivo.setUmbralHumedadSueloMin(detalles.getUmbralHumedadSueloMin());
        dispositivo.setUmbralCO2Max(detalles.getUmbralCO2Max());

        return dispositivoRepository.save(dispositivo);
    }

    /**
     * Actualiza parcialmente los umbrales de alerta de un dispositivo.
     * <p>
     * Solo actualiza los umbrales que estén presentes (no nulos) en el cuerpo de la petición.
     * </p>
     *
     * @param id       identificador del dispositivo
     * @param umbrales objeto con los umbrales a actualizar (los campos null se ignoran)
     * @return el dispositivo con los umbrales actualizados
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @PatchMapping("/{id}/umbrales")
    public DispositivoEsp32 actualizarUmbrales(@PathVariable("id") Long id,
                                                @RequestBody DispositivoEsp32 umbrales) {
        DispositivoEsp32 dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));

        if (umbrales.getUmbralTempMin() != null) {
            dispositivo.setUmbralTempMin(umbrales.getUmbralTempMin());
        }
        if (umbrales.getUmbralTempMax() != null) {
            dispositivo.setUmbralTempMax(umbrales.getUmbralTempMax());
        }
        if (umbrales.getUmbralHumedadAmbienteMin() != null) {
            dispositivo.setUmbralHumedadAmbienteMin(umbrales.getUmbralHumedadAmbienteMin());
        }
        if (umbrales.getUmbralHumedadAmbienteMax() != null) {
            dispositivo.setUmbralHumedadAmbienteMax(umbrales.getUmbralHumedadAmbienteMax());
        }
        if (umbrales.getUmbralHumedadSueloMin() != null) {
            dispositivo.setUmbralHumedadSueloMin(umbrales.getUmbralHumedadSueloMin());
        }
        if (umbrales.getUmbralCO2Max() != null) {
            dispositivo.setUmbralCO2Max(umbrales.getUmbralCO2Max());
        }

        return dispositivoRepository.save(dispositivo);
    }

    /**
     * Elimina un dispositivo ESP32 por su ID.
     * <p>
     * IMPORTANTE: Al eliminar un dispositivo, se eliminan en cascada todas sus
     * lecturas y alertas asociadas (ON DELETE CASCADE).
     * </p>
     *
     * @param id identificador del dispositivo a eliminar
     * @return el dispositivo eliminado
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @DeleteMapping("/{id}")
    public DispositivoEsp32 eliminarDispositivo(@PathVariable("id") Long id) {
        DispositivoEsp32 dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));
        dispositivoRepository.deleteById(id);
        return dispositivo;
    }
}
