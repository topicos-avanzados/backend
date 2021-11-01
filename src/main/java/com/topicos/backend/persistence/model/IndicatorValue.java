package com.topicos.backend.persistence.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "indicatorvalue")
public class IndicatorValue {

  @Id
  @Nullable
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double value;

  private LocalDateTime date;

  @ManyToOne
  @JoinColumn(name = "indicator_id")
  @ToString.Exclude
  private Indicator indicatorId;

}
