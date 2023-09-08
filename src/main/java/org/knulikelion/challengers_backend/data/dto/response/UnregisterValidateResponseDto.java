package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UnregisterValidateResponseDto {
    private boolean success;
    private boolean validateUser;
    private boolean memberEmpty;
    private boolean notAdministrator;
    private boolean matchPassword;
}
