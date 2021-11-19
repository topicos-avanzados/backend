package com.topicos.backend.services;

import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.NewUserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.User;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.UserRepository;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.utils.Mappers;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
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

  private final CompanyRepository companyRepository;

  private final UserRepository userRepository;

  private final JwtTokenUtil jwtTokenUtil;

  private final JwtUserDetailsService jwtUserDetailsService;

  private final AuthenticationManager authenticationManager;

  private PasswordEncoder bcryptEncoder;


  public UserDTO saveAndSendMail(NewUserDTO userRequestDTO, String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      Optional<User> user = this.userRepository.findByUsername(userRequestDTO.getUsername());
      Optional<Company> company = this.companyRepository.findById(userRequestDTO.getCompanyId());
      if (user.isEmpty() && company.isPresent()) {
        User newUser = this.userRepository.save(User
            .builder()
            .admin(false)
            .username(userRequestDTO.getUsername())
            .mail(userRequestDTO.getMail())
            .companyId(company.get())
            .password(bcryptEncoder.encode("a"))
            .active(false)
            .build());

        UserDetails details = this.jwtUserDetailsService.loadUserByUsername(userRequestDTO.getUsername());

        String newToken = this.jwtTokenUtil.generateToken(details, false, newUser
            .getCompanyId()
            .getId());
        System.out.println(newToken);
        //TODO SEND MAIL

        return Mappers.buildUserDTO(newUser);
      }
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }

  @Transactional
  public String activate(UserCredentialDTO userCredential, String token) {
    UserDetails details = this.jwtUserDetailsService.loadUserByUsername(userCredential.getUsername());
    if (this.jwtTokenUtil.validateToken(token, details)) {
      this.jwtUserDetailsService.update(userCredential);
      Long company = this.jwtTokenUtil.getCompanyFromToken(token);
      return this.jwtTokenUtil.generateToken(details, false, company);

    }
    throw new ApiException("Invalid token", "Invalid request, the token could be expired or invalid", 400);
  }

  public String loginAndGenerateToken(UserCredentialDTO user) {

    Authentication authentication =
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    if (authentication.isAuthenticated()) {
      UserDetails details = this.jwtUserDetailsService.loadUserByUsername(user.getUsername());

      Optional<User> optionalUser = this.userRepository.findByUsername(user.getUsername());

      return this.jwtTokenUtil.generateToken(details, optionalUser
          .get()
          .getAdmin(), optionalUser
          .get()
          .getCompanyId()
          .getId());
    }
    throw new ApiException("Invalid credentials", "Invalid credentials", 404);
  }

  public String newAdmin(UserCredentialDTO userRequestDTO) {
    Optional<User> user = userRepository.findByUsername(userRequestDTO.getUsername());
    if (user.isEmpty()) {
      User newUser = User
          .builder()
          .admin(true)
          .username(userRequestDTO.getUsername())
          .password(bcryptEncoder.encode(userRequestDTO.getPassword()))
          .build();

      userRepository.save(newUser);

      UserDetails details = this.jwtUserDetailsService.loadUserByUsername(userRequestDTO.getUsername());
      return this.jwtTokenUtil.generateToken(details, true, 1L);
    }
    throw new ApiException("ERROR", "user ya registrado", HttpStatus.BAD_REQUEST.value());
  }

}
