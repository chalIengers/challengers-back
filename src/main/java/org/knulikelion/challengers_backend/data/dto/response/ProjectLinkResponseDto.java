package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.ProjectLink;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectLinkResponseDto {
    private Long id;
    private String name;
    private String url;

    public ProjectLinkResponseDto(ProjectLink projectLink) {
        this.id = projectLink.getId();
        this.name = projectLink.getLinkName();
        this.url = projectLink.getLinkUrl();
    }
}
