package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.utils.Mappers;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IndicatorValueService {

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  private final CompanyRepository companyRepository;

  private final JwtTokenUtil jwtTokenUtil;

  private final LogService logService;

  private final ComplexIndicatorValue complexIndicatorValue;

  public List<IndicatorValueDTO> getAllIndicatorsValues(Long indicatorId, Long companyId, Date from, Date to) {
    if (!Objects.isNull(indicatorId) && !Objects.isNull(companyId)) {
      if (!Objects.isNull(from) && !Objects.isNull(to)) {
        return this.indicatorValueRepository
            .findAllByIndicatorIdCompanyIdAndDateRanges(indicatorId, companyId, from, to)
            .stream()
            .map(Mappers::buildIndicatorValueDTO)
            .collect(Collectors.toList());
      } else {
        return this.indicatorValueRepository
          .findAllByIndicatorId_IdAndCompanyId_Id(indicatorId, companyId)
          .stream()
          .map(Mappers::buildIndicatorValueDTO)
          .collect(Collectors.toList());
      }
    } else if (!Objects.isNull(indicatorId)) {
      return this.indicatorValueRepository
          .findAllByIndicatorId_Id(indicatorId)
          .stream()
          .map(Mappers::buildIndicatorValueDTO)
          .collect(Collectors.toList());
    } else if (!Objects.isNull(companyId)) {
      return this.indicatorValueRepository
          .findAllByCompanyId_Id(indicatorId)
          .stream()
          .map(Mappers::buildIndicatorValueDTO)
          .collect(Collectors.toList());
    }
    return this.indicatorValueRepository
        .findAll()
        .stream()
        .map(Mappers::buildIndicatorValueDTO)
        .collect(Collectors.toList());
  }

  public IndicatorValueDTO addIndicatorValue(IndicatorValueRequestDTO indicator, String token) {
    Optional<Indicator> ind = this.indicatorRepository.findById(indicator.getIndicatorId());
    Optional<Company> company = this.companyRepository.findById(indicator.getCompanyId());
    if (ind.isPresent() && company.isPresent()) {
      if ("D".equals(ind
          .get()
          .getType())) {
        IndicatorValue indicatorValue =
            this.indicatorValueRepository.save(Mappers.buildIndicatorValue(indicator, ind.get(), company.get()));
        indicator.setId(indicatorValue.getId());
        LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),
            "Se agrego un nuevo valor de indicador para la empresa: " + indicator.getCompanyId());
        logService.addLog(newLog);
        return (Mappers.buildIndicatorValueDTO(indicatorValue));
      }
      return this.complexIndicatorValue.addIndicatorValueComplex(indicator, ind.get(), company.get(), token);
    }
    return null;
  }

  public void deleteIndicatorValue(Long indicatorValueId, String token) {
    Optional<IndicatorValue> indicatorValue = this.indicatorValueRepository.findById(indicatorValueId);
    if (indicatorValue.isPresent()) {
      this.indicatorValueRepository.delete(indicatorValue.get());
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),
          "Se elimino un valor de indicador para la empresa: " + indicatorValue
              .get()
              .getCompanyId());
      logService.addLog(newLog);
    }

  }

  public IndicatorValueDTO modifyIndicatorValue(IndicatorValueRequestDTO indicator, String token) {
    Optional<IndicatorValue> optionalIndicatorValue = this.indicatorValueRepository.findById(indicator.getId());

    if (optionalIndicatorValue.isPresent()) {
      IndicatorValue indicatorValueToSave = optionalIndicatorValue.get();
      indicatorValueToSave.setValue(indicator.getValue());
      indicatorValueToSave.setDate(indicator.getDate());
      this.indicatorValueRepository.save(indicatorValueToSave);
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se modifico un valor de indicador para la empresa: " + indicatorValueToSave.getCompanyId());
      logService.addLog(newLog);
      return Mappers.buildIndicatorValueDTO(indicatorValueToSave);
    }
    return null;
  }

}