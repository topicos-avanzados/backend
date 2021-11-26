package com.topicos.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorDTO {

  private Long id;

  private String name;

  private String unit;

  private String type;

  private Long frequency;

  private AreaDTO area;

  private String description;

  private String formula;

}
