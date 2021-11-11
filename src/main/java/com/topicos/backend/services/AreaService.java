package com.topicos.backend.services;

import com.topicos.backend.dto.AreaDTO;
import com.topicos.backend.dto.request.AreaRequestDTO;
import com.topicos.backend.persistence.model.Area;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.repository.AreaRepository;
import com.topicos.backend.persistence.repository.CompanyRepository;
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
public class AreaService {

  private final AreaRepository areaRepository;

  private final CompanyRepository companyRepository;

  public List<AreaDTO> getAllAreas(Long companyId) {
    return this.areaRepository
        .findAllByCompanyId(companyId)
        .stream()
        .map(Mappers::buildAreaDTO)
        .collect(Collectors.toList());
  }

  public AreaDTO addArea(AreaRequestDTO areaDTO) {
    Area area = this.areaRepository.save(Area
        .builder()
        .name(areaDTO.getName())
        .build());
    return AreaDTO
        .builder()
        .id(area.getId())
        .name(area.getName())
        .build();
  }

  public void deleteArea(Long areaId) {
    Optional<Area> area = this.areaRepository.findById(areaId);
    area.ifPresent(this.areaRepository::delete);
  }

  public AreaDTO modifyArea(AreaDTO areaDTO) {
    Optional<Area> optionalArea = this.areaRepository.findById(areaDTO.getId());
    Optional<Company> optionalCompany = this.companyRepository.findById(areaDTO.getCompanyId());
    if (optionalArea.isPresent() && optionalCompany.isPresent()) {
      Area area = optionalArea.get();
      area.setName(areaDTO.getName());
      this.areaRepository.save(area);
      return Mappers.buildAreaDTO(area);
    }
    //FIXME NULL O QUE DEVUELVA OTRA COSA?
    return null;
  }

}
