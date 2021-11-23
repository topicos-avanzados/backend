package com.topicos.backend.persistence.repository;

import com.topicos.backend.persistence.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByMail(String mail);

  List<User> findAllByCompanyId_Id(Long companyId);
}