package com.topicos.backend.services;

import static com.topicos.backend.utils.Mappers.buildIndicatorDTO;

import com.topicos.backend.dto.IndicatorDTO;
import com.topicos.backend.dto.request.IndicatorRequestDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.repository.AreaRepository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.utils.Mappers;
import java.util.List;
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

  public List<IndicatorDTO> getAllIndicators() {
    return this.indicatorRepository
        .findAll()
        .stream()
        .map(Mappers::buildIndicatorDTO)
        .collect(Collectors.toList());
  }

  public IndicatorDTO createIndicator(IndicatorRequestDTO indicator) {
    Optional<Area> optionalArea = this.areaRepository.findById(indicator.getAreaId());
    Indicator ind = this.indicatorRepository.save(Mappers.buildIndicator(indicator, optionalArea.get()));
    indicator.setId(ind.getId());
    return buildIndicatorDTO(ind);
  }

  @Transactional
  public void deleteIndicator(Long indicatorId) {
    Optional<Indicator> indicator = this.indicatorRepository.findById(indicatorId);
    indicator.ifPresent(this.indicatorRepository::delete);
  }

  public IndicatorDTO modifyIndicator(IndicatorRequestDTO indicator) {
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
      return buildIndicatorDTO(indicatorToSave);
    }
    return null;
  }

}
