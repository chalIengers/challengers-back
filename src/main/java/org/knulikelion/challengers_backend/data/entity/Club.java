package org.knulikelion.challengers_backend.data.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
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
    @Column(nullable = false, name = "club_name")
    private String clubName;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "club_description",columnDefinition = "LONGTEXT")
    private String clubDescription;
    @Column(name = "club_form")
    private String clubForm;
    @Column(name ="club_approved",nullable = false)
    private Integer clubApproved;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> users;
}
