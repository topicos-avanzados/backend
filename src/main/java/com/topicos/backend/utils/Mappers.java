package com.topicos.backend.utils;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;

public interface Mappers {

  static IndicatorValue buildIndicatorValue(IndicatorValueRequestDTO indicatorValue, Indicator indicator, Company company) {
    return IndicatorValue
        .builder()
        .indicatorId(indicator)
        .value(indicatorValue.getValue())
        .date(indicatorValue.getDate())
        .companyId(company)
        .build();
  }

  static IndicatorValueDTO buildIndicatorValueDTO(IndicatorValue indicatorValue) {
    return IndicatorValueDTO
        .builder()
        .indicator(buildIndicatorDTO(indicatorValue.getIndicatorId()))
        .value(indicatorValue.getValue())
        .date(indicatorValue.getDate())
        .id(indicatorValue.getId())
        .company(buildCompanyDTO(indicatorValue.getCompanyId()))
        .build();
  }

  static Indicator buildIndicator(IndicatorRequestDTO indicator, Area area) {
    return Indicator
        .builder()
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .areaId(area)
        .description(indicator.getDescription())
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
        .area(buildAreaDTO(indicator.getAreaId()))
        .description(indicator.getDescription())
        .build();
  }

  static AreaDTO buildAreaDTO(Area area) {
    return AreaDTO
        .builder()
        .id(area.getId())
        .name(area.getName())
        .description(area.getDescription())
        .build();
  }

  static CompanyDTO buildCompanyDTO(Company company) {
    return CompanyDTO
        .builder()
        .id(company.getId())
        .name(company.getName())
        .businessName(company.getBusinessName())
        .rut(company.getRut())
        .businessArea(company.getBusinessArea())
        .build();
  }


  static Company buildCompany(CompanyRequestDTO company) {
    return Company
        .builder()
        .name(company.getName())
        .businessName(company.getBusinessName())
        .rut(company.getRut())
        .businessArea(company.getBusinessArea())
        .build();
  }

}
