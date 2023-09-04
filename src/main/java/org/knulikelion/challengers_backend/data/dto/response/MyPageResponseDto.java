package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyPageResponseDto {
    private String name;
    private List<String> clubs;
    private String email;
}
