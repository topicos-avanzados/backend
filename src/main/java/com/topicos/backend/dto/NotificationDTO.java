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
public class NotificationDTO {
  private Long id;

  private Long remainingDays;

  private boolean alarm;

  private IndicatorDTO indicator;

  private Long daysFromLast;

}