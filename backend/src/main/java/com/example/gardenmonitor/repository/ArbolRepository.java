package com.example.gardenmonitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gardenmonitor.model.Arbol;
import com.example.gardenmonitor.model.CentroEducativo;
import com.example.gardenmonitor.model.DispositivoEsp32;

@Repository
public interface ArbolRepository extends JpaRepository<Arbol, Long>{
    Optional<Arbol> findByEspecie(String especie);
    boolean existByEspecie(String especie);
    List<Arbol> findByCentro(CentroEducativo centroEducativo);
    Arbol findByDispositivoEsp32(DispositivoEsp32 dispositivoEsp32);

    
}
