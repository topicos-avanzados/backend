package com.topicos.backend.services;

import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.dto.response.JwtResponseDTO;
import com.topicos.backend.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenUtil jwtTokenUtil;

  private final JwtUserDetailsService userDetailsService;

  @Autowired
  public AuthenticateService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      JwtUserDetailsService userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  public boolean authenticate(String username, String password) {
    Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    return authentication.isAuthenticated();
  }

  public JwtResponseDTO newToken(UserCredentialDTO userRequestDTO, Long company, Boolean admin) {
    this.authenticate(userRequestDTO.getMail(), userRequestDTO.getPassword());
    final UserDetails userDetails = userDetailsService.loadUserByUsername(userRequestDTO.getMail());

    final String token = this.jwtTokenUtil.generateToken(userDetails, admin, company);
    return new JwtResponseDTO(token);
  }

}
