package org.knulikelion.challengers_backend.data.dto.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JoinRequestDto {

    private Long cludId;
    private String comment;
}
