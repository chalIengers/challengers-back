package org.knulikelion.challengers_backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club_join")
public class ClubJoin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean accepted; // 수락 여부 파악.

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public ClubJoin(User user, Club club, boolean accepted) {
        this.user = user;
        this.club = club;
        this.accepted = accepted;
    }
}
