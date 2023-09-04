package org.knulikelion.challengers_backend.data.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateNoticeRequestDto {
    private Long id;
    private String title;
    private String content;
}
