package com.topicos.backend.controller;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.request.AreaRequestDTO;
import com.topicos.backend.exceptions.UnauthorizedException;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.services.AreaService;
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
public class AreaController {

  private final AreaService areaService;

  private final JwtTokenUtil jwtTokenUtil;

  //CREATE
  @PostMapping("/area/create")
  public AreaDTO addArea(@RequestBody AreaRequestDTO areaDTO, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.areaService.addArea(areaDTO, token);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

  //DELETE
  @DeleteMapping("/area/delete")
  public void deleteArea(@RequestParam Long id, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      this.areaService.deleteArea(id, token);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

  //GET
  @GetMapping("/area")
  public List<AreaDTO> getAllAreas(@RequestHeader("Authorization") String token) {
    return this.areaService.getAllAreas();
  }

  //MODIFICATION
  @PutMapping("/area/modify")
  public AreaDTO modifyArea(@RequestBody AreaDTO areaDTO, @RequestHeader("Authorization") String token) {
    if (this.jwtTokenUtil.getAdminFromToken(token)) {
      return this.areaService.modifyArea(areaDTO, token);
    }
    throw new UnauthorizedException("The user is not an admin", "The user is not an admin");
  }

}
