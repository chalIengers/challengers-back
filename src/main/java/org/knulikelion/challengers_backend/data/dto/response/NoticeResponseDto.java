package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private String uploadedUserName;
    private String uploadedUserRole;
    private String uploadedUserProfileUrl;
    private String createdAt;
    private String updatedAt;
}
