package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.IndicatorService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
public class IndicatorController {

  private final IndicatorService indicatorService;

  private final JwtTokenUtil jwtTokenUtil;

  //CREATE
  @PostMapping("/indicator/create")
  public IndicatorDTO addIndicator(@RequestBody IndicatorRequestDTO indicator, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.indicatorService.createIndicator(indicator, token);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");

  }

  //DELETE
  @DeleteMapping("/indicator/delete")
  public void deleteIndicator(@RequestParam Long id, @RequestHeader("Authorization") String token) {
    if (!this.jwtTokenUtil.getAdminFromToken(token)) {
      throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
    }
    this.indicatorService.deleteIndicator(id, token);

  }

  //GET
  @GetMapping("/indicator")
  public List<IndicatorDTO> getAllIndicators(@RequestHeader("Authorization") String token) {
    return this.indicatorService.getAllIndicators();
  }

  //MODIFICATION
  @PutMapping("/indicator/modify")
  public IndicatorDTO modifyIndicator(@RequestBody IndicatorRequestDTO indicator, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.indicatorService.modifyIndicator(indicator, token);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

}
