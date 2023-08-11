package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResponseDto {
    private boolean success;
    private int code;
    private String msg;
}
