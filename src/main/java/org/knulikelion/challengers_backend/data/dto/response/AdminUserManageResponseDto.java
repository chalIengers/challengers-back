package org.knulikelion.challengers_backend.data.dto.response;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminUserManageResponseDto {
    private String userEmail;
    private String userName;
    private List<String> clubList;
}
