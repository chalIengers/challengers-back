package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClubListResponseDto {
    private Long id;
    private String name;
    private String logo;
}
