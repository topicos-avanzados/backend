package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Optional<Log> findByEmail(String mail);

}
