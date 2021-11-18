package com.topicos.backend.services;

import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.NewUserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.User;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.UserRepository;
import com.topicos.backend.utils.Mappers;
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
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  private CompanyRepository companyRepository;

  private PasswordEncoder bcryptEncoder;

  @Autowired
  public JwtUserDetailsService(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder bcryptEncoder) {
    this.userRepository = userRepository;
    this.companyRepository = companyRepository;
    this.bcryptEncoder = bcryptEncoder;
  }

  @Transactional
  public UserDTO saveAndSendMail(NewUserDTO userRequestDTO) {
    Optional<User> user = this.userRepository.findByUsername(userRequestDTO.getUsername());
    Optional<Company> company = this.companyRepository.findById(userRequestDTO.getCompanyId());
    if (user.isEmpty() && company.isPresent()) {
      User newUser = this.userRepository.save(User
          .builder()
          .admin(false)
          .username(userRequestDTO.getUsername())
          .password(bcryptEncoder.encode(userRequestDTO.getPassword()))
          .mail(userRequestDTO.getMail())
          .companyId(company.get())
          .build());

      //TODO SEND MAIL

      return Mappers.buildUserDTO(newUser);
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }

  @Transactional
  public void update(UserCredentialDTO userRequestDTO) {
    Optional<User> user = this.userRepository.findByUsername(userRequestDTO.getUsername());
    user
        .get()
        .setPassword(this.bcryptEncoder.encode(userRequestDTO.getPassword()));

    this.userRepository.save(user.get());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = this.userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new ApiException(username, "Username not exists", HttpStatus.NOT_FOUND.value());
    }
    return new org.springframework.security.core.userdetails.User(user
        .get()
        .getUsername(), user
        .get()
        .getPassword(), new ArrayList<>());
  }

  public boolean isAuthorized(String username, Map<String, String> params) throws Exception {
    Optional<User> user = this.userRepository.findByUsername(username);

    if (!user
        .get()
        .getAdmin()) {
      throw new ApiException(user
          .get()
          .getUsername(), "Username unauthorized", HttpStatus.UNAUTHORIZED.value());
    }
    return true;
  }

  public void deleteUser(Long areaId) {
    Optional<User> area = this.userRepository.findById(areaId);
    area.ifPresent(this.userRepository::delete);
  }

  public UserDTO modifyUser(UserDTO userDTO) {
    Optional<User> optionalUser = this.userRepository.findByUsername(userDTO.getUsername());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setMail(userDTO.getMail());
      this.userRepository.save(user);
      return Mappers.buildUserDTO(user);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }

  public boolean isAdmin(String username) {
    Optional<User> optionalUser = this.userRepository.findByUsername(username);
    if (optionalUser.isPresent()) {
      return optionalUser
          .get()
          .getAdmin();
    }
    return false;
  }

}