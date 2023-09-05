package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectRequestDto {
    private String projectName;
    private String ProjectDescription;
    private String imageUrl;
    private String projectDetail;
    private ProjectStatus status;
    private String projectPeriod;
    private String projectCategory;
    private List<ProjectCrewRequestDto> projectCrew;
    private List<ProjectLinkRequestDto> projectLink;
    private List<ProjectTechStackRequestDto> projectTechStack;
    private Long belongedClubId;
}
