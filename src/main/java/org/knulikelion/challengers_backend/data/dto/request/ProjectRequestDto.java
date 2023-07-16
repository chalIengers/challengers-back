package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;

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
    private String projectDescription;
    private String projectDetail;
    private Integer projectStatus;
    private String projectPeriod;
    private String projectTechStacks;
    private String projectCategory;
    private List<ProjectCrewRequestDto> projectCrew;
    private Integer uploadedUserId;
    private Integer belongedClubId;
}
