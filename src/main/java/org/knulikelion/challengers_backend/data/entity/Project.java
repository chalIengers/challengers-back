package org.knulikelion.challengers_backend.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;

import javax.persistence.*;
import java.time.Month;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "project")
public class Project extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false,unique = true)
    private String projectName;

    @Column(nullable = false)
    private String projectDescription;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String projectDetail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.MAINTENCE;

    @Column(nullable = false)
    private String projectPeriod;

    @Column(nullable = false)
    private String projectCategory;

    @Column(nullable = true)
    private int updateCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "project")
    private List<ProjectTechStack> techStacks;

    @OneToMany(mappedBy = "project")
    private List<MonthlyViews> monthlyViews;
}
