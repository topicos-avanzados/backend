package com.topicos.backend.dto.response;

import com.topicos.backend.dto.CompanyDTO;
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
public class UserLoginDTO {

  private String token;

  private CompanyDTO company;


}
