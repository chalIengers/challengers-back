package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminHomeFeedResponseDto {
    private String contents;
    private String image;
    private String name;
    private String role;
}
