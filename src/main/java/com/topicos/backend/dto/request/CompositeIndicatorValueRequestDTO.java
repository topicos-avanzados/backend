package com.topicos.backend.dto.request;

import lombok.*;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeIndicatorValueRequestDTO {

    private Long id;

    private Long indicatorId;

    private List<CompositeIndicatorTypeDTO> valuesToCalculate;

    private Date date;

    private Long companyId;
}
