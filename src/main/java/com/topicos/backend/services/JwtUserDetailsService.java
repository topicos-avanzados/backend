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
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new ApiException(user
          .get()
          .getUsername(), "Username not exists", HttpStatus.NOT_FOUND.value());
    }
    return new org.springframework.security.core.userdetails.User(user
        .get()
        .getUsername(), user
        .get()
        .getPassword(), new ArrayList<>());
  }

  public UserDetails saveUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      throw new ApiException(user
          .get()
          .getUsername(), "Username not exists", HttpStatus.NOT_FOUND.value());
    }
    return new org.springframework.security.core.userdetails.User(user
        .get()
        .getUsername(), user
        .get()
        .getPassword(), new ArrayList<>());
  }

  public boolean autorizado(String username, Map<String, String> params) throws Exception {
    Optional<User> user = userRepository.findByUsername(username);

    if (!user
        .get()
        .getAdmin()) {
      throw new ApiException(user
          .get()
          .getUsername(), "Username unauthorized", HttpStatus.UNAUTHORIZED.value());
    }
    return true;
  }

  @Transactional
  public void save(UserDTO userRequestDTO) {
    User user = userRepository
        .findByUsername(userRequestDTO.getUsername())
        .orElse(null);
    if (user == null) {
      User newUser = User
          .builder()
          .admin(false)
          .username(userRequestDTO.getUsername())
          .mail(userRequestDTO.getMail())
          .build();

      userRepository.save(newUser);
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

}
//  private final UserRepository userRepository;
//
//  private final CompanyRepository companyRepository;
//
//  private final PasswordEncoder bcryptEncoder;
//
//  private final JwtTokenUtil jwtTokenUtil;
//
//  private final AuthenticationManager authenticationManager;
//
//
//
//  @Transactional
//  public void update(UserCredentialDTO userRequestDTO) {
//    Optional<User> user = this.userRepository.findByUsername(userRequestDTO.getUsername());
//    user
//        .get()
//        .setPassword(this.bcryptEncoder.encode(userRequestDTO.getPassword()));
//
//    this.userRepository.save(user.get());
//  }
//
//  @Transactional
//  public String activate(UserCredentialDTO userCredential, String token) {
//    UserDetails details = this.loadUserByUsername(userCredential.getUsername());
//    if (this.jwtTokenUtil.validateToken(token, details)) {
//      this.update(userCredential);
////      Long company = this.jwtTokenUtil.getCompanyFromToken(token);
////      return this.jwtTokenUtil.generateToken(details, false, company);
//
//    }
//    throw new ApiException("Invalid token", "Invalid request, the token could be expired or invalid", 400);
//  }
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    Optional<User> user = this.userRepository.findByUsername(username);
//    if (user.isEmpty()) {
//      throw new ApiException(username, "Username not exists", HttpStatus.NOT_FOUND.value());
//    }
//    return new org.springframework.security.core.userdetails.User(user
//        .get()
//        .getUsername(), user
//        .get()
//        .getPassword(), new ArrayList<>());
//  }
//
//  public boolean isAuthorized(String username, Map<String, String> params) throws Exception {
//    Optional<User> user = this.userRepository.findByUsername(username);
//
//    if (!user
//        .get()
//        .getAdmin()) {
//      throw new ApiException(user
//          .get()
//          .getUsername(), "Username unauthorized", HttpStatus.UNAUTHORIZED.value());
//    }
//    return true;
//  }
//
//  public void deleteUser(Long areaId) {
//    Optional<User> area = this.userRepository.findById(areaId);
//    area.ifPresent(this.userRepository::delete);
//  }
//
//  public UserDTO modifyUser(UserDTO userDTO) {
//    Optional<User> optionalUser = this.userRepository.findByUsername(userDTO.getUsername());
//    if (optionalUser.isPresent()) {
//      User user = optionalUser.get();
//      user.setMail(userDTO.getMail());
//      this.userRepository.save(user);
//      return Mappers.buildUserDTO(user);
//    }
//    //FIXME NULL O QUE DEVUELVA OTRA COSA?
//    return null;
//  }
//
//  public boolean isAdmin(String username) {
//    Optional<User> optionalUser = this.userRepository.findByUsername(username);
//    if (optionalUser.isPresent()) {
//      return optionalUser
//          .get()
//          .getAdmin();
//    }
//    return false;
//  }
//
//  public String loginAndGenerateToken(UserCredentialDTO user) {
//
//    Authentication authentication =
//        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//    if (authentication.isAuthenticated()) {
//      UserDetails details = this.loadUserByUsername(user.getUsername());
//
//      Optional<User> optionalUser = this.userRepository.findByUsername(user.getUsername());
//
//      String token = jwtTokenUtil.generateToken(details, optionalUser
//          .get()
//          .getAdmin(), optionalUser
//          .get()
//          .getCompanyId()
//          .getId());
//    }
//    throw new ApiException("Invalid credentials", "Invalid credentials", 404);
//  }


