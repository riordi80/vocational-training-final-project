package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.dto.LecturaRequest;
import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.model.Lectura;
import com.example.gardenmonitor.repository.ArbolRepository;
import com.example.gardenmonitor.repository.DispositivoEsp32Repository;
import com.example.gardenmonitor.repository.LecturaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/lecturas")
public class LecturaController {

    @Autowired
    private LecturaRepository lecturaRepository;

    @Autowired
    private DispositivoEsp32Repository dispositivoEsp32Repository;

    @Autowired
    private ArbolRepository arbolRepository;

    /**
     * Recibe una lectura enviada por un dispositivo ESP32.
     * <p>
     * Flujo:
     * 1. Busca el dispositivo por MAC address
     * 2. Verifica que el dispositivo tiene un árbol asignado
     * 3. Crea la lectura con timestamp actual
     * 4. Actualiza la última conexión del dispositivo
     * </p>
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

        // 2. Verificar que tiene árbol asignado
        Arbol arbol = dispositivo.getArbol();
        if (arbol == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Dispositivo sin árbol asignado: " + request.getMacAddress());
        }

        // 3. Crear lectura
        Lectura lectura = new Lectura();
        lectura.setTimestamp(LocalDateTime.now());
        lectura.setArbol(arbol);
        lectura.setDispositivo(dispositivo);
        lectura.setTemperatura(request.getTemperatura());
        lectura.setHumedadAmbiente(request.getHumedadAmbiente());
        lectura.setHumedadSuelo(request.getHumedadSuelo());
        lectura.setCo2(request.getCo2());
        lectura.setDiametroTronco(request.getDiametroTronco());

        // 4. Actualizar última conexión del dispositivo
        dispositivo.setUltimaConexion(LocalDateTime.now());
        dispositivoEsp32Repository.save(dispositivo);

        return lecturaRepository.save(lectura);
    }

    @GetMapping("/arbol/{arbolId}")
    public Page<Lectura> obtenerLecturasPorArbol(
            @PathVariable("arbolId") Long arbolId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Arbol arbol = arbolRepository.findById(arbolId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        return lecturaRepository.findByArbolOrderByTimestampDesc(
                arbol, PageRequest.of(page, size));
    }

    @GetMapping("/arbol/{arbolId}/rango")
    public Page<Lectura> obtenerLecturasPorRango(
            @PathVariable("arbolId") Long arbolId,
            @RequestParam("desde") LocalDateTime desde,
            @RequestParam("hasta") LocalDateTime hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Arbol arbol = arbolRepository.findById(arbolId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        return lecturaRepository.findByArbolAndTimestampBetweenOrderByTimestampDesc(
                arbol, desde, hasta, PageRequest.of(page, size));
    }
}
