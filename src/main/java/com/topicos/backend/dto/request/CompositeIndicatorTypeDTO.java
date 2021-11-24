package com.topicos.backend.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeIndicatorTypeDTO {

    private String first_value;

    private String operator;

    private String second_value;
}
