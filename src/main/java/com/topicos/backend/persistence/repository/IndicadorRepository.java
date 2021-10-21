package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.Indicador;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, Long> {

  Optional<Indicador> findByIndicadorId(Long id);

}
