package com.topicos.backend.dto.request;

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
public class IndicatorRequestDTO {

  private Long id;

  private String name;

  private String unit;

  private String type;

  private Long frequency;

  private Long areaId;

  private String description;

}