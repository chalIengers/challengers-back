package org.knulikelion.challengers_backend.data.entity;


import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false,name = "user_name")
    private String userName;
    @Column(nullable = false, name = "email")
    private String email;
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

}
