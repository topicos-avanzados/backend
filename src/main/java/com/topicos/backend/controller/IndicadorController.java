package com.topicos.backend.controller;

import com.topicos.backend.dto.request.IndicadorDTO;
import com.topicos.backend.dto.response.IndicadorResponseDTO;
import com.topicos.backend.services.IndicadorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class IndicadorController {

  private final IndicadorService indicadorService;

  @PostMapping("/insert/indicador")
  public IndicadorResponseDTO insertIndicador(@RequestBody IndicadorDTO indicador){

    return this.indicadorService.insertIndicador(indicador);
  }

}
