package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

import java.util.Set;

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
    //    private List<ProjectCrew> projectCrew;
    private Integer uploadedUserId;
    private Integer belongedClubId;
}
