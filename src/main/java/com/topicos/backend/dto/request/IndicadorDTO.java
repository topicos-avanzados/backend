package com.topicos.backend.dto.request;

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
public class IndicadorDTO {
  private Long indicadorId;

  private Long value;

  private String name;

  private LocalDateTime date;
}
