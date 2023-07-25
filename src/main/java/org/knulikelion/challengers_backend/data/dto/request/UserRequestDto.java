package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserRequestDto {
    private String userName;
    private String email;
    @Nullable
    private Long clubId;
}
