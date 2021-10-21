package com.topicos.backend.dto.response;

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
public class IndicadorResponseDTO {
  private Long id;

  private Long indicadorId;

  private Long value;

  private String name;

  private LocalDateTime date;
}
