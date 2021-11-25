package com.topicos.backend.controller;

import com.topicos.backend.dto.NotificationDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.NotificationService;
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
public class NotificationsController {

  private final JwtTokenUtil jwtTokenUtil;

  private final NotificationService notificationService;

  //GET
  @GetMapping("/notifications")
  public List<NotificationDTO> getAllNotifications(@RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      throw new UnauthorizedException("The user is an admin", "The user is an admin");
    }
    return this.notificationService.getAll(this.jwtTokenUtil.getCompanyFromToken(token));
  }

}
