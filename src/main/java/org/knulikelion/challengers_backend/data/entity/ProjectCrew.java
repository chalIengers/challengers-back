package org.knulikelion.challengers_backend.data.entity;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "project_crew")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class ProjectCrew extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectcrew_id",nullable = false)
    private Long id;

    @Column(name = "projectcrew_name",nullable = false)
    private String projectCrewName;

    @Column(name = "projectcrew_position",nullable = false)
    private String projectCrewPosition;

    @Column(name = "projectcrew_role",nullable = false)
    private String projectCrewRole;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
