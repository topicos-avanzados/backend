package com.topicos.backend.services;

import static com.topicos.backend.utils.Mappers.buildIndicatorDTO;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.exceptions.ApiException;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.repository.AreaRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.security.JwtTokenUtil;
import com.topicos.backend.utils.Mappers;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class IndicatorService {

  private final IndicatorRepository indicatorRepository;

  private final AreaRepository areaRepository;

  private final JwtTokenUtil jwtTokenUtil;

  private final LogService logService;

  public List<IndicatorDTO> getAllIndicators() {
    return this.indicatorRepository
        .findAll()
        .stream()
        .map(this::generateIndicatorDTOExpanded)
        .collect(Collectors.toList());
  }

  public IndicatorDTO createIndicator(IndicatorRequestDTO indicator, String token) {
    Optional<Area> optionalArea = this.areaRepository.findById(indicator.getAreaId());
    if ("D".equals(indicator.getType())) {
      Indicator ind = this.indicatorRepository.save(Mappers.buildIndicator(indicator, optionalArea.get()));
      indicator.setId(ind.getId());
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token), "Se registro una nuevo indicador: " + indicator.getName());
      logService.addLog(newLog);
      return buildIndicatorDTO(ind);
    }
    if (Objects.isNull(indicator.getIndicatorLeft())) {
      Optional<Indicator> indRight = this.indicatorRepository.findById(indicator.getIndicatorRight());
      if (indRight.isPresent()) {
        return this.save(indicator, token, optionalArea.get());
      }
      return null;
    }
    if (Objects.isNull(indicator.getIndicatorRight())) {
      Optional<Indicator> indLeft = this.indicatorRepository.findById(indicator.getIndicatorLeft());
      if (indLeft.isPresent()) {
        return this.save(indicator, token, optionalArea.get());
      }
      return null;
    }
    Optional<Indicator> indLeft = this.indicatorRepository.findById(indicator.getIndicatorLeft());
    Optional<Indicator> indRight = this.indicatorRepository.findById(indicator.getIndicatorRight());
    if (indLeft.isPresent() && indRight.isPresent()) {
      return this.save(indicator, token, optionalArea.get());
    }
    return null;
  }

  private IndicatorDTO save(IndicatorRequestDTO indicator, String token, Area area) {
    Indicator ind = this.indicatorRepository.save(Mappers.buildIndicator(indicator, area));
    indicator.setId(ind.getId());
    LogDTO newLog =
        new LogDTO(jwtTokenUtil.getUsernameFromToken(token), "Se registro una nuevo indicador complejo: " + indicator.getName());
    logService.addLog(newLog);
    return buildIndicatorDTO(ind);
  }

  @Transactional
  public void deleteIndicator(Long indicatorId, String token) {
    Optional<Indicator> indicator = this.indicatorRepository.findById(indicatorId);
    List<Indicator> indicatorAppearence = this.indicatorRepository.findAllByIndicatorLeftOrIndicatorRight(indicatorId, indicatorId);
    if (indicator.isPresent() && indicatorAppearence.isEmpty()) {
      String name = indicator
          .get()
          .getName();
      this.indicatorRepository.delete(indicator.get());
      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token), "Se elimino el indicador: " + name);
      logService.addLog(newLog);
      return;
    }
    throw new ApiException("No se pudo borrar el indicador", "No se pudo borrar el indicador", 500);
  }

  public IndicatorDTO modifyIndicator(IndicatorRequestDTO indicator, String token) {
    Optional<Indicator> optionalIndicator = this.indicatorRepository.findById(indicator.getId());
    Optional<Area> optionalArea = this.areaRepository.findById(indicator.getAreaId());
    if (optionalIndicator.isPresent() && optionalArea.isPresent()) {
      Indicator indicatorToSave = optionalIndicator.get();
      indicatorToSave.setAreaId(optionalArea.get());
      indicatorToSave.setDescription(indicator.getDescription());
      indicatorToSave.setFrequency(indicator.getFrequency());
      indicatorToSave.setType(indicator.getType());
      indicatorToSave.setName(indicator.getName());
      indicatorToSave.setUnit(indicator.getUnit());
      this.indicatorRepository.save(indicatorToSave);

      LogDTO newLog = new LogDTO(jwtTokenUtil.getUsernameFromToken(token),"Se actualizo el indicador: " + optionalIndicator.get().getName());
      logService.addLog(newLog);

      return buildIndicatorDTO(indicatorToSave);
    }
    throw new ApiException("No se pudo actualizar el indicador", "No se pudo actualizar el indicador", 500);
  }

  private IndicatorDTO generateIndicatorDTOExpanded(Indicator indicator){

    if("D".equals(indicator.getType())){
      return Mappers.buildIndicatorDTO(indicator);
    }
    if(!Objects.isNull(indicator.getIndicatorRight()) && !Objects.isNull(indicator.getIndicatorLeft())){
      Optional<Indicator> left = this.indicatorRepository.findById(indicator.getIndicatorLeft());
      Optional<Indicator> right = this.indicatorRepository.findById(indicator.getIndicatorRight());
      if(left.isPresent() && right.isPresent()){
        return Mappers.buildIndicatorDTOExpanded(indicator, left.get(), right.get());
      }
      throw new ApiException("Alguno de los indicadores dejo de existir", "Alguno de los indicadores dejo de existir", 500);
    }else if(Objects.isNull(indicator.getIndicatorRight()) && !Objects.isNull(indicator.getIndicatorLeft())){
      Optional<Indicator> left = this.indicatorRepository.findById(indicator.getIndicatorLeft());
      if(left.isPresent() ){
        return Mappers.buildIndicatorDTOExpanded(indicator, left.get(),null);
      }
      throw new ApiException("Alguno de los indicadores dejo de existir", "Alguno de los indicadores dejo de existir", 500);
    }
    else if(!Objects.isNull(indicator.getIndicatorRight()) && Objects.isNull(indicator.getIndicatorLeft())){
      Optional<Indicator> right = this.indicatorRepository.findById(indicator.getIndicatorRight());
      if(right.isPresent() ){
        return Mappers.buildIndicatorDTOExpanded(indicator, null,right.get());
      }
      throw new ApiException("Alguno de los indicadores dejo de existir", "Alguno de los indicadores dejo de existir", 500);
    }
    throw new ApiException("Error", "Error", 500);
    }

}
