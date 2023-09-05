package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@ToString
@Builder
public class AllProjectTechStacksResponseDto {

    private String name;

    public AllProjectTechStacksResponseDto(String name) {
        this.name = name;
    }
}
