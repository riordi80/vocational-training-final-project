package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.dto.LecturaRequest;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Lectura;
import com.example.gardenmonitor.repository.DispositivoEsp32Repository;
import com.example.gardenmonitor.repository.LecturaRepository;
import com.example.gardenmonitor.dto.LecturaMuestraProjection;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar lecturas de sensores IoT.
 * <p>
 * Proporciona endpoints para recibir lecturas enviadas por dispositivos ESP32
 * y para consultar el histórico de lecturas de un dispositivo. Soporta paginación
 * server-side y stride sampling para optimizar la carga de datos en gráficas.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/lecturas")
public class LecturaController {

    @Autowired
    private LecturaRepository lecturaRepository;

    @Autowired
    private DispositivoEsp32Repository dispositivoEsp32Repository;

    /**
     * Recibe una lectura enviada por un dispositivo ESP32.
     * <p>
     * Flujo:
     * </p>
     * <ol>
     *   <li>Busca el dispositivo por dirección MAC</li>
     *   <li>Verifica que el dispositivo tiene un centro asignado</li>
     *   <li>Crea la lectura con timestamp actual</li>
     *   <li>Actualiza la fecha de última conexión del dispositivo</li>
     * </ol>
     *
     * @param request datos de la lectura enviada por el ESP32 (validado con @Valid)
     * @return la lectura registrada
     * @throws ResponseStatusException si el dispositivo no está registrado (404)
     *                                 o no tiene centro asignado (422)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lectura recibirLectura(@Valid @RequestBody LecturaRequest request) {
        // 1. Buscar dispositivo por MAC
        DispositivoEsp32 dispositivo = dispositivoEsp32Repository
                .findByMacAddress(request.getMacAddress())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Dispositivo no registrado: " + request.getMacAddress()));

        // 2. Verificar que tiene centro asignado
        if (dispositivo.getCentroEducativo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Dispositivo sin centro asignado: " + request.getMacAddress());
        }

        // 3. Crear lectura
        Lectura lectura = new Lectura();
        lectura.setTimestamp(LocalDateTime.now());
        lectura.setDispositivo(dispositivo);
        lectura.setTemperatura(request.getTemperatura());
        lectura.setHumedadAmbiente(request.getHumedadAmbiente());
        lectura.setHumedadSuelo(request.getHumedadSuelo());
        lectura.setCo2(request.getCo2());
        lectura.setLuz1(request.getLuz1());
        lectura.setLuz2(request.getLuz2());

        // 4. Actualizar última conexión del dispositivo
        dispositivo.setUltimaConexion(LocalDateTime.now());
        dispositivoEsp32Repository.save(dispositivo);

        return lecturaRepository.save(lectura);
    }

    /**
     * Obtiene las lecturas de un dispositivo paginadas, ordenadas por timestamp descendente.
     *
     * @param dispositivoId identificador del dispositivo
     * @param page          número de página (0-indexed, por defecto 0)
     * @param size          tamaño de página (por defecto 20)
     * @return página de lecturas del dispositivo
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @GetMapping("/dispositivo/{dispositivoId}")
    public Page<Lectura> obtenerLecturasPorDispositivo(
            @PathVariable("dispositivoId") Long dispositivoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        DispositivoEsp32 dispositivo = dispositivoEsp32Repository.findById(dispositivoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));
        return lecturaRepository.findByDispositivoOrderByTimestampDesc(
                dispositivo, PageRequest.of(page, size));
    }

    /**
     * Obtiene las lecturas de un dispositivo en un rango de fechas, paginadas.
     *
     * @param dispositivoId identificador del dispositivo
     * @param desde         inicio del rango temporal (ISO 8601)
     * @param hasta         fin del rango temporal (ISO 8601)
     * @param page          número de página (0-indexed, por defecto 0)
     * @param size          tamaño de página (por defecto 20)
     * @return página de lecturas del dispositivo en el rango indicado
     * @throws ResponseStatusException si no se encuentra el dispositivo (404)
     */
    @GetMapping("/dispositivo/{dispositivoId}/rango")
    public Page<Lectura> obtenerLecturasPorRango(
            @PathVariable("dispositivoId") Long dispositivoId,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        DispositivoEsp32 dispositivo = dispositivoEsp32Repository.findById(dispositivoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));
        return lecturaRepository.findByDispositivoAndTimestampBetweenOrderByTimestampDesc(
                dispositivo, desde, hasta, PageRequest.of(page, size));
    }

    /**
     * Devuelve lecturas reales muestreadas (stride sampling) para la gráfica del frontend.
     * <p>
     * Garantiza siempre ≤ ~400 puntos independientemente del volumen de datos,
     * preservando los picos y valles reales (a diferencia de promedios con time_bucket).
     * </p>
     * <p>
     * Periodos aceptados:
     * </p>
     * <ul>
     *   <li>DIA — últimas 24 horas</li>
     *   <li>SEMANA — últimos 7 días</li>
     *   <li>MES — últimos 30 días</li>
     *   <li>SEMESTRE — últimos 180 días</li>
     *   <li>ANIO — últimos 365 días</li>
     * </ul>
     *
     * @param dispositivoId identificador del dispositivo
     * @param periodo       período de tiempo a consultar
     * @return lista de lecturas muestreadas para la gráfica
     * @throws ResponseStatusException si no se encuentra el dispositivo (404) o el período es inválido (400)
     */
    @GetMapping("/dispositivo/{dispositivoId}/grafica")
    public List<LecturaMuestraProjection> obtenerLecturasParaGrafica(
            @PathVariable("dispositivoId") Long dispositivoId,
            @RequestParam("periodo") String periodo) {

        DispositivoEsp32 dispositivo = dispositivoEsp32Repository.findById(dispositivoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));

        LocalDateTime hasta = LocalDateTime.now();
        LocalDateTime desde = switch (periodo.toUpperCase()) {
            case "DIA"      -> hasta.minusDays(1);
            case "SEMANA"   -> hasta.minusDays(7);
            case "MES"      -> hasta.minusDays(30);
            case "SEMESTRE" -> hasta.minusDays(180);
            case "ANIO"     -> hasta.minusDays(365);
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Periodo inválido: " + periodo +
                    ". Valores aceptados: DIA, SEMANA, MES, SEMESTRE, ANIO");
        };

        return lecturaRepository.findMuestraByDispositivoAndRango(dispositivo.getId(), desde, hasta);
    }

    /**
     * Elimina una lectura por su identificador.
     * <p>
     * Permite borrar lecturas erróneas o corruptas registradas por un dispositivo.
     * </p>
     *
     * @param id identificador de la lectura a eliminar
     * @return la lectura eliminada
     * @throws ResponseStatusException si no se encuentra la lectura (404)
     */
    @DeleteMapping("/{id}")
    public Lectura eliminarLectura(@PathVariable("id") Long id) {
        Lectura lectura = lecturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lectura no encontrada"));
        lecturaRepository.deleteById(id);
        return lectura;
    }
}
