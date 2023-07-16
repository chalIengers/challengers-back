package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.Project;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectTechStackRequestDto {
    private String name;
}
