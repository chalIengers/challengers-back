package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectCrewRequestDto {
    private String name;
    private String role;
    private String position;
    private Long projectId; 
}
