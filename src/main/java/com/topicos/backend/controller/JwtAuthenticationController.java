package com.topicos.backend.controller;

import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.NewUserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.AuthenticateService;
import com.topicos.backend.services.JwtUserDetailsService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class JwtAuthenticationController {

  private AuthenticateService authenticateService;

  private JwtTokenUtil jwtTokenUtil;

  private JwtUserDetailsService userDetailsService;

  @Autowired
  public JwtAuthenticationController(AuthenticateService authenticateService, JwtTokenUtil jwtTokenUtil,
      JwtUserDetailsService userDetailsService) {
    this.authenticateService = authenticateService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping(value = "/register")
  public UserDTO saveUser(@Valid @RequestBody NewUserDTO user) throws Exception {
    this.authenticateService.authenticate(user.getUsername(), user.getPassword());
    return this.userDetailsService.saveAndSendMail(user);
  }

  @PostMapping(value = "/changepassword")
  public void saveUser(@Valid @RequestBody UserCredentialDTO user) throws Exception {
    this.authenticateService.authenticate(user.getUsername(), user.getPassword());
    this.userDetailsService.update(user);
  }

  @PostMapping(value = "/login")
  public void createAuthenticationToken(@RequestBody UserCredentialDTO user) throws Exception {

    this.authenticateService.authenticate(user.getUsername(), user.getPassword());

   /* authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final Boolean admin = userService
        .findByUsername(authenticationRequest.getUsername())
        .getAdmin();

    final String token = jwtTokenUtil.generateToken(userDetails, admin, company);

    return ResponseEntity.ok(new JwtResponse(token));*/
  }


}
