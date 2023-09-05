package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectResponseDto {
    private Long id;
    private String projectName;
    private String projectDescription;
    private String projectDetail;
    private String imageUrl;
    private String projectStatus;
    private String ProjectPeriod;
    private List<ProjectTechStackResponseDto> projectTechStack;
    private List<ProjectLinkResponseDto> projectLink;
    @Builder.Default
    private Map<String, List<ProjectCrewResponseDto>> projectCrew = new HashMap<>();
    private String projectCategory;
    private String createdAt;
    private String updatedAt;
    private Integer uploadedUserId;
    private Integer belongedClubId;
    private String belongedClubName;
}

/*private List<ProjectCrewResponseDto> projectCrew;*/