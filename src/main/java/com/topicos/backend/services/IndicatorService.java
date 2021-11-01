package com.topicos.backend.services;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.AreaRequestDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.AreaRepository;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.utils.Mappers;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IndicatorService {

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  private final AreaRepository areaRepository;

  private final CompanyRepository companyRepository;

  public List<IndicatorDTO> getAllIndicators(Long companyId) {
    return this.indicatorRepository
        .findAllByCompanyId(companyId)
        .stream()
        .map(Mappers::buildIndicatorDTO)
        .collect(Collectors.toList());
  }

  public List<IndicatorValueDTO> getAllIndicatorsValues(Long indicatorId) {
    return this.indicatorValueRepository
        .findAllByIndicatorId(indicatorId)
        .stream()
        .map(Mappers::buildIndicatorValueDTO)
        .collect(Collectors.toList());
  }

  public List<AreaDTO> getAllAreas(Long companyId) {
    return this.areaRepository
        .findAllByCompanyId(companyId)
        .stream()
        .map(Mappers::buildAreaDTO)
        .collect(Collectors.toList());
  }

  public IndicatorDTO createIndicator(IndicatorDTO indicator) {

    Indicator ind = this.indicatorRepository.save(Mappers.buildIndicator(indicator));
    indicator.setId(ind.getId());
    return indicator;
  }

  public IndicatorValueDTO addIndicatorValue(IndicatorValueDTO indicator) {
    //lanzar excepcion si no lo encuentra
    Indicator ind = this.indicatorRepository.getById(indicator.getIndicatorId());
    IndicatorValue indicatorValue = this.indicatorValueRepository.save(Mappers.buildIndicatorValue(indicator, ind));
    indicator.setId(indicatorValue.getId());
    return indicator;
  }


  public CompanyDTO addCompany(CompanyRequestDTO companyRequest) {
    Company company = this.companyRepository.save(Company
        .builder()
        .name(companyRequest.getName())
        .build());
    return CompanyDTO
        .builder()
        .id(company.getId())
        .name(company.getName())
        .build();
  }

  public AreaDTO addArea(AreaRequestDTO areaDTO) {
    Area area = this.areaRepository.save(Area
        .builder()
        .name(areaDTO.getName())
        .build());
    return AreaDTO
        .builder()
        .id(area.getId())
        .name(area.getName())
        .build();
  }

  public void deleteIndicator(Long indicatorId) {
    Optional<Indicator> indicator = this.indicatorRepository.findById(indicatorId);
    indicator.ifPresent(this.indicatorRepository::delete);
  }

  public void deleteIndicatorValue(Long indicatorValueId) {
    Optional<IndicatorValue> indicatorValue = this.indicatorValueRepository.findById(indicatorValueId);
    indicatorValue.ifPresent(this.indicatorValueRepository::delete);
  }

  public void deleteCompany(Long companyId) {
    Optional<Company> company = this.companyRepository.findById(companyId);
    company.ifPresent(this.companyRepository::delete);
  }

  public void deleteArea(Long areaId) {
    Optional<Area> area = this.areaRepository.findById(areaId);
    area.ifPresent(this.areaRepository::delete);
  }


}
