package com.example.gardenmonitor.controller;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.CentroEducativo;
import com.example.gardenmonitor.repository.CentroEducativoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador REST para gestionar centros educativos.
 * <p>
 * Proporciona endpoints para operaciones CRUD de centros educativos
 * y para consultar la relación 1:N con árboles.
 * </p>
 *
 * @author Richard Ortiz y Enrique Pérez
 * @version 1.0
 */
@RestController
@RequestMapping("/api/centros")
public class CentroEducativoController {

    @Autowired
    private CentroEducativoRepository centroEducativoRepository;

    /**
     * Obtiene todos los centros educativos.
     *
     * @return lista de todos los centros educativos
     */
    @GetMapping
    public List<CentroEducativo> obtenerTodosLosCentros() {
        return centroEducativoRepository.findAll();
    }

    /**
     * Obtiene un centro educativo por su ID.
     *
     * @param id identificador del centro educativo
     * @return el centro educativo encontrado
     * @throws ResponseStatusException si no se encuentra el centro (404)
     */
    @GetMapping("/{id}")
    public CentroEducativo obtenerCentroPorId(@PathVariable("id") Long id) {
        return centroEducativoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));
    }

    /**
     * Crea un nuevo centro educativo.
     * <p>
     * Valida que no exista otro centro con el mismo nombre.
     * </p>
     *
     * @param centro centro educativo a crear (validado con @Valid)
     * @return el centro educativo creado
     * @throws ResponseStatusException si ya existe un centro con ese nombre (409)
     */
    @PostMapping
    public CentroEducativo crearCentro(@Valid @RequestBody CentroEducativo centro) {
        if (centroEducativoRepository.existsByNombre(centro.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un centro educativo con ese nombre");
        }
        return centroEducativoRepository.save(centro);
    }

    /**
     * Actualiza un centro educativo existente.
     * <p>
     * Valida que no exista otro centro con el nuevo nombre (si se cambia el nombre).
     * </p>
     *
     * @param id            identificador del centro a actualizar
     * @param detallesCentro datos actualizados del centro (validado con @Valid)
     * @return el centro educativo actualizado
     * @throws ResponseStatusException si no se encuentra el centro (404) o si el nombre ya existe (409)
     */
    @PutMapping("/{id}")
    public CentroEducativo actualizarCentro(@PathVariable("id") Long id,
                                             @Valid @RequestBody CentroEducativo detallesCentro) {
        CentroEducativo centro = centroEducativoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));

        // Verificar si el nuevo nombre ya existe (y no es del mismo centro)
        if (!centro.getNombre().equals(detallesCentro.getNombre())
                && centroEducativoRepository.existsByNombre(detallesCentro.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un centro educativo con ese nombre");
        }

        centro.setNombre(detallesCentro.getNombre());
        centro.setDireccion(detallesCentro.getDireccion());
        centro.setLatitud(detallesCentro.getLatitud());
        centro.setLongitud(detallesCentro.getLongitud());
        centro.setResponsable(detallesCentro.getResponsable());
        centro.setIsla(detallesCentro.getIsla());
        centro.setPoblacion(detallesCentro.getPoblacion());
        centro.setProvincia(detallesCentro.getProvincia());
        centro.setCodigoPostal(detallesCentro.getCodigoPostal());
        centro.setTelefono(detallesCentro.getTelefono());
        centro.setEmail(detallesCentro.getEmail());

        return centroEducativoRepository.save(centro);
    }

    /**
     * Elimina un centro educativo por su ID.
     * <p>
     * IMPORTANTE: Al eliminar un centro, se eliminan también todos sus árboles
     * debido a la configuración cascade = CascadeType.ALL en la relación.
     * </p>
     *
     * @param id identificador del centro a eliminar
     * @return el centro educativo eliminado
     * @throws ResponseStatusException si no se encuentra el centro (404)
     */
    @DeleteMapping("/{id}")
    public CentroEducativo eliminarCentro(@PathVariable("id") Long id) {
        CentroEducativo centro = centroEducativoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));
        centroEducativoRepository.deleteById(id);
        return centro;
    }

    /**
     * Obtiene todos los árboles de un centro educativo específico.
     * <p>
     * Este endpoint demuestra la relación 1:N entre CentroEducativo y Arbol.
     * Un centro educativo puede tener múltiples árboles asociados.
     * </p>
     *
     * @param id identificador del centro educativo
     * @return lista de árboles del centro
     * @throws ResponseStatusException si no se encuentra el centro (404)
     */
    @GetMapping("/{id}/arboles")
    public List<Arbol> obtenerArbolesPorCentro(@PathVariable("id") Long id) {
        CentroEducativo centro = centroEducativoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Centro educativo no encontrado"));
        return centro.getArboles();
    }
}
