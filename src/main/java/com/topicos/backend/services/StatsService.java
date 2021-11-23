package com.topicos.backend.services;

import com.topicos.backend.dto.StatsDTO;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.AreaRepository;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.persistence.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatsService {

  private final AreaRepository areaRepository;

  private final CompanyRepository companyRepository;

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  private final UserRepository userRepository;

  public StatsDTO getStats() {
    int areas = this.areaRepository
        .findAll()
        .size();
    int companies = this.companyRepository
        .findAll()
        .size();
    int indicators = this.indicatorRepository
        .findAll()
        .size();
    int indicatorsValues = this.indicatorValueRepository
        .findAll()
        .size();
    int users = this.userRepository
        .findAll()
        .size();
    return StatsDTO
        .builder()
        .areas(areas)
        .companies(companies)
        .indicators(indicators)
        .indicatorsValues(indicatorsValues)
        .users(users)
        .build();
  }

  public StatsDTO getStatsByCompany(Long companyId) {
    int users = this.userRepository
        .findAllByCompanyId_Id(companyId)
        .size();
    List<IndicatorValue> indicatorsValues = this.indicatorValueRepository.findAllByCompanyId_Id(companyId);
    Set<Indicator> indicators = indicatorsValues
        .stream()
        .map(IndicatorValue::getIndicatorId)
        .collect(Collectors.toSet());
    int areas = indicators
        .stream()
        .map(Indicator::getAreaId)
        .collect(Collectors.toSet())
        .size();

    return StatsDTO
        .builder()
        .areas(areas)
        .companies(1)
        .indicators(indicators.size())
        .indicatorsValues(indicatorsValues.size())
        .users(users)
        .build();
  }

}
