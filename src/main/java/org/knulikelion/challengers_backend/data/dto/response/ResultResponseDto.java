package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class ResultResponseDto {
    private Integer code;
    private String msg;
}
