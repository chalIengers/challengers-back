package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.enums.EventType;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserAuditResponseDto {
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private EventType eventType;

}