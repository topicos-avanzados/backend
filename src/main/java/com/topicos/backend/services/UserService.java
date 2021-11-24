package com.topicos.backend.services;

import com.topicos.backend.dto.UserDTO;
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

        String newToken = this.jwtTokenUtil.generateMailToken(details, false, newUser
            .getCompanyId()
            .getId());
        System.out.println(newToken);
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

      String token = this.jwtTokenUtil.generateToken(details, optionalUser
          .get()
          .getAdmin(), optionalUser
          .get()
          .getCompanyId()
          .getId());

      return new UserLoginDTO(token, Mappers.buildCompanyDTO(optionalUser
          .get()
          .getCompanyId()));


    }
    throw new ApiException("Invalid credentials", "Invalid credentials", 404);
  }

  public UserLoginDTO newAdmin(UserCredentialDTO userRequestDTO) {
    Optional<User> user = userRepository.findByMail(userRequestDTO.getMail());
    Company company = this.companyRepository
        .findByName("DERES")
        .orElseGet(() -> this.companyRepository.save(Company
            .builder()
            .name("DERES")
            .businessName("Deres")
            .businessArea("General")
            .rut(1234)
            .build()));
    if (user.isEmpty()) {
      User newUser = User
          .builder()
          .admin(true)
          .active(true)
          .mail(userRequestDTO.getMail())
          .password(bcryptEncoder.encode(userRequestDTO.getPassword()))
          .companyId(company)
          .build();

      userRepository.save(newUser);

      UserDetails details = this.jwtUserDetailsService.loadUserByUsername(userRequestDTO.getMail());
      String token = this.jwtTokenUtil.generateToken(details, true, company.getId());
      return new UserLoginDTO(token, Mappers.buildCompanyDTO(company));
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }


}
