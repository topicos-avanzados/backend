package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorDTO;
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

@Slf4j
@Service
@AllArgsConstructor
public class IndicatorService {

  private final IndicatorRepository indicatorRepository;

  private final AreaRepository areaRepository;

  public List<IndicatorDTO> getAllIndicators(Long companyId) {
    return this.indicatorRepository
        .findAllByAreaId_Id(companyId)
        .stream()
        .map(Mappers::buildIndicatorDTO)
        .collect(Collectors.toList());
  }

  public IndicatorDTO createIndicator(IndicatorDTO indicator) {

    Indicator ind = this.indicatorRepository.save(Mappers.buildIndicator(indicator));
    indicator.setId(ind.getId());
    return indicator;
  }

  public void deleteIndicator(Long indicatorId) {
    Optional<Indicator> indicator = this.indicatorRepository.findById(indicatorId);
    indicator.ifPresent(this.indicatorRepository::delete);
  }

  public IndicatorDTO modifyIndicator(IndicatorDTO indicator) {
    Optional<Indicator> optionalIndicator = this.indicatorRepository.findById(indicator.getId());
    Optional<Area> optionalArea = this.areaRepository.findById(indicator.getAreaId());
    if (optionalIndicator.isPresent() && optionalArea.isPresent()) {
      Indicator indicatorToSave = optionalIndicator.get();
      indicatorToSave.setAreaId(optionalArea.get());
      this.indicatorRepository.save(indicatorToSave);
      return Mappers.buildIndicatorDTO(indicatorToSave);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }

}
