package com.topicos.backend.controller;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.request.AreaRequestDTO;
import com.topicos.backend.services.AreaService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@AllArgsConstructor
public class AreaController {

  private final AreaService areaService;

  //CREATE
  @PostMapping("/area/create")
  public AreaDTO addArea(@RequestBody AreaRequestDTO areaDTO) {
    return this.areaService.addArea(areaDTO);
  }

  //DELETE
  @DeleteMapping("/area/delete")
  public void deleteArea(@RequestParam Long areaId) {
    this.areaService.deleteArea(areaId);
  }

  //GET
  @GetMapping("/areas")
  public List<AreaDTO> getAllAreas(Long companyId) {
    return this.areaService.getAllAreas(companyId);
  }

  //MODIFICATE
  @PostMapping("/area/modify")
  public AreaDTO modifyArea(@RequestBody AreaDTO areaDTO) {
    return this.areaService.modifyArea(areaDTO);
  }

}
