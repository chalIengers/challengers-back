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
}