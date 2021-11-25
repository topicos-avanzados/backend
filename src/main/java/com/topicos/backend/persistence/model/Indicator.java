package com.topicos.backend.persistence.model;

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
@Table(name = "indicators")
public class Indicator {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String unit;

  private String type;

  @Nullable
  private Long frequency;

  private String description;

  @ManyToOne
  @JoinColumn(name = "area_id")
  @ToString.Exclude
  private Area areaId;

  @Nullable
  private Long indicatorLeft;

  @Nullable
  private Long indicatorRight;

  @Nullable
  private String operator;

  @Nullable
  private Double constant;

}
