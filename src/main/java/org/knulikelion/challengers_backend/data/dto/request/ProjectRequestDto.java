package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
import org.knulikelion.challengers_backend.data.dto.ProjectCrewDto;

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
    private Integer projectStatus;
    private String projectPeriod;
    private String projectCategory;
    private List<ProjectCrewDto> projectCrew;
    private List<ProjectLinkRequestDto> projectLink;
    private List<ProjectTechStackRequestDto> projectTechStack;
    private Long uploadedUserId;
    private Long belongedClubId;
}
