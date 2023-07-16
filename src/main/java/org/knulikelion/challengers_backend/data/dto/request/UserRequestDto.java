package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserRequestDto {
    private String userName;
    private String email;
    private List<ClubRequestDto> clubs;
}
