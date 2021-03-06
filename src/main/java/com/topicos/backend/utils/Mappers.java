package com.topicos.backend.utils;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.CompanyDTO;
import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.UserDTO;
import com.topicos.backend.dto.request.CompanyRequestDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.model.Log;
import com.topicos.backend.persistence.model.User;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder.In;

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
        .indicatorLeft(indicator.getIndicatorLeft())
        .indicatorRight(indicator.getIndicatorRight())
        .operator(indicator.getOperator())
        .constant(indicator.getConstant())
        .build();
  }
  static IndicatorDTO buildIndicatorDTO(Indicator indicator) {
    return  IndicatorDTO
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

  static IndicatorDTO buildIndicatorDTOExpanded(Indicator indicator, Indicator left, Indicator right) {
    IndicatorDTO ind = IndicatorDTO
        .builder()
        .id(indicator.getId())
        .name(indicator.getName())
        .frequency(indicator.getFrequency())
        .type(indicator.getType())
        .unit(indicator.getUnit())
        .area(buildAreaDTO(indicator.getAreaId()))
        .description(indicator.getDescription())
        .build();
    if(!Objects.isNull(left) && !Objects.isNull(right)){
      ind.setFormula(left.getName()+" " + indicator.getOperator()+" " + right.getName());
    } else if (!Objects.isNull(left)){
      ind.setFormula(left.getName() +" "+ indicator.getOperator()+" " + indicator.getConstant());
    } else if (!Objects.isNull(right)){
      ind.setFormula(indicator.getConstant() +" "+ indicator.getOperator()+" " + right.getName());
    }

    return ind;
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

  static UserDTO buildUserDTO(User user) {
    return UserDTO
        .builder()
        .mail(user.getMail())
        .company(buildCompanyDTO(user.getCompanyId()))
        .id(user.getId())
        .build();
  }

  static Log buildLog(LogDTO log) {
    return Log
        .builder()
        .id(log.getId())
        .email(log.getEmail())
        .date(log.getDate())
        .payload(log.getPayload())
        .build();
  }
  static LogDTO buildLogDTO(Log log) {
    return LogDTO
        .builder()
        .id(log.getId())
        .email(log.getEmail())
        .date(log.getDate())
        .payload(log.getPayload())
        .build();
  }
}
