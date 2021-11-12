package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.utils.Mappers;
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

  public List<IndicatorValueDTO> getAllIndicatorsValues(Long indicatorId, Long companyId) {
    if (!Objects.isNull(indicatorId) && !Objects.isNull(companyId)) {
      return this.indicatorValueRepository
          .findAllByIndicatorId_IdAndCompanyId_Id(indicatorId, companyId)
          .stream()
          .map(Mappers::buildIndicatorValueDTO)
          .collect(Collectors.toList());
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

  public IndicatorValueDTO addIndicatorValue(IndicatorValueDTO indicator) {
    //lanzar excepcion si no lo encuentra
    Indicator ind = this.indicatorRepository.getById(indicator.getIndicatorId());
    Optional<Company> company = this.companyRepository.findById(indicator.getCompanyId());
    //agregar if
    IndicatorValue indicatorValue = this.indicatorValueRepository.save(Mappers.buildIndicatorValue(indicator, ind, company.get()));
    indicator.setId(indicatorValue.getId());
    return indicator;
  }

  public void deleteIndicatorValue(Long indicatorValueId) {
    Optional<IndicatorValue> indicatorValue = this.indicatorValueRepository.findById(indicatorValueId);
    indicatorValue.ifPresent(this.indicatorValueRepository::delete);
  }

  public IndicatorValueDTO modifyIndicatorValue(IndicatorValueDTO indicator) {
    Optional<IndicatorValue> optionalIndicatorValue = this.indicatorValueRepository.findById(indicator.getId());

    Optional<Company> optionalCompany = this.companyRepository.findById(indicator.getCompanyId());
    if (optionalIndicatorValue.isPresent() && optionalCompany.isPresent()) {
      IndicatorValue indicatorValueToSave = optionalIndicatorValue.get();
      indicatorValueToSave.setValue(indicator.getValue());
      indicatorValueToSave.setCompanyId(optionalCompany.get());
      this.indicatorValueRepository.save(indicatorValueToSave);
      return Mappers.buildIndicatorValueDTO(indicatorValueToSave);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }

}