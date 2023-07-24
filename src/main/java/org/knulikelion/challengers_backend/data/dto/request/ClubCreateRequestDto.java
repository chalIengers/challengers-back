package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClubCreateRequestDto {
    private String clubName;
    private String logoUrl;
    private String clubDescription;
    private String clubForm;
    private List<UserRequestDto> Members;
}

