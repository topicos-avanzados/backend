package com.topicos.backend.dto;

import java.time.LocalDateTime;
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
public class IndicatorValueDTO {

  private Long id;

  private Long indicatorId;

  private Long value;

  private String name;

  private LocalDateTime date;

}