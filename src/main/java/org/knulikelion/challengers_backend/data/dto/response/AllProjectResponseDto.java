package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.Project;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AllProjectResponseDto {
    private Long id;
    private String projectName;
    private String projectDescription;
    private String imageUrl;
    private String projectCategory;
    private String belongedClubName;

    public static AllProjectResponseDto from(Project project) {
        AllProjectResponseDto dto = new AllProjectResponseDto();

        dto.setId(project.getId());
        dto.setProjectName(project.getProjectName());
        dto.setProjectDescription(project.getProjectDescription());
        dto.setProjectCategory(project.getProjectCategory());
        dto.setImageUrl(project.getImageUrl());

        if (project.getClub() != null) {
            dto.setBelongedClubName(project.getClub().getClubName());
        } else {
            // You might need to handle logging differently here.
            System.out.println("[Log]클럽이 존재하지 않음");
            dto.setBelongedClubName(null);
        }

        return dto;
    }


}