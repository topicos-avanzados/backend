package com.topicos.backend.controller;

import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.LogService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
public class LogsController {

  private final JwtTokenUtil jwtTokenUtil;

  private final LogService logService;

  @GetMapping("/logs")
  public List<LogDTO> getAllLogs(@RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.logService.getAllLogs();
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

}
