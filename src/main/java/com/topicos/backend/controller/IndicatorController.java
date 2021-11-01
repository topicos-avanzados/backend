package com.topicos.backend.controller;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.AreaRequestDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.services.IndicatorService;
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
public class IndicatorController {

  private final IndicatorService indicatorService;

  //CREATE
  @PostMapping("/indicator/create")
  public IndicatorDTO addIndicator(@RequestBody IndicatorDTO indicator) {
    return this.indicatorService.createIndicator(indicator);
  }

  @PostMapping("/indicator/new_value")
  public IndicatorValueDTO addIndicatorValue(@RequestBody IndicatorValueDTO indicator) {
    return this.indicatorService.addIndicatorValue(indicator);
  }

  @PostMapping("/company/create")
  public CompanyDTO addCompany(@RequestBody CompanyRequestDTO company) {
    return this.indicatorService.addCompany(company);
  }

  @PostMapping("/area/create")
  public AreaDTO addArea(@RequestBody AreaRequestDTO areaDTO) {
    return this.indicatorService.addArea(areaDTO);
  }

  //DELETE
  @DeleteMapping("/indicator/delete")
  public void deleteIndicator(@RequestParam Long indicatorId) {
    this.indicatorService.deleteIndicator(indicatorId);
  }

  @DeleteMapping("/indicators_values/delete")
  public void deleteIndicatorValue(@RequestParam Long indicatorValueId) {
    this.indicatorService.deleteIndicatorValue(indicatorValueId);
  }

  @DeleteMapping("/company/delete")
  public void deleteCompany(@RequestParam Long companyId) {
    this.indicatorService.deleteCompany(companyId);
  }

  @DeleteMapping("/area/delete")
  public void deleteArea(@RequestParam Long areaId) {
    this.indicatorService.deleteArea(areaId);
  }

  //GET

  @GetMapping("/indicators")
  public List<IndicatorDTO> getAllIndicators(@RequestParam Long companyId) {
    return this.indicatorService.getAllIndicators(companyId);
  }

  @GetMapping("/indicators_values")
  public List<IndicatorValueDTO> getAllIndicatorsValues(@RequestParam Long indicatorId) {
    return this.indicatorService.getAllIndicatorsValues(indicatorId);
  }

  @GetMapping("/areas")
  public List<AreaDTO> getAllAreas(Long companyId) {
    return this.indicatorService.getAllAreas(companyId);
  }

}
