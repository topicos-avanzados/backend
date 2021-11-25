package com.topicos.backend.dto;

import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {

    private Long id;

    private String email;

    private Date date;

    private String payload;

    public LogDTO(String usernameFromToken, String s) {
        this.email = usernameFromToken;
        this.payload = s;
    }
}
