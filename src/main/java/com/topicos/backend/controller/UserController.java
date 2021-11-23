package com.topicos.backend.controller;

import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.NewUserDTO;
import com.topicos.backend.dto.request.UserCredentialDTO;
import com.topicos.backend.dto.response.UserLoginDTO;
import com.topicos.backend.services.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class UserController {

  private final UserService userService;

  @GetMapping("/user")
  public List<UserDTO> getAllUsers(@RequestHeader("Authorization") String token) {
    return this.userService.getAllUsers();
  }

  @PostMapping(value = "/user/register")
  public UserDTO saveUser(@Valid @RequestBody NewUserDTO user, @RequestHeader("Authorization") String token) {
    return this.userService.saveAndSendMail(user, token);
  }

  @PostMapping(value = "/user/activate")
  public UserLoginDTO activateUser(@Valid @RequestBody UserCredentialDTO user, @RequestHeader("Authorization") String token) {
    return this.userService.activate(user, token);
  }

  @PostMapping(value = "/user/login")
  public UserLoginDTO createAuthenticationToken(@RequestBody UserCredentialDTO user) {
    return this.userService.loginAndGenerateToken(user);
  }

  @PostMapping(value = "/user/admin")
  public UserLoginDTO newAdmin(@RequestBody UserCredentialDTO user) {
    return this.userService.newAdmin(user);
  }

}
