package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminClubResponseDto {
    private Long id;
    private String clubName;
    private String masterName;
    private String masterEmail;
}
