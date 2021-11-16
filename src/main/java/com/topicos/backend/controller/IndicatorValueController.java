package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.services.IndicatorValueService;
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
public class IndicatorValueController {

  private final IndicatorValueService indicatorValueService;

  //CREATE
  @PostMapping("/indicator_value/create")
  public IndicatorValueDTO addIndicatorValue(@RequestBody IndicatorValueDTO indicator) {
    return this.indicatorValueService.addIndicatorValue(indicator);
  }

  //DELETE
  @DeleteMapping("/indicator_value/delete")
  public void deleteIndicatorValue(@RequestParam Long id) {
    this.indicatorValueService.deleteIndicatorValue(id);
  }

  //GET
  @GetMapping("/indicator_value")
  public List<IndicatorValueDTO> getAllIndicatorsValues(@RequestParam(required = false) Long indicator,
      @RequestParam(required = false) Long company) {
    return this.indicatorValueService.getAllIndicatorsValues(indicator, company);
  }

  //MODIFICATE
  @PutMapping("/indicator_value/modify")
  public IndicatorValueDTO modifyIndicatorValue(@RequestBody IndicatorValueDTO indicator) {
    return this.indicatorValueService.modifyIndicatorValue(indicator);
  }

}
