package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.utils.Mappers;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComplexIndicatorValue {

  private final IndicatorRepository indicatorRepository;

  private final IndicatorValueRepository indicatorValueRepository;

  private final JwtTokenUtil jwtTokenUtil;

  private final LogService logService;

  public IndicatorValueDTO addIndicatorValueComplex(IndicatorValueRequestDTO indicator, Indicator ind, Company company, String token) {

    Optional<Indicator> indLeft = this.indicatorRepository.findById(ind.getIndicatorLeft());

    if (indLeft.isPresent()) {
      Optional<IndicatorValue> indicatorValueLeft = this.getIndicatorValue(indLeft.get(), indicator);
      if (!Objects.isNull(ind.getIndicatorRight())) {
        Optional<Indicator> indRight = this.indicatorRepository.findById(ind.getIndicatorRight());
        if (indRight.isPresent()) {
          Optional<IndicatorValue> indicatorValueRight = this.getIndicatorValue(indRight.get(), indicator);
          if (indicatorValueLeft.isPresent() && indicatorValueRight.isPresent()) {
            return this.save(indicatorValueLeft.get(), indicatorValueRight.get(), indicator, ind, company, token);
          } else {
            throw new ApiException("No hay valores para ese mes", "No hay valores para ese mes", 400);
          }
        }
        throw new ApiException("No existe alguno de los indicadores", "No existe alguno de los indicadores", 500);
      }
      if (indicatorValueLeft.isPresent()) {
        return this.saveCons(indicatorValueLeft.get(), indicator, ind, company, token);
      }
      throw new ApiException("No hay valores para ese mes", "No hay valores para ese mes", 400);
    }
    return null;
  }

  private Optional<IndicatorValue> getIndicatorValue(Indicator ind, IndicatorValueRequestDTO indicator) {
    List<IndicatorValue> listIndicatorValueLeft =
        this.indicatorValueRepository.findAllByIndicatorId_IdAndCompanyId_IdOrderByDateDesc(ind.getId(), indicator.getCompanyId());

    SimpleDateFormat getMonthFormat = new SimpleDateFormat("MM");
    Integer month = Integer.valueOf(getMonthFormat.format(indicator.getDate()));

    return listIndicatorValueLeft
        .stream()
        .filter(indValue -> month.equals(Integer.valueOf(getMonthFormat.format(indValue.getDate()))))
        .findFirst();
  }

  private IndicatorValueDTO save(IndicatorValue indicatorValueLeft, IndicatorValue indicatorValueRight, IndicatorValueRequestDTO indicator,
      Indicator ind, Company company, String token) {
    IndicatorValue toSave = IndicatorValue
        .builder()
        .value(getValue(indicatorValueLeft, indicatorValueRight, ind))
        .companyId(company)
        .date(indicator.getDate())
        .indicatorId(ind)
        .build();
    IndicatorValue indicatorSaved = this.indicatorValueRepository.save(toSave);
    indicator.setId(indicatorSaved.getId());
    LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),
        "Se agrego un nuevo valor de indicador para la empresa: " + indicator.getCompanyId());
    logService.addLog(newLog);
    return Mappers.buildIndicatorValueDTO(indicatorSaved);
  }

  private Double getValue(IndicatorValue left, IndicatorValue right, Indicator indicator) {
    String operator = indicator.getOperator();
    if ("+".equals(operator)) {
      return left.getValue() + right.getValue();
    } else if ("*".equals(operator)) {
      return left.getValue() * right.getValue();
    } else if ("-".equals(operator)) {
      return left.getValue() - right.getValue();
    } else if ("/".equals(operator)) {
      return left.getValue() / right.getValue();
    }
    throw new ApiException("El operador ingresado es invalido", "El operador ingresado es invalido", 400);
  }

  private IndicatorValueDTO saveCons(IndicatorValue indicatorValueLeft, IndicatorValueRequestDTO indicator, Indicator ind, Company company,
      String token) {
    IndicatorValue toSave = IndicatorValue
        .builder()
        .value(getValueConst(indicatorValueLeft, ind))
        .companyId(company)
        .date(indicator.getDate())
        .indicatorId(ind)
        .build();
    IndicatorValue indicatorSaved = this.indicatorValueRepository.save(toSave);
    indicator.setId(indicatorSaved.getId());
    LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),
        "Se agrego un nuevo valor de indicador para la empresa: " + indicator.getCompanyId());
    logService.addLog(newLog);
    return Mappers.buildIndicatorValueDTO(indicatorSaved);
  }

  private Double getValueConst(IndicatorValue left, Indicator indicator) {
    String operator = indicator.getOperator();
    Double cons = indicator.getConstant();
    if ("+".equals(operator)) {
      return left.getValue() + cons;
    } else if ("*".equals(operator)) {
      return left.getValue() * cons;
    } else if ("-".equals(operator)) {
      return left.getValue() - cons;
    } else if ("/".equals(operator)) {
      return left.getValue() / cons;
    }
    throw new ApiException("El operador ingresado es invalido", "El operador ingresado es invalido", 400);
  }

}
