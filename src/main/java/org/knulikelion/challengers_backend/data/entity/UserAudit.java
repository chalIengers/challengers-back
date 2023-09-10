package org.knulikelion.challengers_backend.data.entity;


import lombok.*;
import org.knulikelion.challengers_backend.data.enums.EventType;

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
    private EventType eventType;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private String userName;
}
