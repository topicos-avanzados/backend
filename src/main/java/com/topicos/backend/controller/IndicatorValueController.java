package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.IndicatorValueService;
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
public class IndicatorValueController {

  private final IndicatorValueService indicatorValueService;

  private final JwtTokenUtil jwtTokenUtil;

  //CREATE
  @PostMapping("/indicator_value/create")
  public IndicatorValueDTO addIndicatorValue(@RequestBody IndicatorValueRequestDTO indicator,
      @RequestHeader("Authorization") String token) {
    if (!this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.indicatorValueService.addIndicatorValue(indicator);
    }
    throw new UnauthorizedException("The user is an admin", "The user is an admin");
  }

  //DELETE
  @DeleteMapping("/indicator_value/delete")
  public void deleteIndicatorValue(@RequestParam Long id, @RequestHeader("Authorization") String token) {
    if (!this.jwtTokenUtil.getAdminFromToken(token)) {
      this.indicatorValueService.deleteIndicatorValue(id);
    }
    throw new UnauthorizedException("The user is an admin", "The user is an admin");
  }

  //GET
  @GetMapping("/indicator_value")
  public List<IndicatorValueDTO> getAllIndicatorsValues(@RequestParam(required = false) Long indicator,
      @RequestParam(required = false) Long company, @RequestHeader("Authorization") String token) {
    if (!this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.indicatorValueService.getAllIndicatorsValues(indicator, company);
    }
    throw new UnauthorizedException("The user is an admin", "The user is an admin");
  }

  //MODIFICATION
  @PutMapping("/indicator_value/modify")
  public IndicatorValueDTO modifyIndicatorValue(@RequestBody IndicatorValueRequestDTO indicator,
      @RequestHeader("Authorization") String token) {
    if (!this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.indicatorValueService.modifyIndicatorValue(indicator);
    }
    throw new UnauthorizedException("The user is an admin", "The user is an admin");
  }

}
