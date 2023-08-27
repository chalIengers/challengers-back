package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClubRequestDto {
    private Long clubId;
    private String clubName;
    private String logoUrl;
    private String clubDescription;
    private String clubForm;
}
