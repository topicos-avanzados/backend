package com.topicos.backend.utils;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;

public interface Mappers {

  static IndicatorValue buildIndicatorValue(IndicatorValueDTO indicatorValue, Indicator indicator) {
    return IndicatorValue
        .builder()
        .indicatorId(indicator)
        .value(indicatorValue.getValue())
        .date(indicatorValue.getDate())
        .build();
  }

  static IndicatorValueDTO buildIndicatorValueDTO(IndicatorValue indicatorValue) {
    return IndicatorValueDTO
        .builder()
        .indicatorId(indicatorValue
            .getIndicatorId()
            .getId())
        .value(indicatorValue.getValue())
        .date(indicatorValue.getDate())
        .id(indicatorValue.getId())
        .build();
  }

  static Indicator buildIndicator(IndicatorDTO indicator) {
    return Indicator
        .builder()
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .build();
  }

  static IndicatorDTO buildIndicatorDTO(Indicator indicator) {
    return IndicatorDTO
        .builder()
        .id(indicator.getId())
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .build();
  }

  static AreaDTO buildAreaDTO(Area area) {
    return AreaDTO
        .builder()
        .id(area.getId())
        .name(area.getName())
        .companyId(area
            .getCompanyId()
            .getId())
        .build();
  }

}
