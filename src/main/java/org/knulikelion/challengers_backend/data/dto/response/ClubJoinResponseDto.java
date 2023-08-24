package org.knulikelion.challengers_backend.data.dto.response;


import lombok.*;
import org.knulikelion.challengers_backend.data.entity.JoinRequestStatus;

@Data
@NoArgsConstructor
@ToString
@Builder
public class ClubJoinResponseDto {

    private Long joinRequestId;
    private Long userId;
    private Long clubId;
    private JoinRequestStatus status;

    public ClubJoinResponseDto(Long joinRequestId, Long userId, Long clubId, JoinRequestStatus status) {
        this.joinRequestId = joinRequestId;
        this.userId = userId;
        this.clubId = clubId;
        this.status = status;
    }
}
