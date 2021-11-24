package com.topicos.backend.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeIndicatorValueDTO {

    private Long id;

    private IndicatorDTO indicator;

    private Double value;

    private Date date;

    private CompanyDTO company;

}
