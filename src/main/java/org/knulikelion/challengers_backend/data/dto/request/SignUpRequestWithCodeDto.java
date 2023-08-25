package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequestWithCodeDto extends SignUpRequestDto{
    private String inputNumber;
}
