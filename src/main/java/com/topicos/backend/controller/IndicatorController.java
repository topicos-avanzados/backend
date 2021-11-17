package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.services.IndicatorService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
public class IndicatorController {

  private final IndicatorService indicatorService;

  //CREATE
  @PostMapping("/indicator/create")
  public IndicatorDTO addIndicator(@RequestBody IndicatorRequestDTO indicator) {
    return this.indicatorService.createIndicator(indicator);
  }

  //DELETE
  @DeleteMapping("/indicator/delete")
  public void deleteIndicator(@RequestParam Long id) {
    this.indicatorService.deleteIndicator(id);
  }

  //GET
  @GetMapping("/indicator")
  public List<IndicatorDTO> getAllIndicators() {
    return this.indicatorService.getAllIndicators();
  }

  //MODIFICATE
  @PutMapping("/indicator/modify")
  public IndicatorDTO modifyIndicator(@RequestBody IndicatorRequestDTO indicator) {
    return this.indicatorService.modifyIndicator(indicator);
  }

}
