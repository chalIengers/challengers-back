package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserResponseDto {
    private Long id;
    private String userName;
    private String email;
    private String createdAt;
    private String updatedAt;
    private List<String> clubs;
}
