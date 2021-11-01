package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.Area;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

  Optional<Area> findById(Long id);

  List<Area> findAllByCompanyId(Long id);

}
