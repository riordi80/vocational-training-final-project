package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.CentroEducativo;
import com.example.gardenmonitor.model.DispositivoEsp32;
import com.example.gardenmonitor.repository.ArbolRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para gestionar árboles monitorizados.
 * <p>
 * Proporciona endpoints para operaciones CRUD de árboles, así como
 * consultas filtradas por centro, especie y nombre.
 * También incluye endpoints PATCH para asignar dispositivos ESP32
 * y actualizar umbrales de forma independiente.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/arboles")
public class ArbolController {

    @Autowired
    private ArbolRepository arbolRepository;

    /**
     * Obtiene todos los árboles registrados.
     *
     * @return lista de todos los árboles
     */
    @GetMapping
    public List<Arbol> obtenerTodosLosArboles() {
        return arbolRepository.findAll();
    }

    /**
     * Obtiene un árbol por su ID.
     *
     * @param id identificador del árbol
     * @return el árbol encontrado
     * @throws ResponseStatusException si no se encuentra el árbol (404)
     */
    @GetMapping("/{id}")
    public Arbol obtenerArbolPorId(@PathVariable("id") Long id) {
        return arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
    }

    /**
     * Obtiene todos los árboles de una especie determinada.
     *
     * @param especie nombre de la especie a filtrar
     * @return lista de árboles de la especie indicada
     */
    @GetMapping("/especie/{especie}")
    public List<Arbol> obtenerArbolesPorEspecie(@PathVariable("especie") String especie) {
        return arbolRepository.findByEspecie(especie);
    }

    /**
     * Obtiene todos los árboles pertenecientes a un centro educativo.
     *
     * @param centroId identificador del centro educativo
     * @return lista de árboles del centro
     */
    @GetMapping("/centro/{centroId}")
    public List<Arbol> obtenerArbolesPorCentro(@PathVariable("centroId") Long centroId) {
        CentroEducativo centro = new CentroEducativo();
        centro.setId(centroId);
        return arbolRepository.findByCentroEducativo(centro);
    }

    /**
     * Obtiene el árbol asociado a un dispositivo ESP32 específico.
     *
     * @param dispositivoId identificador del dispositivo ESP32
     * @return el árbol asociado al dispositivo
     * @throws ResponseStatusException si no se encuentra ningún árbol para ese dispositivo (404)
     */
    @GetMapping("/dispositivo/{dispositivoId}")
    public Arbol obtenerArbolPorDispositivo(@PathVariable("dispositivoId") Long dispositivoId) {
        DispositivoEsp32 dispositivo = new DispositivoEsp32();
        dispositivo.setId(dispositivoId);
        return arbolRepository.findByDispositivoEsp32(dispositivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado para este dispositivo"));
    }

    /**
     * Busca árboles cuyo nombre contenga el texto indicado (insensible a mayúsculas).
     *
     * @param nombre texto a buscar en el nombre del árbol
     * @return lista de árboles cuyo nombre contiene el texto
     */
    @GetMapping("/buscar/{nombre}")
    public List<Arbol> buscarArbolesPorNombre(@PathVariable("nombre") String nombre) {
        return arbolRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene todos los árboles ordenados alfabéticamente por nombre.
     *
     * @return lista de árboles ordenada por nombre ascendente
     */
    @GetMapping("/ordenados")
    public List<Arbol> obtenerArbolesOrdenados() {
        return arbolRepository.findAllByOrderByNombreAsc();
    }

    /**
     * Crea un nuevo árbol.
     * <p>
     * Valida que no exista otro árbol con el mismo nombre en el mismo centro educativo.
     * </p>
     *
     * @param arbol datos del árbol a crear (validado con @Valid)
     * @return el árbol creado
     * @throws ResponseStatusException si ya existe un árbol con ese nombre en el centro (409)
     */
    @PostMapping
    public Arbol crearArbol(@Valid @RequestBody Arbol arbol) {
        if (arbolRepository.existsByNombreAndCentroEducativo(
                arbol.getNombre(), arbol.getCentroEducativo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un árbol con ese nombre en este centro");
        }
        return arbolRepository.save(arbol);
    }

    /**
     * Actualiza un árbol existente.
     * <p>
     * Valida que el nuevo nombre no esté ya en uso en el mismo centro (si se cambia el nombre).
     * </p>
     *
     * @param id            identificador del árbol a actualizar
     * @param detallesArbol datos actualizados del árbol (validado con @Valid)
     * @return el árbol actualizado
     * @throws ResponseStatusException si no se encuentra el árbol (404) o si el nombre ya existe en el centro (409)
     */
    @PutMapping("/{id}")
    public Arbol actualizarArbol(@PathVariable("id") Long id,
                                  @Valid @RequestBody Arbol detallesArbol) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));

        // Verificar si el nuevo nombre ya existe en el centro (y no es del mismo árbol)
        if (!arbol.getNombre().equals(detallesArbol.getNombre())
                && arbolRepository.existsByNombreAndCentroEducativo(
                        detallesArbol.getNombre(), detallesArbol.getCentroEducativo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un árbol con ese nombre en este centro");
        }

        arbol.setCentroEducativo(detallesArbol.getCentroEducativo());
        arbol.setNombre(detallesArbol.getNombre());
        arbol.setEspecie(detallesArbol.getEspecie());
        arbol.setFechaPlantacion(detallesArbol.getFechaPlantacion());
        arbol.setUbicacionEspecifica(detallesArbol.getUbicacionEspecifica());
        arbol.setDispositivoEsp32(detallesArbol.getDispositivoEsp32());
        arbol.setUmbralTempMin(detallesArbol.getUmbralTempMin());
        arbol.setUmbralTempMax(detallesArbol.getUmbralTempMax());
        arbol.setUmbralHumedadAmbienteMin(detallesArbol.getUmbralHumedadAmbienteMin());
        arbol.setUmbralHumedadAmbienteMax(detallesArbol.getUmbralHumedadAmbienteMax());
        arbol.setUmbralHumedadSueloMin(detallesArbol.getUmbralHumedadSueloMin());
        arbol.setUmbralCO2Max(detallesArbol.getUmbralCO2Max());
        arbol.setAbsorcionCo2Anual(detallesArbol.getAbsorcionCo2Anual());

        return arbolRepository.save(arbol);
    }

    /**
     * Elimina un árbol por su ID.
     * <p>
     * IMPORTANTE: Al eliminar un árbol, se eliminan en cascada sus lecturas y alertas asociadas.
     * </p>
     *
     * @param id identificador del árbol a eliminar
     * @return el árbol eliminado
     * @throws ResponseStatusException si no se encuentra el árbol (404)
     */
    @DeleteMapping("/{id}")
    public Arbol eliminarArbol(@PathVariable("id") Long id) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        arbolRepository.deleteById(id);
        return arbol;
    }

    /**
     * Asigna un dispositivo ESP32 a un árbol.
     * <p>
     * Este endpoint gestiona la relación 1:1 entre árbol y dispositivo.
     * Para desasignar el dispositivo, enviar {@code {"id": null}}.
     * </p>
     *
     * @param id          identificador del árbol
     * @param dispositivo dispositivo a asignar (puede contener solo el id)
     * @return el árbol con el dispositivo actualizado
     * @throws ResponseStatusException si no se encuentra el árbol (404)
     */
    @PatchMapping("/{id}/dispositivo")
    public Arbol asignarDispositivo(@PathVariable("id") Long id,
                                     @RequestBody DispositivoEsp32 dispositivo) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        arbol.setDispositivoEsp32(dispositivo);
        return arbolRepository.save(arbol);
    }

    /**
     * Actualiza parcialmente los umbrales de alerta de un árbol.
     * <p>
     * Solo actualiza los umbrales que estén presentes (no nulos) en el cuerpo de la petición.
     * </p>
     *
     * @param id       identificador del árbol
     * @param umbrales objeto con los umbrales a actualizar (los campos null se ignoran)
     * @return el árbol con los umbrales actualizados
     * @throws ResponseStatusException si no se encuentra el árbol (404)
     */
    @PatchMapping("/{id}/umbrales")
    public Arbol actualizarUmbrales(@PathVariable("id") Long id,
                                     @RequestBody Arbol umbrales) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));

        if (umbrales.getUmbralTempMin() != null) {
            arbol.setUmbralTempMin(umbrales.getUmbralTempMin());
        }
        if (umbrales.getUmbralTempMax() != null) {
            arbol.setUmbralTempMax(umbrales.getUmbralTempMax());
        }
        if (umbrales.getUmbralHumedadAmbienteMin() != null) {
            arbol.setUmbralHumedadAmbienteMin(umbrales.getUmbralHumedadAmbienteMin());
        }
        if (umbrales.getUmbralHumedadAmbienteMax() != null) {
            arbol.setUmbralHumedadAmbienteMax(umbrales.getUmbralHumedadAmbienteMax());
        }
        if (umbrales.getUmbralHumedadSueloMin() != null) {
            arbol.setUmbralHumedadSueloMin(umbrales.getUmbralHumedadSueloMin());
        }
        if (umbrales.getUmbralCO2Max() != null) {
            arbol.setUmbralCO2Max(umbrales.getUmbralCO2Max());
        }

        return arbolRepository.save(arbol);
    }
}
