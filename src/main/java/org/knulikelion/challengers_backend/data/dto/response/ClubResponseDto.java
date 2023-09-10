package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;
import org.knulikelion.challengers_backend.data.entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClubResponseDto {
    private Long id;
    private String clubName;
    private String logoUrl;
    private String clubDescription;
    private String clubForm;
    private boolean clubApproved;
    private String createdAt;
    private String updatedAt;
    private List<User> clubMembers;
}
