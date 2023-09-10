package org.knulikelion.challengers_backend.data.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.knulikelion.challengers_backend.data.enums.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "project_audit")
public class ProjectAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private EventType eventType;

    private LocalDateTime createdAt;

    private String createdBy;

    private String projectName;

    @Column(nullable = true)
    private LocalDateTime deletedAt = null;
}