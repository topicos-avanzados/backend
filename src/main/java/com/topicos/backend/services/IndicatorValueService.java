package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
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
public class IndicatorValueService {

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  public List<IndicatorValueDTO> getAllIndicatorsValues(Long indicatorId) {
    return this.indicatorValueRepository
        .findAllByIndicatorId(indicatorId)
        .stream()
        .map(Mappers::buildIndicatorValueDTO)
        .collect(Collectors.toList());
  }

  public IndicatorValueDTO addIndicatorValue(IndicatorValueDTO indicator) {
    //lanzar excepcion si no lo encuentra
    Indicator ind = this.indicatorRepository.getById(indicator.getIndicatorId());
    IndicatorValue indicatorValue = this.indicatorValueRepository.save(Mappers.buildIndicatorValue(indicator, ind));
    indicator.setId(indicatorValue.getId());
    return indicator;
  }

  public void deleteIndicatorValue(Long indicatorValueId) {
    Optional<IndicatorValue> indicatorValue = this.indicatorValueRepository.findById(indicatorValueId);
    indicatorValue.ifPresent(this.indicatorValueRepository::delete);
  }

  public IndicatorValueDTO modifyIndicatorValue(IndicatorValueDTO indicator) {
    Optional<IndicatorValue> optionalIndicatorValue = this.indicatorValueRepository.findById(indicator.getId());
    if (optionalIndicatorValue.isPresent()) {
      IndicatorValue indicatorValueToSave = optionalIndicatorValue.get();
      indicatorValueToSave.setValue(indicator.getValue());
      this.indicatorValueRepository.save(indicatorValueToSave);
      return Mappers.buildIndicatorValueDTO(indicatorValueToSave);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }

}