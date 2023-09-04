package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordWithCodeRequestDto extends ChangePasswordRequestDto{
    private String approvalNumber;
}
