package org.knulikelion.challengers_backend.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club")
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(nullable = false, name = "club_name",unique = true)
    private String clubName;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "club_description",columnDefinition = "LONGTEXT")
    private String clubDescription;

    @Column(name = "club_form")
    private String clubForm;

    @Column(name ="club_approved",nullable = false)
    private boolean clubApproved=false;

    @ManyToOne
    @JoinColumn(name = "clubManager") /*클럽 생성자 (User 외래키)*/
    private User clubManager;

    @OneToMany(mappedBy = "club",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserClub> members = new ArrayList<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClubJoin> clubJoins = new ArrayList<>();
}
