package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@ToString
@Builder
public class PendingUserResponseDto {
    private Long id;
    private String name;
    private String email;

    public PendingUserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
