package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TokenRequestDto {
    private String refreshToken;
}
