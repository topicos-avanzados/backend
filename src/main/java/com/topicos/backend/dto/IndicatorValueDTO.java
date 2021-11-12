package com.topicos.backend.dto;

import java.util.Date;
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

  private Double value;

  private Date date;

  private Long companyId;

}