package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserClubResponseDto {
    private Long id;
    private String name;
    private String logo;
    private boolean isManager;
    private String managerEmail;
}
