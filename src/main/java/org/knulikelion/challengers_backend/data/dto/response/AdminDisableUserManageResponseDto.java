package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminDisableUserManageResponseDto {
    private String userEmail;
    private String userName;
}
