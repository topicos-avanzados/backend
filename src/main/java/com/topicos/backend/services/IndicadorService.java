package com.topicos.backend.services;

import com.topicos.backend.dto.request.IndicadorDTO;
import com.topicos.backend.dto.response.IndicadorResponseDTO;
import com.topicos.backend.persistence.model.Indicador;
import com.topicos.backend.persistence.repository.IndicadorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IndicadorService {

  private final IndicadorRepository indicadorRepository;

  public IndicadorResponseDTO insertIndicador(IndicadorDTO indicador){

    Indicador ind = this.indicadorRepository.save(this.buildIndicador(indicador));

    return this.buildIndicadorResponseDTO(ind);
  }

  private Indicador buildIndicador(IndicadorDTO indicador){
    return Indicador.builder().indicadorId(indicador.getIndicadorId())
                                   .date(indicador.getDate())
                                   .name(indicador.getName())
                                   .value(indicador.getValue())
                                   .build();
  }

  private IndicadorResponseDTO buildIndicadorResponseDTO(Indicador indicador){
    return IndicadorResponseDTO.builder().indicadorId(indicador.getIndicadorId())
                               .id(indicador.getId())
                               .date(indicador.getDate())
                               .name(indicador.getName())
                               .value(indicador.getValue()).build();
  }

}
