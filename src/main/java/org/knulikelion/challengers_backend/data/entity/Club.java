package org.knulikelion.challengers_backend.data.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "club")
public class Club extends BaseEntity{
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
    @Column(name = "club_form") // club_form column 은 피그마 설명 후 수정 예정.
    private String clubForm;
    @Column(name ="club_approved",nullable = false)
    private boolean clubApproved;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserClub> clubMembers = new ArrayList<>();


}
