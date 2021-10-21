package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IndicatorService {

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  public IndicatorDTO createIndicator(IndicatorDTO indicator) {

    Indicator ind = this.indicatorRepository.save(this.buildIndicator(indicator));
    indicator.setId(ind.getId());
    return indicator;
  }

  public IndicatorValueDTO addIndicatorValue(IndicatorValueDTO indicator) {

    IndicatorValue indicatorValue = this.indicatorValueRepository.save(this.buildIndicatorValue(indicator));
    indicator.setId(indicatorValue.getId());
    return indicator;
  }

  private IndicatorValue buildIndicatorValue(IndicatorValueDTO indicator) {
    return IndicatorValue
        .builder()
        .indicatorId(indicator.getIndicatorId())
        .value(indicator.getValue())
        .date(indicator.getDate())
        .build();
  }

  private Indicator buildIndicator(IndicatorDTO indicator) {
    return Indicator
        .builder()
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .build();
  }

  private IndicatorDTO buildIndicatorDTO(Indicator indicator) {
    return IndicatorDTO
        .builder()
        .id(indicator.getId())
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .build();
  }

}
