package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.services.IndicatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IndicatorController {

  private final IndicatorService indicatorService;

  @PostMapping("/indicator/create")
  public IndicatorDTO createIndicator(@RequestBody IndicatorDTO indicator) {
    return this.indicatorService.createIndicator(indicator);
  }

  @PostMapping("/indicator/new_value")
  public IndicatorValueDTO addIndicatorValue(@RequestBody IndicatorValueDTO indicator) {
    return this.indicatorService.addIndicatorValue(indicator);
  }

}
