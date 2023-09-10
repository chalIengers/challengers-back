package org.knulikelion.challengers_backend.data.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_audit")
public class UserAudit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long UserId;

    private LocalDateTime deletedAt;
}
