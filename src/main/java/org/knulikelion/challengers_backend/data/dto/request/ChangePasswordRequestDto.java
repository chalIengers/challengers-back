package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChangePasswordRequestDto {
    private String changePw;
    private String userPw;

}
