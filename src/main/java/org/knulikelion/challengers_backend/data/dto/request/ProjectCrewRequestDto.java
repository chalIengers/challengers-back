package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectCrewRequestDto {
    private Long id;
    private String name;
    private String role;
    private String position;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
