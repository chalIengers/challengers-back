package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminHomeFeedRequestDto {
    private String contents;
    private String image;
}