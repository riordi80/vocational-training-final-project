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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/arboles")
public class ArbolController {

    @Autowired
    private ArbolRepository arbolRepository;

    @GetMapping
    public List<Arbol> obtenerTodosLosArboles() {
        return arbolRepository.findAll();
    }

    @GetMapping("/{id}")
    public Arbol obtenerArbolPorId(@PathVariable("id") Long id) {
        return arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
    }

    @GetMapping("/especie/{especie}")
    public List<Arbol> obtenerArbolesPorEspecie(@PathVariable("especie") String especie) {
        return arbolRepository.findByEspecie(especie);
    }

    @GetMapping("/centro/{centroId}")
    public List<Arbol> obtenerArbolesPorCentro(@PathVariable("centroId") Long centroId) {
        CentroEducativo centro = new CentroEducativo();
        centro.setId(centroId);
        return arbolRepository.findByCentroEducativo(centro);
    }

    @GetMapping("/dispositivo/{dispositivoId}")
    public Arbol obtenerArbolPorDispositivo(@PathVariable("dispositivoId") Long dispositivoId) {
        DispositivoEsp32 dispositivo = new DispositivoEsp32();
        dispositivo.setId(dispositivoId);
        return arbolRepository.findByDispositivoEsp32(dispositivo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado para este dispositivo"));
    }

    @GetMapping("/buscar/{nombre}")
    public List<Arbol> buscarArbolesPorNombre(@PathVariable("nombre") String nombre) {
        return arbolRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @GetMapping("/ordenados")
    public List<Arbol> obtenerArbolesOrdenados() {
        return arbolRepository.findAllByOrderByNombreAsc();
    }

    @PostMapping
    public Arbol crearArbol(@Valid @RequestBody Arbol arbol) {
        if (arbolRepository.existsByNombreAndCentroEducativo(
                arbol.getNombre(), arbol.getCentroEducativo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un árbol con ese nombre en este centro");
        }
        return arbolRepository.save(arbol);
    }

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

        return arbolRepository.save(arbol);
    }

    @DeleteMapping("/{id}")
    public Arbol eliminarArbol(@PathVariable("id") Long id) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        arbolRepository.deleteById(id);
        return arbol;
    }

    @PatchMapping("/{id}/dispositivo")
    public Arbol asignarDispositivo(@PathVariable("id") Long id, 
                                     @RequestBody DispositivoEsp32 dispositivo) {
        Arbol arbol = arbolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Árbol no encontrado"));
        arbol.setDispositivoEsp32(dispositivo);
        return arbolRepository.save(arbol);
    }

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