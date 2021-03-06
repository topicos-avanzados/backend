package com.topicos.backend.services;

import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.ChangePasswordDTO;
import com.topicos.backend.dto.request.NewUserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.dto.response.UserLoginDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.User;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.UserRepository;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.utils.Mappers;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private static final String GENERIC_PASSWORD = "generic";

  private final CompanyRepository companyRepository;

  private final UserRepository userRepository;

  private final JwtTokenUtil jwtTokenUtil;

  private final JwtUserDetailsService jwtUserDetailsService;

  private final LogService logService;

  private final AuthenticationManager authenticationManager;

  private final PasswordEncoder bcryptEncoder;

  private final MailService mailService;


  public List<UserDTO> getAllUsers() {
    return this.userRepository
        .findAll()
        .stream()
        .map(Mappers::buildUserDTO)
        .collect(Collectors.toList());
  }

  @SneakyThrows
  public UserDTO saveAndSendMail(NewUserDTO userRequestDTO, String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      Optional<User> user = this.userRepository.findByMail(userRequestDTO.getMail());
      Optional<Company> company = this.companyRepository.findById(userRequestDTO.getCompanyId());
      if (user.isEmpty() && company.isPresent()) {
        User newUser = this.userRepository.save(User
            .builder()
            .admin(false)
            .mail(userRequestDTO.getMail())
            .companyId(company.get())
            .password(bcryptEncoder.encode(GENERIC_PASSWORD))
            .active(false)
            .build());

        UserDetails details = this.jwtUserDetailsService.saveUserByUsername(userRequestDTO.getMail());

        LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se registro un nuevo usuario: " + userRequestDTO.getMail());
        logService.addLog(newLog);

        String newToken = this.jwtTokenUtil.generateMailToken(details, false, newUser
            .getCompanyId()
            .getId());
        this.mailService.sendMailWithToken(newUser.getMail(), newToken);

        return Mappers.buildUserDTO(newUser);
      }
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }

  @Transactional
  public UserLoginDTO activate(UserCredentialDTO userCredential, String token) {
    UserDetails details = this.jwtUserDetailsService.loadUserByUsername(userCredential.getMail());
    if (this.jwtTokenUtil.validateToken(token, details)) {
      this.jwtUserDetailsService.update(userCredential);
      Long companyId = this.jwtTokenUtil.getCompanyFromToken(token);
      Company company = this.companyRepository
          .findById(companyId)
          .get();
      String newToken = this.jwtTokenUtil.generateToken(details, false, companyId);
      User user = this.userRepository
          .findByMail(userCredential.getMail())
          .get();
      user.setActive(true);
      this.userRepository.save(user);

      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se activo el usuario: " + user.getMail());
      logService.addLog(newLog);

      return new UserLoginDTO(newToken, Mappers.buildCompanyDTO(company));

    }
    throw new ApiException("Invalid token", "Invalid request, the token could be expired or invalid", 400);
  }

  public UserLoginDTO loginAndGenerateToken(UserCredentialDTO user) {

    Authentication authentication =
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword()));

    if (authentication.isAuthenticated()) {
      UserDetails details = this.jwtUserDetailsService.loadUserByUsername(user.getMail());

      Optional<User> optionalUser = this.userRepository.findByMail(user.getMail());

      if (optionalUser.isPresent() && optionalUser
          .get()
          .getActive()) {
        String token = this.jwtTokenUtil.generateToken(details, optionalUser
            .get()
            .getAdmin(), optionalUser
            .get()
            .getCompanyId()
            .getId());

        LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se ingreso con el usuario: " + optionalUser.get().getMail());
        logService.addLog(newLog);

        return new UserLoginDTO(token, Mappers.buildCompanyDTO(optionalUser
            .get()
            .getCompanyId()));
      }

    }
    throw new ApiException("Invalid credentials", "Invalid credentials", 404);
  }

  public void deleteUser(Long id, String token) {
    Optional<User> user = this.userRepository.findById(id);
    if (user.isPresent()) {
      String name = user
          .get()
          .getMail();
      this.userRepository.delete(user.get());
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token), "Se elimino el user: " + name);
      logService.addLog(newLog);
    }
  }

  public void changePassword(ChangePasswordDTO changePassword, String token) {
    String username = this.jwtTokenUtil.getUsernameFromToken(token);

    Optional<User> user = this.userRepository.findByMail(username);
    String oldPassword = bcryptEncoder.encode(changePassword.getOldPassword());

    if(user.isPresent() && user.get().getPassword().equals(oldPassword)){
      user.get().setPassword(bcryptEncoder.encode(changePassword.getNewPassword()));
      this.userRepository.save(user.get());
    }
    throw new ApiException("Credenciales invalidas", "Credenciales invalidas", 400);
  }

}
