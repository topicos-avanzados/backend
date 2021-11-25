package com.topicos.backend.services;

import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.User;
import com.topicos.backend.persistence.repository.UserRepository;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component

public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder bcryptEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByMail(username);
    if (user.isEmpty()) {
      throw new ApiException(username, "Username not exists", HttpStatus.NOT_FOUND.value());
    }
    return new org.springframework.security.core.userdetails.User(user
        .get()
        .getMail(), user
        .get()
        .getPassword(), new ArrayList<>());
  }

  public UserDetails saveUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByMail(username);
    if (!user.isPresent()) {
      throw new ApiException(user
          .get()
          .getMail(), "Username not exists", HttpStatus.NOT_FOUND.value());
    }
    return new org.springframework.security.core.userdetails.User(user
        .get()
        .getMail(), user
        .get()
        .getPassword(), new ArrayList<>());
  }

  //TODO: eliminar si no se usa
  public boolean autorizado(String username, Map<String, String> params) throws Exception {
    Optional<User> user = userRepository.findByMail(username);

    if (!user
        .get()
        .getAdmin()) {
      throw new ApiException(user
          .get()
          .getMail(), "Username unauthorized", HttpStatus.UNAUTHORIZED.value());
    }
    return true;
  }

  //TODO: eliminar si no se usa
  @Transactional
  public void save(UserDTO userRequestDTO) {
    User user = userRepository
        .findByMail(userRequestDTO.getMail())
        .orElse(null);
    if (user == null) {
      User newUser = User
          .builder()
          .admin(false)
          .mail(userRequestDTO.getMail())
          .build();

      userRepository.save(newUser);
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }

  @Transactional
  public void update(UserCredentialDTO userRequestDTO) {
    Optional<User> user = this.userRepository.findByMail(userRequestDTO.getMail());
    user
        .get()
        .setPassword(this.bcryptEncoder.encode(userRequestDTO.getPassword()));

    this.userRepository.save(user.get());
  }

}