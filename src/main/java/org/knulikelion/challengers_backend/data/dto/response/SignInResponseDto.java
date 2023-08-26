package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class SignInResponseDto extends ResultResponseDto{
    private String accessToken;
    private String refreshToken;
    private String email;
    private String userName;
}
