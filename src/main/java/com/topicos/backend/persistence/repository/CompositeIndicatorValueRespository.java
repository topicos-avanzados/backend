package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.IndicatorValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompositeIndicatorValueRespository extends JpaRepository<IndicatorValue, Long> {
    
    Optional<IndicatorValue> findById(Long id);
    
}
