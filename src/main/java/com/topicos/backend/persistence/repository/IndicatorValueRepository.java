package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.IndicatorValue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorValueRepository extends JpaRepository<IndicatorValue, Long> {

  Optional<IndicatorValue> findByIndicatorId(Long id);

  List<IndicatorValue> findAllByIndicatorId(Long id);

}
