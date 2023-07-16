package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectTechStackRequestDto {
    private String name;
    private String url;
}
