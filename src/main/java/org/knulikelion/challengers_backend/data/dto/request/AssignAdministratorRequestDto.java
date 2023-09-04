package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AssignAdministratorRequestDto {
    private String email;
    private String profileUrl;
    private String role;
}
