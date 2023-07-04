package org.knulikelion.challengers_backend.data.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false,name = "user_name")
    private String userName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(name = "created_at",updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAT;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<UserClub> clubs = new ArrayList<>();




}
