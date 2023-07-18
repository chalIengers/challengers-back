package org.knulikelion.challengers_backend.data.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club")
public class Club {
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
    private boolean clubApproved;
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> users;
}
