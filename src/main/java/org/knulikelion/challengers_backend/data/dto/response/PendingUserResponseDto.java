package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PendingUserResponseDto {
    private String name;
    private String email;
    private String comment;
}
