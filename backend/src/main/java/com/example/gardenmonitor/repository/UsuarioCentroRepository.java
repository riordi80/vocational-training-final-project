package com.example.gardenmonitor.repository;

import com.example.gardenmonitor.model.UsuarioCentro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioCentroRepository extends JpaRepository<UsuarioCentro, Long> {

    List<UsuarioCentro> findByUsuarioId(Long usuarioId);

    List<UsuarioCentro> findByCentroEducativoId(Long centroId);

    Optional<UsuarioCentro> findByUsuarioIdAndCentroEducativoId(Long usuarioId, Long centroId);

    boolean existsByUsuarioIdAndCentroEducativoId(Long usuarioId, Long centroId);
}
