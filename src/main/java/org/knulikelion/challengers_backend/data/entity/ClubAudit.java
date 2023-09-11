package org.knulikelion.challengers_backend.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knulikelion.challengers_backend.data.enums.EventType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club_audit")
public class ClubAudit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clubId;

    private EventType eventType;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private String createdBy;

    private String clubName;

}
