package org.knulikelion.challengers_backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knulikelion.challengers_backend.data.enums.JoinRequestStatus;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club_join")
public class ClubJoin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private JoinRequestStatus status = JoinRequestStatus.PENDING; // 요청 상태 저장.

    @Column(name = "accepted", nullable = false)
    private boolean accepted = false;

    @Column(columnDefinition = "LONGTEXT")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    public ClubJoin(User user, Club club,JoinRequestStatus status) {
        this.user = user;
        this.club = club;
        this.status = status;
    }
}
