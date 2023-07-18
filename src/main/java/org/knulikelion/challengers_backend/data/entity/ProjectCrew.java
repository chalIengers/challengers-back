package org.knulikelion.challengers_backend.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "project_crew")

public class ProjectCrew extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "projectcrew_name",nullable = false)
    private String projectCrewName;

    @Column(name = "projectcrew_position",nullable = false)
    private String projectCrewPosition;

    @Column(name = "projectcrew_role",nullable = false)
    private String projectCrewRole;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;
}
