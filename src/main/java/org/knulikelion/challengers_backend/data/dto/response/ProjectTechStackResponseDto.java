package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectTechStackResponseDto {
    private Long id;
    private String name;

    public ProjectTechStackResponseDto(ProjectTechStack projectTechStack) {
        this.id = projectTechStack.getId();
        this.name = projectTechStack.getTechStackName();
    }
}
