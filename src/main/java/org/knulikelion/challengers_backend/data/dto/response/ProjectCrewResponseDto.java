package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectCrewResponseDto {
    private Long id;
    private String name;
    private String position;
    private String role;

    public ProjectCrewResponseDto(ProjectCrew projectCrew) {
        this.id = projectCrew.getId();
        this.name = projectCrew.getProjectCrewName();
        this.position = projectCrew.getProjectCrewPosition();
        this.role = projectCrew.getProjectCrewRole();
    }
}
