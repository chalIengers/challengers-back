package org.knulikelion.challengers_backend.data.entity;

import lombok.*;

import javax.persistence.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id",nullable = true)
    private Club club;
}
