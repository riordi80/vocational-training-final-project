package com.example.gardenmonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.CentroEducativo;

@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Long>{
    List<Arbol> findByEspecie(String especie);
    List<Arbol> findByCentroEducativo(CentroEducativo centroEducativo);
    List<Arbol> findByNombreContainingIgnoreCase(String nombre);
    List<Arbol> findAllByOrderByNombreAsc();
    boolean existsByNombreAndCentroEducativo(String nombre, CentroEducativo centroEducacion);
}
