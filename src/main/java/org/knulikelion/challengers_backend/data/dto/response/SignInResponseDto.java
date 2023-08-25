
package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignInResponseDto extends SignUpResponseDto{
    private String token;
    @Builder
    public SignInResponseDto(boolean succeess, int code, String msg, String token){
        super(succeess,code,msg);
        this.token = token;
    }
}
