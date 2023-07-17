package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProjectCrewResponseDto {
    private Long id;
    private String name;
    private String role;
    private String position;
    private Long projectId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
