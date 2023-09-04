package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignResponseDto {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String uid;
    private boolean success;
    private String msg;
}
