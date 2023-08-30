package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@ToString
@Builder
public class UserClubResponseDto {
    private String name;
    private String email;

    public UserClubResponseDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
