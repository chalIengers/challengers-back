package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class SignInResponseDto extends ResultResponseDto{
    private String token;
    private String email;
    private String userName;
}
