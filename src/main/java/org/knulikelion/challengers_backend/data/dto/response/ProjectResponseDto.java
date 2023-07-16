package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.Club;
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
    private Integer projectStatus;
    private String ProjectPeriod;
    private String projectTechStacks;
    private String projectCategory;
    private String createdAt;
    private String updatedAt;
    private Integer uploadedUserId;
    private Integer belongedClubId;
    private String belongedClubName;
    private Club club;
}
