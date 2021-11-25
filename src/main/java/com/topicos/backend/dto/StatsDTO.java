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
public class StatsDTO {

  private int areas;

  private int companies;

  private int indicators;

  private int indicatorsValues;

  private int users;

}
