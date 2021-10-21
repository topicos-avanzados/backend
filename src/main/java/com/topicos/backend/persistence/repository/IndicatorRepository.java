package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.Indicator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {

  Optional<Indicator> findById(Long id);

}
